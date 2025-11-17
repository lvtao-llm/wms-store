package com.ruoyi.system.camera;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStream;
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
public class FFmpegWrap {

    private static final Logger log = LoggerFactory.getLogger(FFmpegWrap.class);

    @Getter
    private final String id;
    @Getter
    private final String tag;
    @Getter
    private final String rtspUrl;
    @Getter
    private final String mpegCommand;
    @Getter
    private final Set<Channel> sessions = ConcurrentHashMap.newKeySet();
    @Getter
    private Process process;
    @Getter
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    @Getter
    private LocalDateTime time = LocalDateTime.now();
    private volatile ByteBuf flvHeader;       // FLV header + Metadata
    private volatile ByteBuf lastKeyframe;    // 最新关键帧
    private ByteBuf tempCache = Unpooled.buffer(1024 * 1024);
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

    public void broadcast(ByteBuf buf) {
        // 解析 FLV tag 类型，如果是 Header 或 Metadata
//        if (isFlvHeader(buf)) {
//            flvHeader = copyBuf(buf);
//        } else if (isVideoKeyframe(buf)) {
//            lastKeyframe = copyBuf(buf);
//        }

        for (Channel ch : sessions) {
            ch.writeAndFlush(new BinaryWebSocketFrame(buf.retainedDuplicate()));
            this.time = LocalDateTime.now();
        }
    }

    public void stop() {
        // 先清理所有会话
        for (Channel session : sessions) {
            if (session.isOpen()) {
                session.close();
            }
        }
        sessions.clear();

        // 再停止FFmpeg进程
        if (process != null) {
            try {
                process.destroy();
                if (!process.waitFor(5, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
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

    /**
     * 判断是否是完整 FLV Header + Metadata
     *
     * @param buf 接收到的 FLV 数据
     * @return true 如果是 Header + Metadata
     */
    public static boolean isFlvHeader(ByteBuf buf) {
        if (buf.readableBytes() < 13) { // Header最少9B + 第一个PreviousTagSize(4B)
            return false;
        }

        // 前3字节必须是 'F','L','V'
        if (buf.getByte(0) != 'F' || buf.getByte(1) != 'L' || buf.getByte(2) != 'V') {
            return false;
        }

        // DataOffset = Header长度，通常是9
        int dataOffset = buf.getInt(5); // buf[5-8]是DataOffset
        return dataOffset == 9;
    }

    /**
     * 判断是否是视频关键帧（H.264 IDR）
     *
     * @param buf 完整的 FLV tag
     * @return true 如果是视频关键帧
     */
    public static boolean isVideoKeyframe(ByteBuf buf) {
        if (buf.readableBytes() < 11) return false; // 不够最小 FLV tag

        int tagType = buf.getByte(0) & 0xFF;
        if (tagType != 9) return false; // 9 = 视频 Tag

        // FLV tag header长度 = 11B, Tag Data 从 offset 11 开始
        int dataOffset = 11;
        if (buf.readableBytes() <= dataOffset) return false;

        int firstByte = buf.getByte(dataOffset) & 0xFF;
        int frameType = (firstByte & 0xF0) >> 4;
        int codecId = firstByte & 0x0F;

        // H.264关键帧：frameType=1 (keyframe), codecId=7 (AVC/H.264)
        return frameType == 1 && codecId == 7;
    }

    /**
     * 缓存 Header 或关键帧
     */
    public static ByteBuf copyBuf(ByteBuf buf) {
        return Unpooled.copiedBuffer(buf);
    }

    public void addClient(Channel channel) {
        sessions.add(channel);
//        // 先发送 Header + Metadata
//        if (flvHeader != null) {
//            channel.writeAndFlush(flvHeader.retainedDuplicate());
//        }
//
//        // 再发送最新关键帧
//        if (lastKeyframe != null) {
//            channel.writeAndFlush(lastKeyframe.retainedDuplicate());
//        }
    }

    public ByteBuf getTempCache() {
        return tempCache;
    }
}
