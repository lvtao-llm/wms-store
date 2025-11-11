package com.ruoyi.system.lanya.data;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/17
 */
@Component

public class LanyaDataSync {
    private static final Logger log = LoggerFactory.getLogger(LanyaDataSync.class);

    /**
     * Lanya定位数据数据服务
     */
    @Autowired
    private ILanyaPositionHistoryService lanyaPositionHistoryService;

    /**
     * 系统配置服务
     */
    @Autowired
    private ISysConfigService configService;

    /**
     * 报警检测服务
     */
    @Autowired
    private AlarmDetection alarmDetection;

    /**
     * 报警日志服务
     */
    @Autowired
    private IWmsAlarmLogService wmsAlarmLogService;

    /**
     * Lanya设备卡发送日志服务
     */
    @Autowired
    private ILanyaDeviceCardSenderLogService lanyaDeviceCardSenderLogService;

    /**
     * 设备卡工作日志服务
     */
    @Autowired
    private IWmsDeviceCardWorkLogService wmsDeviceCardWorkLogService;

    /**
     * 轨迹服务
     */
    @Autowired
    private IWmsTrajectoryService wmsTrajectoryService;

    /**
     * 区域服务
     */
    @Autowired
    private IWmsAreaService wmsAreaService;

    /**
     * 定时同步Lanya定位数据开关
     */
    @Value("${lanya.position.sync.enabled:false}")
    private boolean enablePosition;

    /**
     * 定时同步Lanya设备卡发送日志开关
     */
    @Value("${lanya.position.mock.enabled:false}")
    private boolean enableMock;

    /**
     * 定时同步Lanya设备卡发放日志开关
     */
    @Value("${lanya.issuing.sync.enabled:false}")
    private boolean enableIssuing;

    /**
     * 定时同步Lanya定位数据表名
     */
    String positionKey = "lanya.position.sync.offset";

    /**
     * 定时同步Lanya设备卡发放日志表名
     */
    String cardLogKey = "lanya.card_log.sync.offset";

    /**
     * 定时同步Lanya定位数据表名配置
     */
    SysConfig positionConfig = new SysConfig(positionKey);

    /**
     * 定时同步Lanya设备卡发放日志表名配置
     */
    SysConfig cardLogConfig = new SysConfig(cardLogKey);

    /**
     * 工作活动缓存
     */
    Map<Long, WmsDeviceCardWorkLog> workActivities = new HashMap<>();

    /**
     * 工作活动缓存，按卡ID缓存
     */
    Map<Long, WmsDeviceCardWorkLog> workActivitiesByCardId = new HashMap<>();

    /**
     * 时间格式
     */
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 时间格式
     */
    SimpleDateFormat sdfTableSuffix = new SimpleDateFormat("yyyyMMdd");

    // 大庆市萨尔图区中心坐标
    double centerLat = 46.6346017782;  // 纬度
    double centerLng = 124.8472070448; // 经度
    // 地球半径(米)
    double earthRadius = 6371000;

    // 以中心点为中心，边长2公里的正方形坐标
    double leftLat = 124.8382070448;
    double rightLat = 124.8562070448;
    double topLng = 46.6436017782;
    double bottomLng = 46.6256017782;

    /**
     * 创建LanyaPositionHistory数据模拟器
     */
    public void generateMockPositionHistoryData() {
        if (!enableMock) {
            return;
        }

        Random random = new Random();
        Date now = new Date();

        Long[] cardIds = new Long[]{4791L, 77561L, 81126L, 111L, 222L, 333L};
        String[] persons = new String[]{"单北斗81126", "吕涛11", "啊啊啊", "不不不", "新卡2", "于1111"};
        Long[] personIds = new Long[]{1925015210049478657L, 1972900380282490881L, 1977660310642294785L, 1978327655543013378L, 1924763411983966210L, 1924719928770424834L};
        int personIndex = random.nextInt(persons.length);
        Long cardId = cardIds[personIndex];
        String person = persons[personIndex];
        Long personId = personIds[personIndex];


        LanyaPositionHistory position = new LanyaPositionHistory();

        // 基本信息
        position.setAcceptTime(now); // 24小时内随机时间
        position.setCardId(cardId);
        position.setCardType("card"); // 0或1
        position.setBeaconId(-1L);

        // 位置信息 (大庆市萨尔图区半径2公里内坐标)
        // 生成半径内的随机坐标点
        double radiusInMeters = 2000; // 2公里
        double distance = Math.sqrt(random.nextDouble()) * radiusInMeters;
        double angle = random.nextDouble() * 2 * Math.PI;

        // 计算新坐标点
        double deltaLat = distance * Math.cos(angle) / earthRadius;
        double deltaLng = distance * Math.sin(angle) / (earthRadius * Math.cos(Math.toRadians(centerLat)));

        double newLat = centerLat + Math.toDegrees(deltaLat);
        double newLng = centerLng + Math.toDegrees(deltaLng);

        BigDecimal latBigDecimal = new BigDecimal(newLat).setScale(10, RoundingMode.HALF_UP);
        BigDecimal lngBigDecimal = new BigDecimal(newLng).setScale(10, RoundingMode.HALF_UP);

        position.setLatitude(latBigDecimal.doubleValue());
        position.setLongitude(lngBigDecimal.doubleValue());
        position.setDistance(1.0); // 0-100米距离
        position.setLayerId("全图");
        position.setLayerHeight(0); // 0-10米高度

        // 人员信息
        position.setPersonId(personId);
        position.setPersonType("staff"); // 0-2
        position.setRealName(person);

        // 其他信息
        position.setCreateTime(now);

        lanyaPositionHistoryService.insertLanyaPositionHistory(position);
    }


