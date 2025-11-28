package com.ruoyi.system.controller;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
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
import com.ruoyi.system.domain.WmsMaterialIdentifyRecord;
import com.ruoyi.system.service.IWmsMaterialIdentifyRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料识别记录Controller
 *
 * @author ruoyi
 * @date 2025-11-21
 */
@RestController
@RequestMapping("/system/wms_material_identify_record")
public class WmsMaterialIdentifyRecordController extends BaseController {
    @Autowired
    private IWmsMaterialIdentifyRecordService wmsMaterialIdentifyRecordService;
    @Value("${wms.wms-material-identify-record-list-day-offset:-1}")
    private int huokou;
    SimpleDateFormat sdfYearMonDay = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 查询物料识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, huokou);
        String start = sdfYearMonDay.format(calendar.getTime()) + " 00:00:00";
        String end = sdfYearMonDay.format(new Date()) + " 23:59:59";

        List<WmsMaterialIdentifyRecord> list = wmsMaterialIdentifyRecordService.selectWmsMaterialIdentifyRecordListByRange(start, end);

        List<JSONObject> jsonArray = new ArrayList<>();
        for (WmsMaterialIdentifyRecord record : list) {
            JSONObject jsonObject = JSONObject.from(record);
            if (jsonObject.containsKey("img1") && jsonObject.getString("img1") != null) {
                String[] images = jsonObject.getString("img1").split(",");
                List<String> imagePaths1 = new ArrayList<>();
                for (int i = 0; i < images.length; i++) {
                    String p = Paths.get(jsonObject.getString("imagePath"), images[i]).toFile().getPath().replace("\\", "/").replace(RuoYiConfig.getProfile() + "/", "");
                    if (i == 0) {
                        imagePaths1.add(p);
                        jsonObject.put("img1Url" + i, p);
                    }
                }
                jsonObject.put("img1", String.join(",", imagePaths1));
            }
            if (jsonObject.containsKey("mg2") && jsonObject.getString("mg2") != null) {
                String[] images = jsonObject.getString("mg2").split(",");
                List<String> imagePaths2 = new ArrayList<>();
                for (int i = 0; i < images.length; i++) {
                    String p = Paths.get(jsonObject.getString("imagePath"), images[i]).toFile().getPath().replace("\\", "/").replace(RuoYiConfig.getProfile() + "/", "");
                    if(i==images.length-1){
                    imagePaths2.add(p);
                    jsonObject.put("img2Url" + i, p);
                }
                    }
                jsonObject.put("mg2", String.join(",", imagePaths2));
            }
            jsonArray.add(jsonObject);
        }
        return getDataTable(jsonArray);
    }

    /**
     * 导出物料识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:export')")
    @Log(title = "物料识别记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord) {
        List<WmsMaterialIdentifyRecord> list = wmsMaterialIdentifyRecordService.selectWmsMaterialIdentifyRecordList(wmsMaterialIdentifyRecord);
        ExcelUtil<WmsMaterialIdentifyRecord> util = new ExcelUtil<WmsMaterialIdentifyRecord>(WmsMaterialIdentifyRecord.class);
        util.exportExcel(response, list, "物料识别记录数据");
    }

    /**
     * 获取物料识别记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:query')")
    @GetMapping(value = "/{ymd}")
    public AjaxResult getInfo(@PathVariable("ymd") String ymd) {
        return success(wmsMaterialIdentifyRecordService.selectWmsMaterialIdentifyRecordByYmd(ymd));
    }

    /**
     * 新增物料识别记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:add')")
    @Log(title = "物料识别记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord) {
        return toAjax(wmsMaterialIdentifyRecordService.insertWmsMaterialIdentifyRecord(wmsMaterialIdentifyRecord));
    }

    /**
     * 修改物料识别记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:edit')")
    @Log(title = "物料识别记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord) {
        return toAjax(wmsMaterialIdentifyRecordService.updateWmsMaterialIdentifyRecord(wmsMaterialIdentifyRecord));
    }

    /**
     * 删除物料识别记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:remove')")
    @Log(title = "物料识别记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ymds}")
    public AjaxResult remove(@PathVariable String[] ymds) {
        return toAjax(wmsMaterialIdentifyRecordService.deleteWmsMaterialIdentifyRecordByYmds(ymds));
    }
}
