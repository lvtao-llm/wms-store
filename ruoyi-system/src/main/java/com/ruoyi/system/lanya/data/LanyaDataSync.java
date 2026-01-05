package com.ruoyi.system.lanya.data;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SysJobLogMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Autowired
    private SiteNoticeService siteNoticeService;

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

    @Autowired
    private ILanyaPositionCurrentService lanyaPositionCurrentService;

    /**
     * 定时同步Lanya定位数据开关
     */
    @Value("${lanya.position.sync-table.enabled:false}")
    private boolean enablePositionTableSync;

    /**
     * 启动时同步Lanya定位历史数据开关
     */
    @Value("${lanya.position.sync-table.past-table.enabled:false}")
    private boolean enablePositionTableSyncPast;

    /**
     * 定时同步Lanya定位数据表数据量
     * 默认100
     */
    @Value("${lanya.position.sync-table.count-per-fetch:100}")
    private int countPerFetch;

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

    /**
     * 时间格式
     */
    SimpleDateFormat sdfChineseDateTime = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");

    /**
     * 已发现的规则报警
     */
    Set<String> discoveredSmsAlarm = new HashSet<>();

    /**
     * 已发现的规则报警
     */
    Set<String> discoveredImAlarm = new HashSet<>();

    /**
     * 已发现的规则报警
     */
    Set<String> discoveredSysAlarm = new HashSet<>();

    /**
     * HTTP客户端
     */
    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private ISysUserService userService;

    private Map<Long, SysUser> userCache = new LinkedHashMap<>();

    /**
     * 每天00:00清除已发现的报警
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearDiscoveredAlarm() throws Exception {
        discoveredSmsAlarm.clear();
        discoveredImAlarm.clear();
        discoveredSysAlarm.clear();
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

        if (!enablePositionTableSyncPast) {
            return;
        }

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

        // 初始化系统用户
        List<SysUser> sysUsers = userService.selectUserList(new SysUser());
        for (SysUser sysUser : sysUsers) {
            userCache.put(sysUser.getUserId(), sysUser);
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
                if (lanyaDeviceCardSenderLog.getCardSenderType() == 1 &&
                        lanyaDeviceCardSenderLog.getCardId() != null &&
                        lanyaDeviceCardSenderLog.getRealName() != null &&
                        "成功".equals(lanyaDeviceCardSenderLog.getResult())) {

                    // 发卡记录
                    WmsDeviceCardWorkLog workLog = parseWorkLog(lanyaDeviceCardSenderLog);

                    // 保存workLog记录
                    saveWorkLog(workLog);
                }

                // 还卡
                if (lanyaDeviceCardSenderLog.getCardSenderType() == 0 && lanyaDeviceCardSenderLog.getRealName() != null) {
                    // 还卡记录
                    WmsDeviceCardWorkLog query = new WmsDeviceCardWorkLog();
                    query.setCardId(lanyaDeviceCardSenderLog.getCardId());
                    query.setRealName(lanyaDeviceCardSenderLog.getRealName());

                    List<WmsDeviceCardWorkLog> wmsDeviceCardWorkLogs = wmsDeviceCardWorkLogService.selectWmsDeviceCardWorkLogListEnd(query);
                    if (wmsDeviceCardWorkLogs.isEmpty()) {
                        log.error("-------------------------\r\nERROR:没有找到对应发卡记录>\r\n{}\r\n-------------------------", query);
                    }

                    if (wmsDeviceCardWorkLogs.size() > 1) {
                        log.error("---------------------------\r\nERROR:找到多个发卡记录>\r\n{}\r\n---------------------------", query);
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

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            Date begin = workLog.getSenderCommandTime();
                            Date end = workLog.getReturnCommandTime();
                            Long personId = workLog.getPersonId();
                            calendar.setTime(begin);

                            List<Map<String, Object>> trajectoryPoints = new ArrayList<>();
                            while (!calendar.getTime().after(end)) {
                                String tableName = "position_history_" + sdf.format(calendar.getTime());
                                if (lanyaPositionHistoryService.checkTableExists(tableName) > 0) {
                                    List<LanyaPositionHistory> trs = lanyaPositionHistoryService.selectLanyaPositionHistoryListByTableTimeRange(begin, end, personId, tableName);
                                    int step = trs.size() / 3000;
                                    for (int i = 0; i < trs.size(); i += step) {
                                        trajectoryPoints.add(getStringObjectMap(trs, i));
                                    }
                                }
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }

                            wmsTrajectory.setTrajectoryPoints(JSON.toJSONString(trajectoryPoints));

                            // 保存轨迹
                            wmsTrajectoryService.insertWmsTrajectory(wmsTrajectory);

                            // 移除workLog
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

    private WmsDeviceCardWorkLog parseWorkLog(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog) {
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
        return wordLog;
    }

    private void saveWorkLog(WmsDeviceCardWorkLog workLog) {
        if (wmsDeviceCardWorkLogService.selectWmsDeviceCardWorkLogCountBySenderLanyaLogId(workLog.getSenderLanyaLogId()) == 0) {
            if (wmsDeviceCardWorkLogService.insertWmsDeviceCardWorkLog(workLog) > 0) {
                initPosition(workLog);
                workActivitiesByCardId.put(workLog.getCardId(), workLog);
            }
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

    public void processPosition(LanyaPositionHistory position, String[] testAreaName) {
        WmsDeviceCardWorkLog workLog = workActivitiesByCardId.get(position.getCardId());
        if (workLog == null) {
            // 从wms_device_card_work_log表获取工作记录
            WmsDeviceCardWorkLog query = new WmsDeviceCardWorkLog();
            query.setCardId(position.getCardId());
            // 根据定位卡ID查找没有还卡的工作记录
            List<WmsDeviceCardWorkLog> wmsDeviceCardWorkLogs = wmsDeviceCardWorkLogService.selectWmsDeviceCardWorkLogListEnd(query);
            if (wmsDeviceCardWorkLogs.isEmpty()) {
                // 从lanya的device_card_sender_log表查询开进路
                LanyaDeviceCardSenderLog deviceCardSenderLog = lanyaDeviceCardSenderLogService.selectLanyaDeviceCardSenderLogByLastCardId(position.getCardId());
                if (deviceCardSenderLog != null && deviceCardSenderLog.getCardSenderType() != null && deviceCardSenderLog.getCardSenderType() == 1) {
                    // 如果定位卡ID最后一条是开记录则重建工作记录
                    workLog = parseWorkLog(deviceCardSenderLog);
                    saveWorkLog(workLog);
                } else {
                    // 如果仍然没有则放弃此条数据
                    return;
                }
            } else {
                workLog = wmsDeviceCardWorkLogs.get(0);
            }
        }
        // 检测是否触发告警规则
        List<AlarmResult> alarmRules = alarmDetection.detect(position, "face".equals(workLog.getIdentifyType()) ? "内部员工" : "外部访客", testAreaName);


        // 如果有告警规则触发则在workActivities查询匹配的wms_device_card_work_log记录ID
        if (!alarmRules.isEmpty()) {
            // wms_device_card_work_log记录ID
            Long workId = position.getPersonId().equals(workLog.getPersonId()) ? workLog.getId() : null;

            // 如果workActivities中没有匹配的wms_device_card_work_log记录ID则到数据库中查找
            if (workId == null) {
                workId = wmsDeviceCardWorkLogService.selectWmsDeviceCardWorkLogIdInTimeByCardAndName(position.getAcceptTime(), position.getCardId(), position.getRealName());
            }

            if (workId == null) {
                log.error("未找到对应的设备卡记录");
            }

            Date alarmTime = position.getAcceptTime();
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

                String content = String.format("【大庆油田有限责任公司】(物资公司)[%s]在%s时触发了%s报警，请尽快处理！", position.getPersonId(), sdfChineseDateTime.format(alarmTime), wmsAlarmRule.rule.getAlarmRuleName());

                // 通知短信接收人员
                for (String smsUser : wmsAlarmRule.rule.getSmsNoticeUsers().split(",")) {
                    Long UserId = Long.parseLong(smsUser);
                    if (!userCache.containsKey(UserId)) {
                        userCache.put(UserId, userService.selectUserById(UserId));
                    }
                    String uniqueId = wmsAlarmRule.rule.getAlarmRuleName() + "@" + UserId;

                    // 忽略重复
                    if (discoveredSmsAlarm.contains(uniqueId)) {
                        continue;
                    }

                    discoveredSmsAlarm.add(uniqueId);
                    SysUser sysUser = userCache.get(UserId);
                    Map<String, Object> sms = new HashMap<String, Object>() {{
                        put("mobile", sysUser.getPhonenumber());
                        put("content", content);
                        put("create_time", alarmTime);
                        put("taskId", alarmTime.getTime());
                    }};
                    SpringUtils.getBean(SysJobLogMapper.class).insertSmsCat(sms);
                }

                // 通知即时通接收人员
                for (String smsUser : wmsAlarmRule.rule.getImNoticeUsers().split(",")) {
                    Long UserId = Long.parseLong(smsUser);
                    if (!userCache.containsKey(UserId)) {
                        userCache.put(UserId, userService.selectUserById(UserId));
                    }

                    String uniqueId = wmsAlarmRule.rule.getAlarmRuleName() + "@" + UserId;

                    // 忽略重复
                    if (discoveredImAlarm.contains(uniqueId)) {
                        continue;
                    }

                    discoveredImAlarm.add(uniqueId);

                    SysUser sysUser = userCache.get(UserId);
                    Map<String, Object> sms = new HashMap<String, Object>() {{
                        put("user", sysUser.getJstWorkNumber());
                        put("content", content);
                        put("create_time", alarmTime);
                    }};
                    SpringUtils.getBean(SysJobLogMapper.class).insertSmsJst(sms);
                }

                // 通知站内通知接收人员
                for (String smsUser : wmsAlarmRule.rule.getSysNoticeUsers().split(",")) {
                    Long UserId = Long.parseLong(smsUser);

                    String uniqueId = wmsAlarmRule.rule.getAlarmRuleName() + "@" + UserId;

                    // 忽略重复
                    if (discoveredSysAlarm.contains(uniqueId)) {
                        continue;
                    }

                    discoveredSysAlarm.add(uniqueId);

                    if (!userCache.containsKey(UserId)) {
                        userCache.put(UserId, userService.selectUserById(UserId));
                    }
                    SysUser sysUser = userCache.get(UserId);
                    siteNoticeService.noticeTo(sysUser, content);
                }
            }
        }

        // 添加轨迹给卡工作日志
        workLog.getTrajectory().add(position);  //经度
    }

    public void PositionSync(String tableName) throws ParseException {
        if (!enablePositionTableSync) {
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

            // 如果目标表不存在则直接放弃本次同步
            if (!lanyaPositionHistoryService.showPositionHistoryTableNames().contains(tableName)) {
                break;
            }

            // 获取count条position_history数据
            List<LanyaPositionHistory> lanyaPositionHistories = lanyaPositionHistoryService.selectLanyaPositionHistoryListStartTime(positionOffset, countPerFetch, tableName);

            // 循环处理数据
            for (LanyaPositionHistory position : lanyaPositionHistories) {
                processPosition(position, null);
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