    @PostConstruct
    public void init() throws Exception {
        if (!alarmDetection.isInitialized()) {
            alarmDetection.loadAlarmRules();
        }
        List<SysConfig> sysConfigs = configService.selectConfigList(new SysConfig());
        for (SysConfig sysConfig : sysConfigs) {
            if (sysConfig.getConfigKey().equals(positionKey)) {
                positionConfig = sysConfig;
            }
            if (sysConfig.getConfigKey().equals(cardLogKey)) {
                cardLogConfig = sysConfig;
            }
        }

        // 初始化未同步的layan device_sender_card_log表数据到wms_device_card_work_log表
        CardLogSync();

        // 初始化未同步的layan position_history表数据到wms_alarm_log表
        // 历史position_history 按日期分表的表名
        List<String> tableNames = lanyaPositionHistoryService.showPositionHistoryTableNames();

        // 最后同步的position_history表的时间
        Date positionOffset = sdfDateTime.parse(positionConfig.getConfigValue());

        for (String tableName : tableNames) {
            if ("position_history_calendar".equals(tableName)) {
                return;
            }

            Date dateSuffix = sdfTableSuffix.parse(tableName.substring(tableName.length() - 8));

            // 只有当表日期大于等于最后同步日期时才执行同步
            if (dateSuffix.compareTo(positionOffset) >= 0) {
                PositionSync(tableName);
            }
        }
    }

