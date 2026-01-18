package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.WmsAccessLogMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 人员和车辆进出记录Controller
 *
 * @author ruoyi
 * @date 2025-01-10
 */
@Api(value = "人员和车辆进出记录管理", tags = {"系统端", "人员和车辆进出记录管理"})
@RestController
@RequestMapping("/system/access_log")
public class WmsAccessLogController extends BaseController {
    @Autowired
    private WmsAccessLogMapper wmsAccessLogMapper;

    /**
     * 查询人员和车辆进出进路列表
     */
    @ApiOperation("查询人员和车辆进出记录列表")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String jssj_s, @RequestParam(required = false) String jssj_e) {
        startPage();
        List<JSONObject> list = wmsAccessLogMapper.selectWmsAccessLogList(jssj_s, jssj_e);
        return getDataTable(list);
    }
}
