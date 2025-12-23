package com.ruoyi.system.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.service.ILanyaPositionHistoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.WmsPathsDefinetion;
import com.ruoyi.system.service.IWmsPathsDefinetionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 虚拟路径点Controller
 *
 * @author ruoyi
 * @date 2025-12-19
 */
@RestController
@RequestMapping("/system/wms_paths_definetion")
public class WmsPathsDefinetionController extends BaseController {

    SimpleDateFormat ymdSdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private IWmsPathsDefinetionService wmsPathsDefinetionService;

    @Autowired
    private ILanyaPositionHistoryService lanyaPositionHistoryService;

    /**
     * 查询虚拟路径点列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_paths_definetion:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsPathsDefinetion wmsPathsDefinetion) {
        startPage();
        List<WmsPathsDefinetion> list = wmsPathsDefinetionService.selectWmsPathsDefinetionList(wmsPathsDefinetion);
        return getDataTable(list);
    }

    /**
     * 导出虚拟路径点列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_paths_definetion:export')")
    @Log(title = "虚拟路径点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsPathsDefinetion wmsPathsDefinetion) {
        List<WmsPathsDefinetion> list = wmsPathsDefinetionService.selectWmsPathsDefinetionList(wmsPathsDefinetion);
        ExcelUtil<WmsPathsDefinetion> util = new ExcelUtil<WmsPathsDefinetion>(WmsPathsDefinetion.class);
        util.exportExcel(response, list, "虚拟路径点数据");
    }

    /**
     * 获取虚拟路径点详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_paths_definetion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(wmsPathsDefinetionService.selectWmsPathsDefinetionById(id));
    }

    /**
     * 新增虚拟路径点
     */
    @PreAuthorize("@ss.hasPermi('system:wms_paths_definetion:add')")
    @Log(title = "虚拟路径点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JSONObject jsonObject) {
        // 时间参数校验
        String beginTime = jsonObject.getString("beginTime");
        String finishTime = jsonObject.getString("finishTime");
        if (beginTime == null || finishTime == null) {
            return error("时间参数错误");
        }

        Date begin = jsonObject.getObject("beginTime", Date.class);
        Date end = jsonObject.getObject("finishTime", Date.class);

        // 时间参数格式校验
        beginTime = ymdSdf.format(begin);
        finishTime = ymdSdf.format(end);
        if (!beginTime.equals(finishTime)) {
            return error("时间参数错误");
        }

        // 查询位置信息
        List<LanyaPositionHistory> lanyaPositionHistories = lanyaPositionHistoryService.selectLanyaPositionHistoryListByTableTimeRangeCardId(
                begin,
                end,
                jsonObject.getLong("cardId"),
                "position_history_" + beginTime
        );

        // 经纬度参数
        List<String> longitudeList = new ArrayList<>();
        List<String> latitudeList = new ArrayList<>();
        for (LanyaPositionHistory lanyaPositionHistory : lanyaPositionHistories) {
            longitudeList.add(lanyaPositionHistory.getLongitude().toString());
            latitudeList.add(lanyaPositionHistory.getLatitude().toString());
        }

        // 创建虚拟路径点
        WmsPathsDefinetion wmsPathsDefinetion = jsonObject.to(WmsPathsDefinetion.class);
        List<WmsPathsDefinetion> wmsPathsDefinetions = wmsPathsDefinetionService.selectWmsPathsDefinetionList(wmsPathsDefinetion);
        if (!wmsPathsDefinetions.isEmpty()) {
            wmsPathsDefinetion = wmsPathsDefinetions.get(0);
        }
        wmsPathsDefinetion.setPathLongitude(String.join(",", longitudeList));
        wmsPathsDefinetion.setPathLatitude(String.join(",", latitudeList));

        if (!wmsPathsDefinetions.isEmpty()) {
            return toAjax(wmsPathsDefinetionService.updateWmsPathsDefinetion(wmsPathsDefinetion));
        } else {
            return toAjax(wmsPathsDefinetionService.insertWmsPathsDefinetion(wmsPathsDefinetion));
        }
    }

    /**
     * 修改虚拟路径点
     */
    @PreAuthorize("@ss.hasPermi('system:wms_paths_definetion:edit')")
    @Log(title = "虚拟路径点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsPathsDefinetion wmsPathsDefinetion) {
        return toAjax(wmsPathsDefinetionService.updateWmsPathsDefinetion(wmsPathsDefinetion));
    }

    /**
     * 删除虚拟路径点
     */
    @PreAuthorize("@ss.hasPermi('system:wms_paths_definetion:remove')")
    @Log(title = "虚拟路径点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(wmsPathsDefinetionService.deleteWmsPathsDefinetionByIds(ids));
    }
}