    public void CardLogSync() throws Exception {
        if (!enableIssuing) {
            return;
        }
        String cardLogOffsetVal = configService.selectConfigByKey(cardLogKey);
        Date cardLogOffset = sdfDateTime.parse(cardLogOffsetVal);
        Long timestamp = cardLogOffset.getTime();
        boolean continueGet = true;
        while (continueGet) {
            List<LanyaDeviceCardSenderLog> lanyaDeviceCardSenderLogs = lanyaDeviceCardSenderLogService.selectLanyaDeviceCardSenderLogListStartTime(cardLogOffset, 10);
            for (LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog : lanyaDeviceCardSenderLogs) {

                // 发卡
                if (lanyaDeviceCardSenderLog.getCardSenderType() == 1) {
                    // 发卡记录
                    WmsDeviceCardWorkLog wordLog = new WmsDeviceCardWorkLog();
                    wordLog.setCardId(lanyaDeviceCardSenderLog.getCardId());
                    wordLog.setPersonPhoto(lanyaDeviceCardSenderLog.getPersonPhoto());
                    wordLog.setPersonId(lanyaDeviceCardSenderLog.getPersonId());
                    wordLog.setDeptId(lanyaDeviceCardSenderLog.getDeptId());
                    wordLog.setRealName(lanyaDeviceCardSenderLog.getRealName());
                    wordLog.setDeptName(lanyaDeviceCardSenderLog.getDeptName());
                    wordLog.setIdNumber(lanyaDeviceCardSenderLog.getIdNumber());
                    wordLog.setIdentifyType(lanyaDeviceCardSenderLog.getIdentifyType());
                    wordLog.setSenderDeviceSn(lanyaDeviceCardSenderLog.getDeviceSn());
                    wordLog.setSenderDeviceName(lanyaDeviceCardSenderLog.getDeviceName());
                    wordLog.setSenderDeviceNum(lanyaDeviceCardSenderLog.getDeviceNum());
                    wordLog.setSenderCommandTime(lanyaDeviceCardSenderLog.getCommandTime());
                    wordLog.setSenderRentType(lanyaDeviceCardSenderLog.getRentType());
                    wordLog.setSenderIdentifyTime(lanyaDeviceCardSenderLog.getIdentifyTime());
                    wordLog.setSenderLanyaLogId(lanyaDeviceCardSenderLog.getId());
                    wordLog.setCreateTime(new Date());

                    // 保存workLog记录
                    if (wmsDeviceCardWorkLogService.selectWmsDeviceCardWorkLogCountBySenderLanyaLogId(lanyaDeviceCardSenderLog.getId()) == 0) {
                        if (wmsDeviceCardWorkLogService.insertWmsDeviceCardWorkLog(wordLog) > 0) {
                            initPosition(wordLog);
                            workActivities.put(lanyaDeviceCardSenderLog.getId(), wordLog);
                            workActivitiesByCardId.put(lanyaDeviceCardSenderLog.getCardId(), wordLog);
                        }
                    }
                }

                // 还卡
                if (lanyaDeviceCardSenderLog.getCardSenderType() == 0 && lanyaDeviceCardSenderLog.getRealName() != null) {
                    // 还卡记录
                    WmsDeviceCardWorkLog query = new WmsDeviceCardWorkLog();
                    query.setCardId(lanyaDeviceCardSenderLog.getCardId());
                    query.setRealName(lanyaDeviceCardSenderLog.getRealName());
                    List<WmsDeviceCardWorkLog> wmsDeviceCardWorkLogs = wmsDeviceCardWorkLogService.selectWmsDeviceCardWorkLogListEnd(query);
                    if (wmsDeviceCardWorkLogs.isEmpty()) {
                        log.error("--------------------------------------------------------------\r\nERROR:没有找到对应的发卡记录>\r\n{}\r\n--------------------------------------------------------------", query);
                    }

                    if (wmsDeviceCardWorkLogs.size() > 1) {
                        log.error("--------------------------------------------------------------\r\nERROR:找到多个发卡记录>\r\n{}\r\n--------------------------------------------------------------", query);
                    }

                    if (!wmsDeviceCardWorkLogs.isEmpty()) {
                        WmsDeviceCardWorkLog workLog = wmsDeviceCardWorkLogs.get(wmsDeviceCardWorkLogs.size() - 1);
                        workLog.setReturnDeviceSn(lanyaDeviceCardSenderLog.getDeviceSn());
                        workLog.setReturnDeviceName(lanyaDeviceCardSenderLog.getDeviceName());
                        workLog.setReturnDeviceNum(lanyaDeviceCardSenderLog.getDeviceNum());
                        workLog.setReturnCommandTime(lanyaDeviceCardSenderLog.getCommandTime());
                        workLog.setReturnLanyaLogId(lanyaDeviceCardSenderLog.getId());
                        workLog.setUpdateTime(new Date());

                        // 更新workLog记录
                        if (wmsDeviceCardWorkLogService.updateWmsDeviceCardWorkLog(workLog) > 0) {
                            completePosition(workLog);

                            // 轨迹记录
                            WmsTrajectory wmsTrajectory = new WmsTrajectory();
                            wmsTrajectory.setWorkId(workLog.getId());
                            wmsTrajectory.setTrajectoryBegin(workLog.getCreateTime());
                            wmsTrajectory.setTrajectoryEnd(workLog.getReturnCommandTime());
                            wmsTrajectory.setTrajectoryType("人员");
                            wmsTrajectory.setFuzzy(workLog.getRealName() + "-" + workLog.getSenderCommandTime() + "-内部员工");

                            // 轨迹点
                            List<Map<String, Object>> trajectoryPoints = new ArrayList<>();
                            List<LanyaPositionHistory> trajectory = workActivitiesByCardId.get(workLog.getCardId()).getTrajectory();
                            for (int i = 0; i < trajectory.size(); i += 2) {
                                if (i + 1 < trajectory.size()) {
                                    Map<String, Object> point = getStringObjectMap(trajectory, i);
                                    trajectoryPoints.add(point);
                                }
                            }
                            wmsTrajectory.setTrajectoryPoints(JSON.toJSONString(trajectoryPoints));

                            // 保存轨迹
                            wmsTrajectoryService.insertWmsTrajectory(wmsTrajectory);

                            // 移除workLog
                            workActivities.remove(workLog.getId());
                            workActivitiesByCardId.remove(workLog.getCardId());
                        }
                    }
                }

                cardLogOffset = lanyaDeviceCardSenderLog.getCreateTime();
                cardLogConfig.setConfigValue(sdfDateTime.format(cardLogOffset));
                configService.updateConfig(cardLogConfig);
            }

            continueGet = !lanyaDeviceCardSenderLogs.isEmpty();
        }
    }

