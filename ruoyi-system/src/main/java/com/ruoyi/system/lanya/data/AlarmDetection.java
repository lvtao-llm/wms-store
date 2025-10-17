package com.ruoyi.system.lanya.data;

import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.service.IWmsAlarmRuleService;
import com.ruoyi.system.service.IWmsAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/17
 */
@Component
public class AlarmDetection {

    @Autowired
    private IWmsAlarmRuleService wmsAlarmRuleService;
    @Autowired
    private IWmsAreaService wmsAreaService;
    private Map<WmsAlarmRule, List<WmsArea>> rules = new HashMap<>();

    @PostConstruct
    private void loadAlarmRules() {
        WmsArea queryArea = new WmsArea();
        List<WmsArea> wmsAreas = wmsAreaService.selectWmsAreaList(queryArea);
        WmsAlarmRule queryRule = new WmsAlarmRule();
        List<WmsAlarmRule> wmsAlarmRules = wmsAlarmRuleService.selectWmsAlarmRuleList(queryRule);
        for (WmsAlarmRule r : wmsAlarmRules) {
            String[] targetCodes = r.getAlarmRuleTargetAreaCode().split(",");
            List<WmsArea> value = new ArrayList<>();
            for (String c : targetCodes) {
                List<WmsArea> result = wmsAreas.stream()
                        .filter(obj -> obj.getId() == 2)
                        .collect(Collectors.toList());
            }
        }
    }

    private final Region region;
    private Long enterTime; // null=当前不在区域内

    public PointDistanceTimer(Region region) {
        this.region = region;
    }

    /**
     * 每收到一个点位调用一次，返回是否触发报警
     */
    public boolean update(double x, double y, long nowMs) {
        double dist = region.minDistToEdge(x, y);
        boolean inside = region.contains(x, y);

        /* 1. 距离阈值过滤：最近边 < 阈值 视为“不在” */
        if (dist < region.getDistThreshold()) {
            if (enterTime != null) {     // 之前算在区域内，现在被挤出
                enterTime = null;
            }
            return false;                // 肯定不报警
        }

        /* 2. 真正在区域内 */
        if (inside) {
            if (enterTime == null) {     // 首次进入
                enterTime = nowMs;
                return false;            // 还未超时
            }
            // 已在区域内，检查是否超时
            return (nowMs - enterTime) >= region.getTimeThresholdMs();
        }

        /* 3. 在“缓冲区”外且不在多边形内 */
        if (enterTime != null) {         // 离开区域
            enterTime = null;
        }
        return false;
    }

    /**
     * 手动重置（可选）
     */
    public void reset() {
        enterTime = null;
    }

    public WmsAlarmRule[] detect(Long cardId, Long personId, Long beaconId, BigDecimal longitude, BigDecimal latitude) {


    }
}
