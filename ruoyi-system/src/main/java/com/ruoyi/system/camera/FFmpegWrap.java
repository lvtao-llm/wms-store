package com.ruoyi.system.camera;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/13
 */
@Data
public class FFmpegWrap {

    private static final Logger log = LoggerFactory.getLogger("camera-stream");

    @Getter
    private final String id;
    @Getter
    private final String tag;
    @Getter
    private final String rtspUrl;
    @Getter
    private final String mpegCommand;
    @Getter
    private final Set<Channel> channels = ConcurrentHashMap.newKeySet();
    @Getter
    private Process process;
    @Getter
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    @Getter
    private LocalDateTime time = LocalDateTime.now();

    private boolean running = true;

    private volatile byte[] flvHeader; // 缓存 FLV Header

    public FFmpegWrap(String id, String tag, String rtspUrl, String mpegCommand) {
        this.id = id;
        this.tag = tag;
        this.rtspUrl = rtspUrl;
        this.mpegCommand = mpegCommand;
    }

    public void startIfNotRunning() throws IOException {
        if (process != null && process.isAlive()) return;
        String[] commandArray = this.mpegCommand.split("\\s+");
        try {
            ProcessBuilder pb = new ProcessBuilder(commandArray);
            process = pb.start();

            InputStream stderr = process.getErrorStream();
            executor.submit(() -> {
                byte[] errBuf = new byte[4096];
                int errRead;
                while (!Thread.currentThread().isInterrupted() && running) {
                    try {
                        if (process == null) {
                            log.warn("{}-ffmpeg进程已关闭", this.id);
                            break;
                        }
                        if (!process.isAlive()) {
                            log.warn("{}-ffmpeg进程已关闭", this.id);
                            break;
                        }
                        // 在这里能拿到Thread.currentThread().interrupt()的事件吗
                        errRead = stderr.read(errBuf);
                        if (errRead == -1) {
                            continue;
                        }
                        String msg = new String(errBuf, 0, errRead);
                        log.info("{}-ffmpeg进程信息:{}", this.id, msg);
                    } catch (IOException e) {
                        log.error("{}-ffmpeg进程读取错误信息错误:{}", this.id, e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            log.error("{}-ffmpeg进程启动错误:{}", this.id, e.getMessage());
        }
    }

    public void broadcast(ByteBuf buf) {
        byte[] data = new byte[buf.readableBytes()];
        buf.getBytes(0, data); // 取出完整字节，不破坏原 buf

        // 记录 FLV Header（只记录一次）
        if (flvHeader == null && data.length >= 13 && data[0] == 'F' && data[1] == 'L' && data[2] == 'V') {
            flvHeader = Arrays.copyOf(data, 13);
        }

        // 给所有客户端发送
        for (Channel ch : channels) {
            if (ch.isActive()) {
                ch.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(data))); // 保持原样二进制
            } else {
                ch.close();
                channels.remove(ch);
            }
        }
    }

    public void stop() {
        this.running = false;

        // 先清理所有会话
        for (Channel session : channels) {
            if (session.isOpen()) {
                session.close();
            }
        }
        channels.clear();

        // 再停止FFmpeg进程
        if (process != null) {
            try {
                process.destroy();
                if (!process.waitFor(5, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                    if (!process.waitFor(5, TimeUnit.SECONDS)) {
                        throw new RuntimeException("进程未能及时终止");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                process.destroyForcibly();
            }
            process = null;
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

    public void addClient(Channel channel) {
        channels.add(channel);
    }
}
