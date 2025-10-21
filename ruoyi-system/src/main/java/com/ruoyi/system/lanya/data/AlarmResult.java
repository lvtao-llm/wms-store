package com.ruoyi.system.lanya.data;

import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.domain.WmsArea;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/21
 */
public class AlarmResult {
    public final WmsAlarmRule rule;
    public final WmsArea area;

    public AlarmResult(WmsAlarmRule rule, WmsArea area) {
        this.rule = rule;
        this.area = area;
    }
}
