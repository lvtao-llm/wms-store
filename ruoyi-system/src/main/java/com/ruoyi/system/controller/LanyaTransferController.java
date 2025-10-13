package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.ThirdPartyAuth;
import com.ruoyi.system.domain.LanyaCoreAlarm;
import com.ruoyi.system.service.ILanyaCoreAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报警记录Controller
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya-transfer")
@DataSource(DataSourceType.SLAVE)
public class LanyaTransferController extends BaseController {

    @Autowired
    private ThirdPartyAuth thirdPartyAuth;

    @Autowired
    private ILanyaCoreAlarmService lanyaCoreAlarmService;

    /**
     * 获取人员实时定位
     */
    @Log(title = "人员实时定位", businessType = BusinessType.OTHER)
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/position-person-realtime")
    public TableDataInfo positionPersonRealtime(LanyaCoreAlarm lanyaCoreAlarm) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        List<?> map = (List<?>) thirdPartyAuth.callThirdParty("/api-service/position/currentList", HttpMethod.POST, body);
        return getDataTable(map);
    }

    /**
     * 获取人员历史定位
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/position-person-history")
    public TableDataInfo positionPersonHistory(LanyaCoreAlarm lanyaCoreAlarm) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        List<?> map = (List<?>) thirdPartyAuth.callThirdParty("/api-service/position/historyPosition", HttpMethod.POST, body);
        return getDataTable(map);
    }

    /**
     * 获取人员最终位置
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/position-person-finally")
    public TableDataInfo positionPersonFinally(LanyaCoreAlarm lanyaCoreAlarm) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        List<?> map = (List<?>) thirdPartyAuth.callThirdParty("/api-service/position/currentList", HttpMethod.POST, body);
        return getDataTable(map);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/dict/findData")
    public AjaxResult dictFindData() throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        Map<?, ?> map = (Map<?, ?>) thirdPartyAuth.callThirdParty("/system/dict/findData", HttpMethod.POST, body);
        return AjaxResult.success(map);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/machine/listPage")
    public AjaxResult machineListPage() throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("pageNum", 1);
        body.put("total", 0);
        body.put("pageFlag", "Y");
        List<?> map = (List<?>) thirdPartyAuth.callThirdParty("/monitor-service/cardSender/listPage", HttpMethod.POST, body);
        return AjaxResult.success(map);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/machine/config-relation/edit-by-person")
    public AjaxResult machineConfigRelationEditByPerson(@RequestBody JSONObject body) throws JsonProcessingException {
        List<?> map = (List<?>) thirdPartyAuth.callThirdParty("/monitor-service/cardSenderConfigRelation/editByPerson", HttpMethod.POST, body);
        return AjaxResult.success(map);
    }
}
