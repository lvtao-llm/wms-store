package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.WmsAlarm;
import com.ruoyi.system.lanya.data.AlarmDetection;
import com.ruoyi.system.service.IWmsAlarmService;
import com.ruoyi.system.service.IWmsPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报警信息Controller
 *
 * @author ruoyi
 * @date 2025-09-26
 */
@Api(tags = "报警信息管理")
@RestController
@RequestMapping("/system/core/main/screen")
public class WmsCoreMainScreenController extends BaseController {
    @Autowired
    private IWmsAlarmService wmsAlarmService;

    @Autowired
    private IWmsPersonService wmsPersonService;

    @Autowired
    AlarmDetection alarmDetection;

    /**
     * 获取报警信息详细信息
     */
    @ApiOperation("获取报警信息详细信息")
    @GetMapping(value = "/data")
    public AjaxResult getInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("车辆统计", alarmDetection.getLiveVehicleTotal());
        jsonObject.put("人员统计", alarmDetection.getLivePeopleTotal());
        jsonObject.put("今日人员报警", alarmDetection.getPeopleAlarmToday());
        jsonObject.put("今日车辆报警", alarmDetection.getVehicleAlarmToday());
        jsonObject.put("物料统计", new JSONArray());
        jsonObject.put("高风险区域统计", alarmDetection.getHeightRiskAreaTotal());
        return success(jsonObject);
    }
}
