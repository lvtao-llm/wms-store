package com.ruoyi.system.lanya.data;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/12/24
 */
@Component
@ServerEndpoint(value = "/system/lanya-transfer/ws/{requestId}", configurator = ServerEndpointConfig.Configurator.class)
public class SiteNoticeService {

    @Autowired
    private ISysUserService userService;

    private Map<Long, SysUser> userCache = new LinkedHashMap<>();

    /**
     * 存储浏览器会话队列
     */
    private static final Map<SysUser, Session> clientSessions = new ConcurrentHashMap<>();

    /**
     * 客户端连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("requestId") String requestId) throws Exception {

    }

    /**
     * 客户端连接关闭时处理
     */
    @OnClose
    public void onClose(Session session, @PathParam("requestId") String requestId) throws IOException {

    }

    /**
     * 客户端抛出异常时处理
     */
    @OnError
    public void onError(Session session, @PathParam("requestId") String requestId, Throwable exception) throws Exception {

    }

    /**
     * 接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("requestId") String requestId) {

    }
}
