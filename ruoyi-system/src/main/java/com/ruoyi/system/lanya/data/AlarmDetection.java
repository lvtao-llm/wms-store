package com.ruoyi.system.lanya.data;

import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.service.IWmsAlarmRuleService;
import com.ruoyi.system.service.IWmsAreaService;
import lombok.Getter;
import lombok.Setter;
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
    private IWmsAlarmRuleService wmsAlarmRuleService;

    @Autowired
    private void setWmsAlarmRuleService(IWmsAlarmRuleService wmsAlarmRuleService) {
        this.wmsAlarmRuleService = wmsAlarmRuleService;
    }

    /**
     * 区域服务
     */
    private IWmsAreaService wmsAreaService;

    @Autowired
    private void setWmsAreaService(IWmsAreaService wmsAreaService) {
        this.wmsAreaService = wmsAreaService;
    }

    /**
     * 告警规则与区域映射
     */
    @Getter
    private final List<RuleAreaWrap> ruleAreaWraps = new ArrayList<>();

    /**
     * 告警规则
     */
    @Getter
    private List<WmsAlarmRule> wmsAlarmRules;

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
    @Getter
    @Setter
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

        wmsAlarmRules = wmsAlarmRuleService.selectWmsAlarmRuleList(new WmsAlarmRule());

        // 初始化规则
        for (WmsAlarmRule rule : wmsAlarmRules) {
            if (rule.getAlarmRuleTargetAreaCode() == null) {
                // 跳过没有绑定区域的无效规则
                continue;
            }

            // 获取区域数组
            String[] targetCodes = rule.getAlarmRuleTargetAreaCode().split(",");

            // 遍历目标区域Code
            for (String c : targetCodes) {
                long targetCode = Long.parseLong(c);
                WmsArea area = wmsAreas.get(targetCode);
                if (area != null) {
                    ruleAreaWraps.add(new RuleAreaWrap(rule, area, area.getGeometry()));
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

    public List<AlarmResult> detect(LanyaPositionHistory position, String type, String[] testAreaName) {
        Point point = geometryFactory.createPoint(new Coordinate(position.getLongitude(), position.getLatitude()));
        // 发现的告警规则
        List<AlarmResult> detected = new ArrayList<>();
        // 定位卡ID
        long cardId = position.getCardId();
        // 定位数据产生时间
        Date time = position.getAcceptTime();

        if (!livePeopleCards.containsKey(cardId)) {
            livePeopleCards.put(cardId, new LiveCard(position));
        }
        LiveCard liveCard = livePeopleCards.get(cardId);
        liveCard.point = point;

        // 区域内人员计数器
        for (WmsArea area : wmsAreaService.getAreaMap().values()) {
            if (area.getGeometry() == null) {
                continue;
            }
            // 判断点是否在区域多边形内
            boolean inside = area.getGeometry().contains(point);

            if (inside) {
                // 如果liveCard的区域不为空，表示定位卡ID在上一个点位potion已经在区域内。那么把定位卡ID从之前所处的区域中移除。并且将定位卡ID更改到当前的区域内。
                // 从定位卡ID在之前所处的区域中移除定位卡ID的计数器中移除。
                if ("内部员工".equals(type) && liveCard.getArea() != null && liveCard.getArea().getStaffCount() >= 0) {
                    // 将之前所处区域-1
                    liveCard.getArea().setStaffCount(liveCard.getArea().getStaffCount() - 1);
                    // 将当前定位卡ID所处的区域的计数器加1
                    area.setStaffCount(liveCard.getArea().getStaffCount() - 1);
                }
                if ("外部访客".equals(type) && liveCard.getArea() != null && liveCard.getArea().getVisitorCount() >= 0) {
                    // 将之前所处区域-1
                    liveCard.getArea().setVisitorCount(liveCard.getArea().getVisitorCount() - 1);
                    // 将当前定位卡ID所处的区域的计数器加1
                    area.setVisitorCount(liveCard.getArea().getVisitorCount() - 1);
                }
                if ("车辆".equals(type) && liveCard.getArea() != null && liveCard.getArea().getVehicleCount() >= 0) {
                    // 将之前所处区域-1
                    liveCard.getArea().setVehicleCount(liveCard.getArea().getVehicleCount() - 1);
                    // 将当前定位卡ID所处的区域的计数器加1
                    area.setVehicleCount(liveCard.getArea().getVehicleCount() - 1);
                }
                // 将当前定位卡ID所处的区域赋值给liveCard
                liveCard.setArea(area);
            }
        }


        // 遍历所有告警规则
        for (RuleAreaWrap wrap : ruleAreaWraps) {

            if (testAreaName != null && Arrays.stream(testAreaName).noneMatch(wrap.area.getAreaName()::equals)) {
                // 跳出测试
                continue;
            }
            // 规则
            WmsAlarmRule rule = wrap.rule;

            if (!"人员".equals(rule.getAlarmType())) {
                // 只检测人员报警规则
                continue;
            }

            // 是否发生了报警
            boolean isAlarm = false;

            // 判断点是否在多边形内
            boolean inside = wrap.geometry.contains(point);

            // 计算点到多边形边界的距离(度单位)
//            double dist = point.distance(wrap.geometry) * 111320;
            double dist = calculateDistanceToBoundary(point, wrap.geometry) * 0.8;

            // 根据规则类型使用不同的检测方法
            switch (rule.getAlarmRuleType()) {
                case "进入报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!wrap.enterTime.containsKey(cardId)) {
                            wrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = wrap.enterTime.get(cardId);
                        isAlarm = (time.getTime() - enterTime) >= rule.getAlarmRuleTimeThreshold();
                    }
                    break;
                }
                case "越界报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!wrap.enterTime.containsKey(cardId)) {
                            wrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = wrap.enterTime.get(cardId);
                        isAlarm = (time.getTime() - enterTime) >= rule.getAlarmRuleTimeThreshold();
                    }
                    break;
                }
                case "滞留报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!wrap.enterTime.containsKey(cardId)) {
                            wrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = wrap.enterTime.get(cardId);
                        isAlarm = (time.getTime() - enterTime) >= rule.getAlarmRuleTimeThreshold();
                    }
                    break;
                }
                case "超员报警": {
                    if (inside && dist < rule.getAlarmRuleDistThreshold()) {
                        if (!wrap.enterTime.containsKey(cardId)) {
                            wrap.enterTime.put(cardId, time.getTime());
                        }
                        Long enterTime = wrap.enterTime.get(cardId);
                        long stayTime = time.getTime() - enterTime;
                        if (stayTime >= rule.getAlarmRuleTimeThreshold()) {
                            if (liveCard.onAreaWrap == null) {
                                liveCard.onAreaWrap = wrap;
                            }
                            if (liveCard.onAreaWrap != wrap) {
                                liveCard.onAreaWrap.enterTime.remove(cardId);
                                liveCard.onAreaWrap = wrap;
                            }

                            if (!wrap.enterTime.containsKey(cardId)) {
                                wrap.enterTime.put(cardId, time.getTime());
                                isAlarm = wrap.enterTime.size() > rule.getMaxPeopleCount();
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
                detected.add(new AlarmResult(wrap.rule, wrap.area));
                wrap.rule.setCount(wrap.rule.getCount() + 1);
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

    /**
     * 计算点到多边形边界的距离
     * 如果点在多边形内部，返回到边界的最短距离
     * 如果点在多边形外部，返回到边界的距离
     */
    private double calculateDistanceToBoundary(Point point, Geometry polygon) {
        // 先检查点是否在多边形内部
        boolean isInside = polygon.contains(point);

        if (isInside) {
            // 如果在内部，计算到边界的距离
            // 获取多边形的边界（LineString）
            Geometry boundary = polygon.getBoundary();
            // 计算点到边界的距离
            return point.distance(boundary) * 111320; // 转换为米
        } else {
            // 如果在外部，使用原始距离计算
            return point.distance(polygon) * 111320; // 转换为米
        }
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

    /**
     * 规则区域包装类
     */
    static public class RuleAreaWrap {
        public final WmsAlarmRule rule;
        public WmsArea area;
        public Geometry geometry;
        public Map<Long, Long> enterTime = new HashMap<>();

        public RuleAreaWrap(WmsAlarmRule rule, WmsArea area, Geometry geometry) {
            this.rule = rule;
            this.area = area;
            this.geometry = geometry;
        }
    }

    /**
     * 临时存储正在移动的卡
     */
    static class LiveCard {
        @Getter
        @Setter
        private WmsArea area;
        public LanyaPositionHistory position;
        public RuleAreaWrap onAreaWrap;
        public Point point;
        public Long cardId;
        public String realName;

        public LiveCard(LanyaPositionHistory position) {
            this.position = position;
            this.cardId = position.getCardId();
            this.realName = position.getRealName();
        }
    }
}
