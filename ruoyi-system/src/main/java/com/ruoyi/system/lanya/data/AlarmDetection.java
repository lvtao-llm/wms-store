package com.ruoyi.system.lanya.data;

import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.service.IWmsAlarmRuleService;
import com.ruoyi.system.service.IWmsAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Point;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class AlarmDetection {

    private static final Logger log = LoggerFactory.getLogger(AlarmDetection.class);

    /**
     * 告警规则服务
     */
    @Autowired
    private IWmsAlarmRuleService wmsAlarmRuleService;

    /**
     * 区域服务
     */
    @Autowired
    private IWmsAreaService wmsAreaService;

    /**
     * 告警规则与区域映射
     */
    private final Map<WmsAlarmRule, RuleAreaWrap> rules = new HashMap<>();

    /**
     * 活跃卡列表
     */
    private final Map<Long, LiveCard> livePeopleCards = new HashMap<>();

    /**
     * 活跃车辆列表
     */
    private final Map<Long, LiveCard> liveVehicleCards = new HashMap<>();

    /**
     * 创建点对象工厂
     */
    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * 是否被初始化
     */
    private boolean isInitialized = false;

    /**
     * 区域服务
     */
    Map<Long, WmsArea> wmsAreas;

    /**
     * 加载必要的数据
     */
    @PostConstruct
    public void loadAlarmRules() {
        if (isInitialized) {
            return;
        }

        wmsAreas = wmsAreaService.getAreaMap();

        // 初始化区域Geometry
        for (WmsArea area : wmsAreas.values()) {
            if (area.getAreaPolygon() != null && area.getAreaPolygonDouble().size() > 2) {
                // 创建JTS多边形
                Geometry polygon = createPolygonFromArea(area.getAreaPolygonDouble());
                area.setGeometry(polygon);
            }
        }

        List<WmsAlarmRule> wmsAlarmRules = wmsAlarmRuleService.selectWmsAlarmRuleList(new WmsAlarmRule());

        // 遍历规则
        for (WmsAlarmRule rule : wmsAlarmRules) {
            if (rule.getAlarmRuleTargetAreaCode() == null) {
                // 跳过无效规则
                continue;
            }

            // 获取区域数组
            String[] targetCodes = rule.getAlarmRuleTargetAreaCode().split(",");

            // 规则的目标区域列表
            List<WmsArea> value = new ArrayList<>();

            // 遍历目标区域Code
            for (String c : targetCodes) {

                // 匹配目标区域的WmsArea对象
                for (WmsArea area : wmsAreas.values()) {
                    if (area.getAreaId() == Integer.parseInt(c) && area.getGeometry() != null) {
                        rules.put(rule, new RuleAreaWrap(area, area.getGeometry()));
                        break;
                    }
                }
            }
        }

        isInitialized = true;
    }

    /**
     * 从区域坐标点创建JTS多边形
     */
    private Geometry createPolygonFromArea(List<double[]> polygonPoints) {
        try {
            Coordinate[] coordinates = new Coordinate[polygonPoints.size() + 1];
            for (int i = 0; i < polygonPoints.size(); i++) {
                coordinates[i] = new Coordinate(polygonPoints.get(i)[0], polygonPoints.get(i)[1]);
            }
            // 闭合多边形
            coordinates[coordinates.length - 1] = new Coordinate(polygonPoints.get(0)[0], polygonPoints.get(0)[1]);

            LinearRing ring = geometryFactory.createLinearRing(coordinates);
            return geometryFactory.createPolygon(ring);
        } catch (Exception e) {
            log.error("Error createPolygonFromArea", e);
            return null;
        }
    }

    public List<AlarmResult> detect(LanyaPositionHistory position) {
        Point point = geometryFactory.createPoint(new Coordinate(position.getLongitude(), position.getLatitude()));
        // 发现的告警规则
        List<AlarmResult> detected = new ArrayList<>();

        long cardId = position.getCardId();
        Date time = position.getAcceptTime();

        if (!livePeopleCards.containsKey(cardId)) {
            livePeopleCards.put(cardId, new LiveCard(position));
        }
        LiveCard liveCard = livePeopleCards.get(cardId);
        liveCard.point = point;

        // 当前卡的geometry对象

        for (WmsArea area : wmsAreaService.getAreaMap().values()) {
            // 判断点是否在多边形内
            boolean inside = area.getGeometry().contains(point);

            if (inside) {
                if (liveCard.getArea() != null && liveCard.getArea().getPersonCount() >= 0) {
                    liveCard.getArea().setPersonCount(liveCard.getArea().getPersonCount() - 1);
                }
                liveCard.setArea(area);
                liveCard.getArea().setPersonCount(liveCard.getArea().getPersonCount() + 1);
            }
        }


        // 遍历所有告警规则
        for (Map.Entry<WmsAlarmRule, RuleAreaWrap> entry : rules.entrySet()) {

            // 规则
            WmsAlarmRule rule = entry.getKey();

            // 目标区域
            RuleAreaWrap targetAreaWrap = entry.getValue();

            // 是否发生了报警
            boolean isAlarm = false;

            // 判断点是否在多边形内
            boolean inside = targetAreaWrap.geometry.contains(point);

            // 计算点到多边形边界的距离(度单位)
            double dist = point.distance(targetAreaWrap.geometry) * 111320;

            // 根据规则类型使用不同的检测方法
            switch (rule.getAlarmRuleType()) {
                case "进入报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!targetAreaWrap.enterTime.containsKey(cardId)) {
                            targetAreaWrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = targetAreaWrap.enterTime.get(cardId);
                        isAlarm = (time.getTime() - enterTime) >= rule.getAlarmRuleTimeThreshold();
                    }
                    break;
                }
                case "越界报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!targetAreaWrap.enterTime.containsKey(cardId)) {
                            targetAreaWrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = targetAreaWrap.enterTime.get(cardId);
                        isAlarm = (time.getTime() - enterTime) >= rule.getAlarmRuleTimeThreshold();
                    }
                    break;
                }
                case "滞留报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!targetAreaWrap.enterTime.containsKey(cardId)) {
                            targetAreaWrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = targetAreaWrap.enterTime.get(cardId);
                        isAlarm = (time.getTime() - enterTime) >= rule.getAlarmRuleTimeThreshold();
                    }
                    break;
                }
                case "超员报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!targetAreaWrap.enterTime.containsKey(cardId)) {
                            targetAreaWrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = targetAreaWrap.enterTime.get(cardId);
                        Long stayTime = time.getTime() - enterTime;
                        if (stayTime >= rule.getAlarmRuleTimeThreshold()) {
                            if (liveCard.onAreaWrap == null) {
                                liveCard.onAreaWrap = targetAreaWrap;
                            }
                            if (liveCard.onAreaWrap != targetAreaWrap) {
                                liveCard.onAreaWrap.enterTime.remove(cardId);
                                liveCard.onAreaWrap = targetAreaWrap;
                            }

                            if (!targetAreaWrap.enterTime.containsKey(cardId)) {
                                targetAreaWrap.enterTime.put(cardId, time.getTime());
                                isAlarm = targetAreaWrap.enterTime.size() > rule.getMaxPeopleCount();
                            }
                        }
                    }
                    break;
                }
                case "聚集报警": {
                    if (rule.getMaxPeopleCount() == null) {
                        break;
                    }
                    List<List<Point>> lists = groupPointsByDistance(livePeopleCards.values().stream()
                            .map(c -> c.point)
                            .filter(Objects::nonNull)
                            .toArray(Point[]::new), rule.getAlarmRuleDistThreshold());
                    for (List<Point> list : lists) {
                        isAlarm = list.size() > rule.getMaxPeopleCount() || isAlarm;
                    }
                    break;
                }
            }
            if (isAlarm) {
                detected.add(new AlarmResult(entry.getKey(), entry.getValue().area));
            }


        }
        return detected;
    }

    /**
     * 根据距离将坐标点分组，距离不超过指定阈值的点归为一组
     *
     * @param points            经纬度坐标点数组
     * @param distanceThreshold 距离阈值(米)
     * @return 分组后的点列表
     */
    public List<List<Point>> groupPointsByDistance(Point[] points, double distanceThreshold) {
        List<List<Point>> groups = new ArrayList<>();
        boolean[] visited = new boolean[points.length];

        for (int i = 0; i < points.length; i++) {
            if (visited[i]) continue;

            // 创建新组
            List<Point> group = new ArrayList<>();
            group.add(points[i]);
            visited[i] = true;

            // 使用队列进行广度优先搜索
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(i);

            while (!queue.isEmpty()) {
                int currentIndex = queue.poll();
                Point currentPoint = points[currentIndex];

                // 检查未访问的点
                for (int j = 0; j < points.length; j++) {
                    if (visited[j]) continue;

                    Point otherPoint = points[j];
                    // 计算两点间距离(转换为米)
                    double distance = currentPoint.distance(otherPoint) * 111320;

                    // 如果距离小于等于阈值，归为同一组
                    if (distance <= distanceThreshold) {
                        group.add(points[j]);
                        visited[j] = true;
                        queue.offer(j);
                    }
                }
            }

            groups.add(group);
        }

        return groups;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public List<Object> getLiveVehicleTotal() {
        return new ArrayList<>();
    }

    public List<Object> getLivePeopleTotal() {
        return new ArrayList<>();
    }

    public List<Object> getPeopleAlarmToday() {
        return new ArrayList<>();
    }

    public List<Object> getVehicleAlarmToday() {
        return new ArrayList<>();
    }

    public List<Object> getHeightRiskAreaTotal() {
        return new ArrayList<>();
    }

    static class RuleAreaWrap {
        public WmsArea area;
        public Geometry geometry;
        public Map<Long, Long> enterTime = new HashMap<>();

        public RuleAreaWrap(WmsArea area, Geometry geometry) {
            this.area = area;
            this.geometry = geometry;
        }
    }

    private class LiveCard {
        public LanyaPositionHistory position;
        public RuleAreaWrap onAreaWrap;
        public Point point;
        public Long cardId;
        public String realName;
        private WmsArea area;

        public LiveCard(LanyaPositionHistory position) {
            this.position = position;
            this.cardId = position.getCardId();
            this.realName = position.getRealName();
        }

        public WmsArea getArea() {
            return area;
        }

        public void setArea(WmsArea area) {
            this.area = area;
        }
    }
}
