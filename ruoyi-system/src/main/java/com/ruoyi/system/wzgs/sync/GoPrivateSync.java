package com.ruoyi.system.wzgs.sync;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.system.domain.LanyaCoreAlarm;
import com.ruoyi.system.domain.LanyaPositionCurrent;
import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.service.ILanyaPositionCurrentService;
import com.ruoyi.system.service.ILanyaPositionHistoryService;
import com.ruoyi.system.service.impl.LanyaCoreAlarmServiceImpl;
import com.ruoyi.system.utils.HttpUtil;
import com.ruoyi.system.websocket.WebSocketServer;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/14
 */
@Component
public class GoPrivateSync {

    private static final Logger log = LoggerFactory.getLogger("sync-go-private");

    /**
     * httpUtil
     */
    @Autowired
    HttpUtil httpUtil;

    /**
     * 告警信息服务
     */
    @Autowired
    LanyaCoreAlarmServiceImpl lanyaCoreAlarmService;

    /**
     * 定时同步Lanya定位数据开关
     */
    @Value("${lanya.position.sync-api.enabled:false}")
    private boolean positionHistorySyncEnabled;

    /**
     * 定时同步Lanya定位数据API地址
     */
    @Value("${lanya.position.sync-api.api-url:http://112.98.110.101:10030/system/lanya_position_history/new}")
    private String positionHistorySyncUrl;

    /**
     * 定时同步Lanya定位数据开关
     */
    @Value("${lanya.position-current.sync-api.enabled:false}")
    private boolean positionCurrentSyncEnabled;

    /**
     * 定时同步Lanya定位数据API地址
     */
    @Value("${lanya.position-current.sync-api.api-url:http://112.98.110.101:10030/system/position_current/list}")
    private String positionCurrentSyncUrl;

    /**
     * 定时同步Lanya报警数据开关
     */
    @Value("${lanya.alarm.sync-api.enabled:false}")
    private boolean sosSyncEnabled;

    /**
     * 定时同步Lanya定位数据API地址
     */
    @Value("${lanya.alarm.sync-api.api-url:http://112.98.110.101:10030/system/lanya_core_alarm/new-sos}")
    private String sosSyncUrl;

    /**
     * JSON对象转换器
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 时间格式
     */
    SimpleDateFormat sdfTableSuffix = new SimpleDateFormat("yyyyMMdd");

    /**
     * Lanya定位数据数据服务
     */
    @Autowired
    private ILanyaPositionHistoryService lanyaPositionHistoryService;

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    private ILanyaPositionCurrentService lanyaPositionCurrentService;

    @PostConstruct
    public void init() {
        log.info("同步凯德信定位卡数据到物资公司开关：{}", positionHistorySyncEnabled);
        log.info("同步凯德信定位卡数据到物资公司数据请求URL:{}", positionHistorySyncUrl);
        log.info("同步凯德信定位卡SOS报警数据到物资公司开关：{}", sosSyncEnabled);
        log.info("同步凯德信定位卡SOS报警数据到物资公司数据请求URL:{}", sosSyncUrl);
        log.info("同步凯德信定位卡实时数据到物资公司开关：{}", positionCurrentSyncEnabled);
        log.info("同步凯德信定位卡实时数据到物资公司数据请求URL:{}", positionCurrentSyncUrl);
    }

    public void CoreAlarmSync() throws ParseException, IOException {
        if (!sosSyncEnabled) {
            return;
        }

        try (CloseableHttpResponse response = httpUtil.executeGet(this.sosSyncUrl)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JSONObject object = this.mapper.readValue(content, JSONObject.class);
                if (object != null && object.containsKey("rows")) {
                    for (int i = 0; i < object.getJSONArray("rows").size(); i++) {
                        JSONObject row = object.getJSONArray("rows").getJSONObject(i);
                        LanyaCoreAlarm position = row.toJavaObject(LanyaCoreAlarm.class);
                        lanyaCoreAlarmService.insertLanyaCoreAlarm(position);
                    }
                }
            } else {
                throw new RuntimeException("HTTP Get请求失败: " + statusCode + "[ " + this.sosSyncUrl + " ]");
            }
        }
        log.info("凯德信定位卡SOS报警数据到物资公司同步完成");
    }

    public void PositionHistorySync() throws ParseException, IOException {
        if (!positionHistorySyncEnabled) {
            return;
        }

        try (CloseableHttpResponse response = httpUtil.executeGet(this.positionHistorySyncUrl)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JSONObject object = this.mapper.readValue(content, JSONObject.class);
                for (int i = 0; i < object.getJSONArray("rows").size(); i++) {
                    JSONObject row = object.getJSONArray("rows").getJSONObject(i);
                    LanyaPositionHistory position = row.toJavaObject(LanyaPositionHistory.class);
                    String tableName = "position_history_" + sdfTableSuffix.format(position.getCreateTime());
                    lanyaPositionHistoryService.createTable(tableName);
                    position.setAcceptTime(position.getCreateTime());
                    lanyaPositionHistoryService.insertLanyaPositionHistory(position, tableName);
                }
            } else {
                throw new RuntimeException("HTTP Get请求失败: " + statusCode + "[ " + this.positionHistorySyncUrl + " ]");
            }
        } catch (Exception e) {
            log.error("同步凯德信定位卡历史数据到物资公司发生错误", e);
        }
        log.info("凯德信定位卡历史数据到物资公司同步完成");
    }

    public void PositionRealtimeSync() {
        if (!positionCurrentSyncEnabled) {
            return;
        }
        try (CloseableHttpResponse response = httpUtil.executeGet(this.positionCurrentSyncUrl)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                lanyaPositionCurrentService.deleteLanyaPositionCurrentAll();
                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                JSONObject object = this.mapper.readValue(content, JSONObject.class);
                for (int i = 0; i < object.getJSONArray("rows").size(); i++) {
                    JSONObject row = object.getJSONArray("rows").getJSONObject(i);
                    LanyaPositionCurrent position = row.toJavaObject(LanyaPositionCurrent.class);
//                    if (i == 0) {
//                        position.setRealName("黑EPA888");
//                    }
                    lanyaPositionCurrentService.insertLanyaPositionCurrent(position);
                }
            } else {
                throw new RuntimeException("HTTP Get请求失败: " + statusCode + "[ " + this.positionHistorySyncUrl + " ]");
            }
        } catch (Exception e) {
            log.error("同步凯德信定位卡实时数据到物资公司发生错误", e);
        }
        log.info("凯德信定位卡实时数据到物资公司同步完成");
    }
}
