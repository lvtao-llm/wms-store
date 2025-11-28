package com.ruoyi.system.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
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
import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.service.ILanyaPositionHistoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 历史轨迹Controller
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya_position_history")
public class LanyaPositionHistoryController extends BaseController {
    /**
     * 历史轨迹服务
     */
    @Autowired
    private ILanyaPositionHistoryService lanyaPositionHistoryService;

    /**
     * 系统配置服务
     */
    @Autowired
    private ISysConfigService configService;

    /**
     * 时间格式
     */
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 时间格式
     */
    SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
    /**
     * 查询历史轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaPositionHistory lanyaPositionHistory) {
        startPage();
        List<LanyaPositionHistory> list = lanyaPositionHistoryService.selectLanyaPositionHistoryList(lanyaPositionHistory);
        return getDataTable(list);
    }

    /**
     * 导出历史轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:export')")
    @Log(title = "历史轨迹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaPositionHistory lanyaPositionHistory) {
        List<LanyaPositionHistory> list = lanyaPositionHistoryService.selectLanyaPositionHistoryList(lanyaPositionHistory);
        ExcelUtil<LanyaPositionHistory> util = new ExcelUtil<LanyaPositionHistory>(LanyaPositionHistory.class);
        util.exportExcel(response, list, "历史轨迹数据");
    }

    /**
     * 获取历史轨迹详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(lanyaPositionHistoryService.selectLanyaPositionHistoryById(id));
    }
    /**
     * admin
     * Ll112233
     */
    /**
     * 新增历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:add')")
    @Log(title = "历史轨迹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaPositionHistory lanyaPositionHistory) {
        return toAjax(lanyaPositionHistoryService.insertLanyaPositionHistory(lanyaPositionHistory));
    }

    /**
     * 修改历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:edit')")
    @Log(title = "历史轨迹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaPositionHistory lanyaPositionHistory) {
        return toAjax(lanyaPositionHistoryService.updateLanyaPositionHistory(lanyaPositionHistory));
    }

    /**
     * 删除历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:remove')")
    @Log(title = "历史轨迹", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lanyaPositionHistoryService.deleteLanyaPositionHistoryByIds(ids));
    }

    @GetMapping("/new")
    public TableDataInfo newData() throws ParseException {
        SysConfig sysConfig = null;
        List<SysConfig> sysConfigs = configService.selectConfigList(new SysConfig());
        for (SysConfig c : sysConfigs) {
            if (c.getConfigKey().equals("lanya.position.last.offset")) {
                sysConfig = c;
            }
        }

        Date offset = sdfDateTime.parse(sysConfig.getConfigValue());

        // 表名
        String tableName = "position_history_" + sdfYMD.format(offset);
        // 最后同步的position_history表的时间
        List<LanyaPositionHistory> list = lanyaPositionHistoryService.selectLanyaPositionHistoryListStartTime(offset, 100, tableName);

        if (list.isEmpty()) {
            // 将 Date 转换为 LocalDateTime
            LocalDate offsetDate = offset.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // 获取当前时间
            LocalDate currentDate = LocalDate.now();

            // 计算当前日期与offset之间的天数差
            long daysBetween = ChronoUnit.DAYS.between(offsetDate, currentDate);

            if (daysBetween > 0) {
                // 如果offset不是今天，则遍历所有日期
                for (int i = 0; i < daysBetween; i++) {
                    offsetDate = offsetDate.plusDays(1);
                    String ymd = offsetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    tableName = "position_history_" + ymd;
                    offset = sdfDateTime.parse(offsetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00.000");
                    sysConfig.setConfigValue(sdfDateTime.format(offset));
                    configService.updateConfig(sysConfig);
                    int exists = lanyaPositionHistoryService.checkTableExists(tableName);
                    if (exists == 1) {
                        list = lanyaPositionHistoryService.selectLanyaPositionHistoryListStartTime(offset, 1000, tableName);
                        if (!list.isEmpty()) {
                            break;
                        }
                    }
                }
            }
        }

        if (!list.isEmpty()) {
            sysConfig.setConfigValue(sdfDateTime.format(list.get(list.size() - 1).getCreateTime()));
            configService.updateConfig(sysConfig);
        }

        return getDataTable(list);
    }
}
