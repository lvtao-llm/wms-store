package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.MlPositionHistory;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.service.IMlPositionHistoryService;
import com.ruoyi.quartz.service.ISysJobLogService;
import com.ruoyi.quartz.service.impl.MlPositionHistoryServiceImpl;
import com.ruoyi.quartz.util.AbstractQuartzJob;
import com.ruoyi.system.domain.WmsTrajectory;
import com.ruoyi.system.service.IWmsTrajectoryService;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/9/29
 */
@Component
public class PointHistorySync {
    public void PointHistoryIncrementSync() {
        IMlPositionHistoryService mlPositionHistoryService = SpringUtils.getBean(IMlPositionHistoryService.class);
        IWmsTrajectoryService trajectoryService = SpringUtils.getBean(IWmsTrajectoryService.class);
        System.out.println("开始同步");
        List<MlPositionHistory> mlPositionHistories = mlPositionHistoryService.selectMlPositionHistoryListFromId(0L);
        for (MlPositionHistory mlPositionHistory : mlPositionHistories) {
            trajectoryService.insertWmsTrajectory(ConvertWmsTrajectory(mlPositionHistory));
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
