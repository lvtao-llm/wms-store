package com.ruoyi.system.lanya.data;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/12/24
 */
@Component
@ServerEndpoint(value = "/system/ws-site-notice/{requestId}", configurator = ServerEndpointConfig.Configurator.class)
public class SiteNoticeService {

    /**
     * 存储浏览器会话队列
     */
    private static final Map<Long, Wrap> clientSessions = new ConcurrentHashMap<>();

    @Autowired
    private ISysUserService userService;

    private final Map<Long, SysUser> userCache = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        // 初始化系统用户
        List<SysUser> sysUsers = userService.selectUserList(new SysUser());
        for (SysUser sysUser : sysUsers) {
            userCache.put(sysUser.getUserId(), sysUser);
        }
    }

    /**
     * 客户端连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("requestId") String requestId) throws Exception {
        Long UserId = Long.parseLong(requestId);
        if (!userCache.containsKey(UserId)) {
            userCache.put(UserId, userService.selectUserById(UserId));
        }
        SysUser sysUser = userCache.get(UserId);
        clientSessions.put(UserId, new Wrap(sysUser, session));
    }

    /**
     * 客户端连接关闭时处理
     */
    @OnClose
    public void onClose(Session session, @PathParam("requestId") String requestId) throws IOException {
        Long UserId = Long.parseLong(requestId);
        clientSessions.remove(UserId);
    }

    /**
     * 客户端抛出异常时处理
     */
    @OnError
    public void onError(Session session, @PathParam("requestId") String requestId, Throwable exception) throws Exception {
        Long UserId = Long.parseLong(requestId);
        clientSessions.remove(UserId);
    }

    /**
     * 接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("requestId") String requestId) {

    }

    public void noticeTo(SysUser sysUser, String content) {
        clientSessions.get(sysUser.getUserId()).session.getAsyncRemote().sendText(content);
    }

    @Data
    @AllArgsConstructor
    private static class Wrap {
        private SysUser sysUser;
        private Session session;
    }
}