    private static Map<String, Object> getStringObjectMap(List<LanyaPositionHistory> trajectory, int i) {
        Map<String, Object> point = new HashMap<>();
        point.put("id", trajectory.get(i).getId());
        point.put("longitude", trajectory.get(i).getLongitude());
        point.put("latitude", trajectory.get(i).getLatitude());
        point.put("distance", trajectory.get(i).getDistance());
        point.put("time", trajectory.get(i).getAcceptTime().getTime());
        point.put("beaconId", trajectory.get(i).getBeaconId());
        return point;
    }

    private void initPosition(WmsDeviceCardWorkLog workLog) {
    }

    private void completePosition(WmsDeviceCardWorkLog workLog) {
    }

    public void PositionSync(String tableName) throws ParseException {
        if (!enablePosition) {
            return;
        }
        if (!alarmDetection.isInitialized()) {
            alarmDetection.loadAlarmRules();
        }
        // position_history表的最后同步id
        String positionOffsetVal = configService.selectConfigByKey(positionKey);
        Date positionOffset = sdfDateTime.parse(positionOffsetVal);

        // 是否继续同步
        boolean continueGet = true;

        // 同步position_history表数据
        while (continueGet) {

            // 获取count条position_history数据
            int count = 100;
            List<LanyaPositionHistory> lanyaPositionHistories = lanyaPositionHistoryService.selectLanyaPositionHistoryListStartTime(positionOffset, count, tableName);

            // 循环处理数据
            for (LanyaPositionHistory position : lanyaPositionHistories) {

                // 检测是否触发告警规则
                List<AlarmResult> alarmRules = alarmDetection.detect(position);


                // 如果有告警规则触发则在workActivities查询匹配的wms_device_card_work_log记录ID
                if (!alarmRules.isEmpty()) {
                    // wms_device_card_work_log记录ID
                    Long workId = null;

                    for (WmsDeviceCardWorkLog workLog : workActivities.values()) {
                        if (position.getCardId().equals(workLog.getCardId()) && position.getPersonId().equals(workLog.getPersonId())) {
                            workId = workLog.getId();
                        }
                        break;
                    }

                    // 如果workActivities中没有匹配的wms_device_card_work_log记录ID则到数据库中查找
                    if (workId == null) {
                        workId = wmsDeviceCardWorkLogService.selectWmsDeviceCardWorkLogIdInTimeByCardAndName(position.getAcceptTime(), position.getCardId(), position.getRealName());
                    }

                    if (workId == null) {
                        log.error("未找到对应的设备卡记录");
                    }

                    // 插入告警日志
                    for (AlarmResult wmsAlarmRule : alarmRules) {
                        WmsAlarmLog log = new WmsAlarmLog();
                        log.setAlarmBehavior(wmsAlarmRule.rule.getAlarmRuleType() + "(" + wmsAlarmRule.rule.getAlarmRuleName() + ")");
                        log.setCardRecordId(workId);
                        log.setAlarmEnterTime(position.getAcceptTime());
                        log.setAreaCode(wmsAlarmRule.area.getAreaCode());
                        log.setAlarmLocation(position.getLatitude() + "," + position.getLongitude());
                        log.setPersonName(position.getRealName());
                        log.setWorkId(workId);
                        log.setAlarmType("人员");

                        wmsAlarmLogService.insertWmsAlarmLog(log);
                    }
                }

                // 添加轨迹给卡工作日志
                WmsDeviceCardWorkLog workLog = workActivitiesByCardId.get(position.getCardId());
                if (workLog != null) {
                    workLog.getTrajectory().add(position);  //经度
                }

                // 更新本地position_history的offset
                positionOffset = position.getAcceptTime();

                // 更新sys_config表中最后positon_history同步的offset
                positionConfig.setConfigValue(sdfDateTime.format(positionOffset));
                configService.updateConfig(positionConfig);
            }

            // 检查是否继续获取数据
            continueGet = !lanyaPositionHistories.isEmpty();
        }
    }


}
