package com.ruoyi.system.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/13
 */
public class FFmpegFrameGrabberWrap {

    private static final Logger log = LoggerFactory.getLogger(FFmpegFrameGrabberWrap.class);

    private final String id;
    private final String rtspUrl;
    private final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private Process ffmpegProcess;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private LocalDateTime time = LocalDateTime.now();

    public FFmpegFrameGrabberWrap(String id, String rtspUrl) {
        this.id = id;
        this.rtspUrl = rtspUrl;
    }

    public void startIfNotRunning() throws IOException {
        if (ffmpegProcess != null && ffmpegProcess.isAlive()) return;

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-rtsp_transport", "tcp",
                    "-max_delay", "500000",
                    "-use_wallclock_as_timestamps", "1",
                    "-i", rtspUrl,
                    "-c:v", "libx264",
                    "-preset", "ultrafast",
                    "-tune", "zerolatency",
                    "-g", "25",
                    "-r", "25",
                    "-c:a", "aac",
                    "-f", "flv",
                    "-bufsize", "2M",
                    "-"
            );
//            pb.redirectErrorStream(true);      // 合并 stderr 到 stdout
            ffmpegProcess = pb.start();

            InputStream stdout = ffmpegProcess.getInputStream();
            executor.submit(() -> {
                byte[] buf = new byte[4096];
                int read;
                while (true) {
                    try {
                        if (ffmpegProcess == null) {
                            log.debug("g");
                            break;
                        }

                        read = stdout.read(buf);

                        if (read == -1) {
                            log.debug("f");
                            continue;
                        }

                        broadcast(buf, read);

                    } catch (IOException ioe) {
                        log.error("读取缓冲区错误:{}", ioe.getMessage());
                    }
                }
                log.debug("h");
            });

            InputStream stderr = ffmpegProcess.getErrorStream();
            executor.submit(() -> {
                byte[] errBuf = new byte[4096];
                int errRead;
                while (true) {
                    try {
                        errRead = stderr.read(errBuf);
                        if (errRead == -1) {
                            continue;
                        }
                        // 可以选择记录或忽略错误信息
                        log.error("错误信息:{}", new String(errBuf, 0, errRead));
                    } catch (IOException ignored) {
                        log.error("读取错误信息错误:{}", ignored.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(byte[] data, int len) {
        ByteBuffer payload = ByteBuffer.wrap(data, 0, len);
        for (Session s : sessions) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendBinary(payload.duplicate());
                } catch (IOException ignored) {
                }
            } else {
                sessions.remove(s);
                log.info("有客户端关闭:{}", s);
            }
            this.time = LocalDateTime.now();
        }
    }

    public void stop() {
        // 先清理所有会话
        for (Session session : new ArrayList<>(sessions)) {
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (IOException e) {
                log.warn("关闭会话失败", e);
            }
        }
        sessions.clear();

        // 再停止FFmpeg进程
        if (ffmpegProcess != null) {
            try {
                ffmpegProcess.destroy();
                if (!ffmpegProcess.waitFor(5, TimeUnit.SECONDS)) {
                    ffmpegProcess.destroyForcibly();
                    ffmpegProcess.waitFor(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ffmpegProcess.destroyForcibly();
            }
            ffmpegProcess = null;
        }

        // 最后关闭线程池
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
                    log.warn("Executor未及时终止");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executor = null;
        }
    }


    public String getId() {
        return id;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
