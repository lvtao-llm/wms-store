package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.MlPositionHistory;
import com.ruoyi.system.domain.WmsTrajectory;
import com.ruoyi.system.lanya.data.LanyaDataSync;
import org.springframework.stereotype.Component;

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
            bean.PositionSync("position_history");
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
        LanyaDataSync bean = SpringUtils.getBean(LanyaDataSync.class);
        try {
            bean.generateMockPositionHistoryData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public WmsTrajectory ConvertWmsTrajectory(MlPositionHistory mlPositionHistory) {
        WmsTrajectory trajectory = new WmsTrajectory();

        trajectory.setTrajectoryId(mlPositionHistory.getId());
        trajectory.setTrajectoryType(mlPositionHistory.getCardType());
        trajectory.setTrajectoryBegin(mlPositionHistory.getAcceptTime());
        trajectory.setTrajectoryEnd(mlPositionHistory.getAcceptTime());
        trajectory.setCardRecordId(mlPositionHistory.getId());
        trajectory.setTrajectoryPoints(mlPositionHistory.getLatitude() + "," + mlPositionHistory.getLongitude());
        trajectory.setRemark("");
        trajectory.setCreateTime(mlPositionHistory.getAcceptTime());
        trajectory.setUpdateTime(mlPositionHistory.getAcceptTime());
        trajectory.setCreateBy("system");
        trajectory.setUpdateBy("system");

        return trajectory;
    }

}
