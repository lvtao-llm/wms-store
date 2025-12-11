package com.ruoyi.system.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.utils.ThirdPartyAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
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
import com.ruoyi.system.domain.WmsCardContentSend;
import com.ruoyi.system.service.IWmsCardContentSendService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 发送内容给卡播报Controller
 *
 * @author ruoyi
 * @date 2025-11-10
 */
@Api(tags = "发送内容给卡播报Controller")
@RestController
@RequestMapping("/system/wms_card_content_send")
public class WmsCardContentSendController extends BaseController {
    @Autowired
    private IWmsCardContentSendService wmsCardContentSendService;

    @Autowired
    private LanyaTransferController lanyaTransferController;

    @Autowired
    private ThirdPartyAuth thirdPartyAuth;

    /**
     * 查询发送内容给卡播报列表
     */
    @ApiOperation("获取发送内容给卡播报列表")
    @PreAuthorize("@ss.hasPermi('system:wms_card_content_send:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsCardContentSend wmsCardContentSend) {
        startPage();
        List<WmsCardContentSend> list = wmsCardContentSendService.selectWmsCardContentSendList(wmsCardContentSend);
        return getDataTable(list);
    }

    /**
     * 导出发送内容给卡播报列表
     */
    @ApiOperation("导出发送内容给卡播报列表")
    @PreAuthorize("@ss.hasPermi('system:wms_card_content_send:export')")
    @Log(title = "发送内容给卡播报", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsCardContentSend wmsCardContentSend) {
        List<WmsCardContentSend> list = wmsCardContentSendService.selectWmsCardContentSendList(wmsCardContentSend);
        ExcelUtil<WmsCardContentSend> util = new ExcelUtil<WmsCardContentSend>(WmsCardContentSend.class);
        util.exportExcel(response, list, "发送内容给卡播报数据");
    }

    /**
     * 获取发送内容给卡播报详细信息
     */
    @ApiOperation("获取发送内容给卡播报详细信息")
    @PreAuthorize("@ss.hasPermi('system:wms_card_content_send:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(wmsCardContentSendService.selectWmsCardContentSendById(id));
    }

    /**
     * 新增发送内容给卡播报
     */
    @ApiOperation("新增发送内容给卡播报")
    @PreAuthorize("@ss.hasPermi('system:wms_card_content_send:add')")
    @Log(title = "发送内容给卡播报", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsCardContentSend wmsCardContentSend) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(wmsCardContentSend.getCardCode());
        jsonObject.put("cardIds", jsonArray);
        jsonObject.put("content", wmsCardContentSend.getContent());
        CompletableFuture.runAsync(() -> {
            try {
                lanyaTransferController.cardSendContent(jsonObject);
            } catch (Exception e) {
                // 记录异常日志
                logger.error("异步发送卡片内容失败", e);
            }
        });
        return toAjax(wmsCardContentSendService.insertWmsCardContentSend(wmsCardContentSend));
    }

    /**
     * 修改发送内容给卡播报
     */
    @ApiOperation("修改发送内容给卡播报")
    @PreAuthorize("@ss.hasPermi('system:wms_card_content_send:edit')")
    @Log(title = "发送内容给卡播报", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsCardContentSend wmsCardContentSend) {
        return toAjax(wmsCardContentSendService.updateWmsCardContentSend(wmsCardContentSend));
    }

    /**
     * 删除发送内容给卡播报
     */
    @ApiOperation("删除发送内容给卡播报")
    @PreAuthorize("@ss.hasPermi('system:wms_card_content_send:remove')")
    @Log(title = "发送内容给卡播报", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(wmsCardContentSendService.deleteWmsCardContentSendByIds(ids));
    }
}
