package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/14
 */
public class GoPrivateSync {
    public void start() throws IOException, ParseException {
        com.ruoyi.system.wzgs.sync.GoPrivateSync bean = SpringUtils.getBean(com.ruoyi.system.wzgs.sync.GoPrivateSync.class);
        bean.CoreAlarmSync();
        bean.PositionSync();
    }
}
