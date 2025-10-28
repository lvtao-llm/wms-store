package com.ruoyi.framework.websocket;

import org.apache.tomcat.websocket.WsWebSocketContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.WebSocketContainer;

/**
 * websocket 配置
 *
 * @author ruoyi
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebSocketClient webSocketClient() {
        WebSocketContainer container = new WsWebSocketContainer();
        // 设置二进制消息缓冲区大小（以字节为单位）
        container.setDefaultMaxBinaryMessageBufferSize(5120000);
        // 设置文本消息缓冲区大小（以字节为单位）
        container.setDefaultMaxTextMessageBufferSize(5120000);
        // 设置会话空闲超时时间（以毫秒为单位）
        container.setDefaultMaxSessionIdleTimeout(15 * 60000L);

        StandardWebSocketClient client = new StandardWebSocketClient(container);
        // 设置合理的缓冲区大小
        client.getUserProperties().put("org.apache.tomcat.websocket.textBufferSize", 1024 * 1024 * 1024); // 1MB
        client.getUserProperties().put("org.apache.tomcat.websocket.binaryBufferSize", 1024 * 1024 * 1024); // 1MB
        // 设置连接超时
        client.getUserProperties().put("org.apache.tomcat.websocket.CONNECT_TIMEOUT", 600000); // 30秒
        // 设置写入超时
        client.getUserProperties().put("org.apache.tomcat.websocket.WRITE_TIMEOUT", 600000); // 10分钟
        // 设置会话超时
        client.getUserProperties().put("javax.websocket.session.timeout", 600000); // 10分钟
        return client;
    }
}
