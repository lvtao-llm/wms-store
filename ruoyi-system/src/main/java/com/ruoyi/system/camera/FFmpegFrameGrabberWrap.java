package com.ruoyi.system.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/13
 */
public class FFmpegFrameGrabberWrap {

    private static final Logger log = LoggerFactory.getLogger(FFmpegFrameGrabberWrap.class);

    private final String id;
    private final String rtspUrl;
    private String mpegCommand;
    private final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private Process ffmpegProcess;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private LocalDateTime time = LocalDateTime.now();
    private ServerSocket serverSocket;
    private boolean running = true;
    private Thread socketThread;
    private int dynamicPort;
    ConcurrentLinkedQueue<ByteBuffer> queue = new ConcurrentLinkedQueue<>();

    public FFmpegFrameGrabberWrap(String id, String rtspUrl, String mpegCommand) {
        this.id = id;
        this.rtspUrl = rtspUrl;
        this.mpegCommand = mpegCommand;
    }

    public void startIfNotRunning() throws IOException {
        if (ffmpegProcess != null && ffmpegProcess.isAlive()) return;

        startSocketService();

        try {
            executor.submit(() -> {
                byte[] errBuf = new byte[4096];
                int errRead;
                while (true) {
                    ByteBuffer buffer = queue.poll();
                    if (buffer != null) {
                        for (Session s : sessions) {
                            if (s.isOpen()) {
                                s.getAsyncRemote().sendBinary(buffer, result -> {
                                    if (!result.isOK()) {
                                        log.error("发送数据错误:{}", result.getException());
                                    }
                                });
                            } else {
                                sessions.remove(s);
                                log.info("有客户端关闭:{}", s);
                            }
                        }
                        this.time = LocalDateTime.now();
                    }
                }
            });

            this.mpegCommand = this.mpegCommand + "tcp://127.0.0.1:" + dynamicPort;
//            this.mpegCommand = "ffmpeg -rtsp_transport tcp -max_delay 500000 -i " + rtspUrl + " -vf fps=2 -c:v libx264 -f flv ";
            String[] commandArray = this.mpegCommand.split("\\s+");

            ProcessBuilder pb = new ProcessBuilder(commandArray);
            ffmpegProcess = pb.start();
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
                        log.error("FFMpeg进程信息:{}", new String(errBuf, 0, errRead));
                    } catch (IOException e) {
                        log.error("FFMpeg进程读取错误信息错误:{}", e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(ByteBuffer payload) {

        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getAsyncRemote().sendBinary(payload, result -> {
                    if (!result.isOK()) {
                        // 发送失败
                    }
                });
            } else {
                sessions.remove(s);
                log.info("有客户端关闭:{}", s);
            }
            this.time = LocalDateTime.now();
        }
    }

    // 启动Socket服务接收FFmpeg流
    public void startSocketService() {
        try {
            InetAddress bindAddr = InetAddress.getByName("127.0.0.1");
            serverSocket = new ServerSocket(0, 50, bindAddr);
            this.dynamicPort = serverSocket.getLocalPort();
            log.info("FFmpegFrameGrabberWrap创建ServerSocket动态分配端口: {}", dynamicPort);
            running = true;

            socketThread = new Thread(() -> {
                try {
                    while (running) {
                        Socket clientSocket = serverSocket.accept();
                        handleClientConnection(clientSocket);
                    }
                } catch (IOException e) {
                    if (running) {
                        log.error("Socket服务错误", e);
                    }
                }
            });
            socketThread.start();
        } catch (IOException e) {
            log.error("创建ServerSocket失败", e);
        }
    }

    // 处理客户端连接，转发数据到WebSocket
    private void handleClientConnection(Socket clientSocket) {
        new Thread(() -> {
            try (InputStream inputStream = clientSocket.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    // 转发给所有WebSocket会话
//                    log.info("开始转发数据给所有WebSocket会话");
                    ByteBuffer payload = ByteBuffer.wrap(buffer, 0, bytesRead);
                    queue.offer(payload);
//                    broadcast(payload);
                }
            } catch (IOException e) {
                log.error("处理客户端连接错误", e);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ignored) {
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }
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

    public String getRtspUrl() {
        return this.rtspUrl;
    }

    public String getMpegCommand() {
        return this.mpegCommand;
    }
}
