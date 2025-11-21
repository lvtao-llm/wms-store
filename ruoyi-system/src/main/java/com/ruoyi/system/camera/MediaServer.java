package com.ruoyi.system.camera;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.unix.PreferredDirectByteBufAllocator;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * flv摄像头服务
 *
 * @author ZJ
 */
@Component
public class MediaServer {
    private static final Logger log = LoggerFactory.getLogger("camera-stream");

    @Autowired
    private FlvHandler flvHandler;

    public void start(InetSocketAddress socketAddress) {
        //new 一个主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //new 一个工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup(200);
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();

                        socketChannel.pipeline()
                                .addLast(new HttpResponseEncoder())
                                .addLast(new HttpRequestDecoder())
                                .addLast(new ChunkedWriteHandler())
                                .addLast(new HttpObjectAggregator(64 * 1024))
                                .addLast(new CorsHandler(corsConfig))
                                .addLast(flvHandler);
                    }
                })
                .localAddress(socketAddress)
                .option(ChannelOption.SO_BACKLOG, 128)
                //首选直接内存
                .option(ChannelOption.ALLOCATOR, PreferredDirectByteBufAllocator.DEFAULT)
                //设置队列大小
//                .option(ChannelOption.SO_BACKLOG, 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_RCVBUF, 128 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 1024 / 2, 1024 * 1024));
        //绑定端口,开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            log.info("摄像头服务启动开始监听端口: {}", socketAddress.getPort());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("摄像头服务启动失败: {}", e.getMessage());
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workGroup.shutdownGracefully();
        }
    }
}