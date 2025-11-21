package com.ruoyi.system.wzgs.sync;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.system.domain.LanyaCoreAlarm;
import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.service.ILanyaPositionHistoryService;
import com.ruoyi.system.service.impl.LanyaCoreAlarmServiceImpl;
import com.ruoyi.system.utils.HttpUtil;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    private static final Logger log = LoggerFactory.getLogger("wzgs-sync");

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
    private boolean enablePositionApiSync;

    /**
     * 定时同步Lanya定位数据开关
     */
    @Value("${lanya.alarm.sync-api.enabled:false}")
    private boolean enableAlarmApiSync;

    /**
     * 定时同步Lanya定位数据API地址
     */
    @Value("${lanya.position.sync-api.api-url:http://112.98.110.101:10030/system/lanya_position_history/new}")
    private String apiUrlPosition;

    /**
     * 定时同步Lanya定位数据API地址
     */
    @Value("${lanya.position.sync-api.api-url:http://112.98.110.101:10030/system/lanya_core_alarm/new-sos}")
    private String apiUrlSos;

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

    public void CoreAlarmSync() throws ParseException, IOException {
        if (!enableAlarmApiSync) {
            return;
        }

        try (CloseableHttpResponse response = httpUtil.executeGet(this.apiUrlSos)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.info("CoreAlarmSync获取数据({})成功:{}", this.apiUrlSos, content);
                JSONObject object = this.mapper.readValue(content, JSONObject.class);
                for (int i = 0; i < object.getJSONArray("rows").size(); i++) {
                    JSONObject row = object.getJSONArray("rows").getJSONObject(i);
                    LanyaCoreAlarm position = row.toJavaObject(LanyaCoreAlarm.class);
                    lanyaCoreAlarmService.insertLanyaCoreAlarm(position);
                }
            } else {
                throw new RuntimeException("HTTP Get请求失败: " + statusCode + "[ " + this.apiUrlSos + " ]");
            }
        }
    }

    public void PositionSync() throws ParseException, IOException {
        if (!enablePositionApiSync) {
            return;
        }

        try (CloseableHttpResponse response = httpUtil.executeGet(this.apiUrlPosition)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.info("PositionSync获取数据({})成功:{}", this.apiUrlPosition, content);
                JSONObject object = this.mapper.readValue(content, JSONObject.class);
                for (int i = 0; i < object.getJSONArray("rows").size(); i++) {
                    JSONObject row = object.getJSONArray("rows").getJSONObject(i);
                    LanyaPositionHistory position = row.toJavaObject(LanyaPositionHistory.class);
                    String tableName = "position_history_" + sdfTableSuffix.format(position.getAcceptTime());
                    lanyaPositionHistoryService.createTable(tableName);
                    lanyaPositionHistoryService.insertLanyaPositionHistory(position, tableName);
                }
            } else {
                throw new RuntimeException("HTTP Get请求失败: " + statusCode + "[ " + this.apiUrlPosition + " ]");
            }
        }
    }
}
