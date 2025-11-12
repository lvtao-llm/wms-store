package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.MlPositionHistory;
import com.ruoyi.system.domain.WmsTrajectory;
import com.ruoyi.system.lanya.data.LanyaDataSync;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/9/29
 */
@Component
public class PointHistorySync {
    public void PointHistoryIncrementSync() {
        LanyaDataSync bean = SpringUtils.getBean(LanyaDataSync.class);
        try {
            SimpleDateFormat sdfTableSuffix = new SimpleDateFormat("yyyyMMdd");
            bean.PositionSync("position_history_" + sdfTableSuffix.format(new Date()));
            bean.PositionSync1();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void CardLogIncrementSync() {
        LanyaDataSync bean = SpringUtils.getBean(LanyaDataSync.class);
        try {
            bean.CardLogSync();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void MockPositionHistoryData() {
//        WebSocketServer bean = SpringUtils.getBean(WebSocketServer.class);
//        try {
//            bean.generateMockPositionHistoryData();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
    }
}
