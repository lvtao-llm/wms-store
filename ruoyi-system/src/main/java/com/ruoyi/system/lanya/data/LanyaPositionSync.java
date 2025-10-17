package com.ruoyi.system.lanya.data;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.domain.WmsAlarmLog;
import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/17
 */
@Component
public class LanyaPositionSync {
    @Autowired
    private ILanyaPositionHistoryService lanyaPositionHistoryService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private AlarmDetection alarmDetection;

    @Autowired
    private IWmsAlarmLogService wmsAlarmLogService;



    String key = "lanya.sync.offset";
    SysConfig config = new SysConfig(key);

    public void sync() {
        long offset = Long.parseLong(configService.selectConfigByKey("lanya.sync.offset"));
        List<LanyaPositionHistory> lanyaPositionHistories = lanyaPositionHistoryService.selectLanyaPositionHistoryListStartById(offset);
        for (LanyaPositionHistory p : lanyaPositionHistories) {

            WmsAlarmRule[] alarmRules = alarmDetection.detect(p.getCardId(), p.getPersonId(), p.getBeaconId(), p.getLongitude(), p.getLatitude());

            for (WmsAlarmRule wmsAlarmRule : alarmRules) {
                WmsAlarmLog log = new WmsAlarmLog();
                log.setAlarmBehavior(wmsAlarmRule.getAlarmRuleName());
                wmsAlarmLogService.insertWmsAlarmLog(log);
            }

            config.setConfigValue(p.getId().toString());
            configService.updateConfig(config);
        }
    }
}
