//package com.ruoyi.framework.websocket;
//
//import com.alibaba.fastjson2.JSONObject;
//import com.ruoyi.common.core.controller.BaseController;
//import com.ruoyi.common.core.domain.AjaxResult;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 区域Controller
// *
// * @author ruoyi
// * @date 2025-09-26
// */
//@RestController
//@RequestMapping("/system/lanya-transfer/message-endpoint")
//public class WebSocketController extends BaseController {
//
//    private static final Logger log = LoggerFactory.getLogger("realtime-data-podcast");
//
//    @Autowired
//    private WebSocketServer webSocketServer;
//
//    /**
//     * 接收消息
//     */
//    @PostMapping
//    public AjaxResult add(@RequestBody JSONObject body) {
//        webSocketServer.messageQueue.add(body.getString("message"));
//        return AjaxResult.success();
//    }
//}
