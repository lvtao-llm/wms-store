package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/14
 */
public class GoPrivateSync {
    private static final Logger log = LoggerFactory.getLogger("sync-go-private");

    public void start() throws IOException, ParseException {
        com.ruoyi.system.wzgs.sync.GoPrivateSync bean = SpringUtils.getBean(com.ruoyi.system.wzgs.sync.GoPrivateSync.class);
        try {
            bean.CoreAlarmSync();
        } catch (Exception e) {
            log.error("同步凯德信定位卡SOS报警数据到物资公司发生错误", e);
        }
        try {
            bean.PositionHistorySync();
        } catch (Exception e) {
            log.error("同步凯德信定位卡历史数据到物资公司发生错误", e);
        }
        try{
            bean.PositionRealtimeSync();
        }catch (Exception e){
            log.error("同步凯德信定位卡实时数据到物资公司发生错误", e);
        }
    }
}
