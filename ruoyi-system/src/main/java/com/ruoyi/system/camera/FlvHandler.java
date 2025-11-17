package com.ruoyi.system.camera;

import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

// http://localhost:8866/live?url=rtsp://admin:VZCDOY@192.168.2.84:554/Streaming/Channels/102
// ws://localhost:8866/live?url=rtsp://admin:VZCDOY@192.168.2.84:554/Streaming/Channels/102
@Slf4j
@Service
@Sharable //不new，采用共享handler
public class FlvHandler extends SimpleChannelInboundHandler<Object> {


    private WebSocketServerHandshaker handshaker;

    /**
     * 网络超时
     */
    @Value("${mediaserver.netTimeout:15000000}")
    private String netTimeout;
    /**
     * 读写超时
     */
    @Value("${mediaserver.readOrWriteTimeout:15000000}")
    private String readOrWriteTimeout;

    /**
     * 无人拉流观看是否自动关闭流
     */
    @Value("${mediaserver.autoClose:true}")
    private boolean autoClose;

    /**
     * 无人拉流观看持续多久自动关闭，1分钟
     */
    @Value("${mediaserver.autoClose.noClientsDuration:60000}")
    private long noClientsDuration;

    @Autowired
    private CameraServeice cameraServeice;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            String uri = req.uri();  // 比如 /live/123
            if (uri.startsWith("/live/")) {
                String id = uri.substring("/live/".length());
                System.out.println("客户端连接 ID = " + id);


                if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
                    // http请求
                    sendFlvReqHeader(ctx);
//                mediaService.playForHttp(camera, ctx);

                } else {
                    ctx.channel().attr(AttributeKey.valueOf("streamId")).set(id);
                    // websocket握手，请求升级

                    // 参数分别是ws地址，子协议，是否扩展，最大frame长度
                    WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                            getWebSocketLocation(req), null, true, 5 * 1024 * 1024);
                    handshaker = factory.newHandshaker(req);
                    if (handshaker == null) {
                        WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                    } else {
                        handshaker.handshake(ctx.channel(), req);
                        if (cameraServeice.activeWrap.containsKey(id)) {
                            FFmpegWrap wrap = cameraServeice.activeWrap.get(id);
                            if (wrap != null) {
                                wrap.addClient(ctx.channel()); // 发送 Header + 最新关键帧
                                wrap.startIfNotRunning();
                            }
                        }
                    }
                }
            } else {
                log.info("请求错误");
            }
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketRequest(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 断开连接
        String id = (String) ctx.channel().attr(AttributeKey.valueOf("streamId")).get();
        if (cameraServeice.activeWrap.containsKey(id)) {
            cameraServeice.activeWrap.get(id).getSessions().remove(ctx.channel());
            if (cameraServeice.activeWrap.get(id).getSessions().isEmpty()) {
                cameraServeice.activeWrap.get(id).stop();
                cameraServeice.activeWrap.remove(id);
            }
        }
    }

    /**
     * ws握手地址
     */
    private String getWebSocketLocation(FullHttpRequest request) {
        String location = request.headers().get(HttpHeaderNames.HOST) + request.uri();
        return "ws://" + location;
    }

    /**
     * 发送req header，告知浏览器是flv格式
     *
     * @param ctx
     */
    private void sendFlvReqHeader(ChannelHandlerContext ctx) {
        HttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        rsp.headers()
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
                .set(HttpHeaderNames.CONTENT_TYPE, "video/x-flv").set(HttpHeaderNames.ACCEPT_RANGES, "bytes")
                .set(HttpHeaderNames.PRAGMA, "no-cache").set(HttpHeaderNames.CACHE_CONTROL, "no-cache")
                .set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED).set(HttpHeaderNames.SERVER, "zhang");
        ctx.writeAndFlush(rsp);
    }

    /**
     * 错误请求响应
     *
     * @param ctx
     * @param status
     */
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("请求地址有误: " + status + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        String id = (String) ctx.channel().attr(AttributeKey.valueOf("streamId")).get();
        if (cameraServeice.activeWrap.containsKey(id)) {
            cameraServeice.activeWrap.get(id).getSessions().remove(ctx.channel());
            if (cameraServeice.activeWrap.get(id).getSessions().isEmpty()) {
                cameraServeice.activeWrap.get(id).stop();
                cameraServeice.activeWrap.remove(id);
            }
        }
    }

    /**
     * websocket处理
     *
     * @param ctx
     * @param frame
     */
    private void handleWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 关闭
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            String id = (String) ctx.channel().attr(AttributeKey.valueOf("streamId")).get();
            if (cameraServeice.activeWrap.containsKey(id)) {
                cameraServeice.activeWrap.get(id).getSessions().remove(ctx.channel());
                if (cameraServeice.activeWrap.get(id).getSessions().isEmpty()) {
                    cameraServeice.activeWrap.get(id).stop();
                    cameraServeice.activeWrap.remove(id);
                }
            }
            return;
        }

        // 握手PING/PONG
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 文本
        if (frame instanceof TextWebSocketFrame) {
            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {
            return;
        }
    }
}