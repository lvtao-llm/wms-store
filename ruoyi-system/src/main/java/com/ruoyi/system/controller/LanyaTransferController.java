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
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
    public Object positionPersonRealtime(LanyaCoreAlarm lanyaCoreAlarm) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        return thirdPartyAuth.callThirdParty("/api-service/position/currentList", HttpMethod.POST, body);
    }

    /**
     * 获取人员历史定位
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/position-person-history")
    public Object positionPersonHistory(LanyaCoreAlarm lanyaCoreAlarm) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        return thirdPartyAuth.callThirdParty("/api-service/position/historyPosition", HttpMethod.POST, body);
    }

    /**
     * 获取人员最终位置
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/position-person-finally")
    public Object positionPersonFinally(LanyaCoreAlarm lanyaCoreAlarm) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        return thirdPartyAuth.callThirdParty("/api-service/position/currentList", HttpMethod.POST, body);
    }

    /**
     * 获取字典数据
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/dict/findData")
    public Object dictFindData() throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        return thirdPartyAuth.callThirdParty("/system/dict/findData", HttpMethod.POST, body);
    }

    /**
     * 获取设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/machine/listPage")
    public Object machineListPage() throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("pageNum", 1);
        body.put("total", 0);
        body.put("pageFlag", "Y");
        return thirdPartyAuth.callThirdParty("/monitor-service/cardSender/listPage", HttpMethod.POST, body);
    }

    /**
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/machine/detailPage")
    public Object machineListDetail(@Param("deviceSn") String deviceSn) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("deviceSn", deviceSn);
        return thirdPartyAuth.callThirdParty("/monitor-service/cardSender/getListDetail", HttpMethod.POST, body);
    }

    /**
     * 内部员工与机器绑定
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/machine/config-relation/detail-by-person")
    public Object machineConfigRelationDetailByPerson(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/monitor-service/cardSenderConfigRelation/detailByPerson", HttpMethod.POST, body);
    }

    /**
     * 添加员工与设备绑定关系
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/machine/config-relation/edit-by-person")
    public Object machineConfigRelationEditByPerson(@RequestBody JSONObject body) throws JsonProcessingException {
        Object obj = thirdPartyAuth.callThirdParty("/monitor-service/cardSenderConfigRelation/editByPerson", HttpMethod.POST, body);
        return obj;
    }

    /**
     * 获取内部员工列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/person/staff/listPage")
    public Object personStaffListPage(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/person/staff/listPage", HttpMethod.POST, body);
    }

    /**
     * 添加内部员工
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/person/staff/addStaff")
    public Object personStaffAddStaff(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/person/staff/addStaff", HttpMethod.POST, body);
    }

    /**
     * 修改内部员工
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/person/staff/updateStaff")
    public Object personStaffUpdateStaff(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/person/staff/updateStaff", HttpMethod.POST, body);
    }

    /**
     * 删除内部员工
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/person/staff/delStaff")
    public Object personStaffDelStaff(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/person/staff/delStaff", HttpMethod.POST, body);
    }

    /**
     * 获取设备卡列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/device/card/listPage")
    public Object deviceCardListPage(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/device/card/listPage", HttpMethod.POST, body);
    }

    /**
     * 添加设备卡
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/device/card/addCard")
    public Object deviceCardAddCard(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/device/card/addCard", HttpMethod.POST, body);
    }

    /**
     * 修改设备卡
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/device/card/updateCard")
    public Object deviceCardUpdateCard(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/device/card/updateCard", HttpMethod.POST, body);
    }

    /**
     * 删除设备卡
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/device/card/delCard")
    public Object deviceCardDelCard(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/device/card/delCard", HttpMethod.POST, body);
    }

    /**
     * 人员发卡记录
     *
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/cardSender/cardSenderLog")
    public Object cardSenderLog(@RequestBody JSONObject body) throws JsonProcessingException {
        HashMap<?, ?> res = (HashMap<?, ?>) thirdPartyAuth.callThirdParty("/monitor-service/cardSender/cardSenderLog", HttpMethod.POST, body);
        ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) res.get("data");

        HashMap<Integer, HashMap<String, Object>> sending = new HashMap<>();
        List<HashMap<String, Object>> complete = new ArrayList<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            HashMap<String, Object> obj = data.get(i);

            // 过滤掉没有人员关联和发卡中的记录
            if (obj.get("personId") == null || obj.get("realName") == null || ((Integer) obj.get("cardSenderType")) == 3) {
                continue;
            }

            if (((Integer) obj.get("cardSenderType")) == 0) {
                // cardSenderType 0:还卡记录
                if (sending.containsKey((Integer) obj.get("cardId"))) {
                    HashMap<String, Object> send = sending.remove(obj.get("cardId"));
                    send.put("returnCardTime", obj.get("createTime"));
                    send.put("returnCardId", obj.get("id"));
                    complete.add(0, send);
                } else {
                    obj.put("returnCardTime", obj.get("createTime"));
                    obj.put("return_card_id", obj.get("id"));
                    obj.put("sendCardTime", null);
                    obj.put("sendCardId", null);
                    complete.add(0, obj);
                }
            }

            if (((Integer) obj.get("cardSenderType")) == 1) {
                // cardSenderType 1:发卡记录
                if (sending.containsKey((Integer) obj.get("cardId"))) {
                    // 如果有发卡记录有可能是卡机那头的错误（不好理解卡机的记录）导致的重复发卡
                    continue;
                } else {
                    obj.put("returnCardTime", null);
                    obj.put("returnCardId", null);
                    obj.put("sendCardTime", obj.get("createTime"));
                    obj.put("sendCardId", obj.get("id"));
                    sending.put((Integer) obj.get("cardId"), obj);
                }
            }
        }
        return AjaxResult.success(complete);
    }

    /**
     * 获取图片
     */
    @GetMapping("/files/image")
    public void filesStatic(String path, HttpServletResponse response) throws IOException {
        String target = "http://" + thirdPartyAuth.baseUrl + "/fileStatic/" + path;
        byte[] bytes = thirdPartyAuth.restTemplate.execute(
                target,
                HttpMethod.GET,
                null,
                clientHttpResponse -> StreamUtils.copyToByteArray(clientHttpResponse.getBody())
        );
        response.reset();
        response.setContentType("image/jpeg");
        response.setContentLength(bytes.length);
        StreamUtils.copy(bytes, response.getOutputStream());
    }

    /**
     * 文件上传
     */
    @PostMapping("/files/upload")
    public Object filesUpload(String module, Boolean compress, @RequestParam("files") MultipartFile[] files) throws IOException {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("module", module);
        body.add("compress", compress);

        // 2. 把 MultipartFile 转成 Resource（关键）
        for (MultipartFile file : files) {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();   // 必须给文件名
                }
            };
            body.add("files", resource);   // 与接收方字段名保持一致
        }

        // 1. 组装 body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        return thirdPartyAuth.callThirdParty("/file/files/upload", HttpMethod.POST, entity);
    }


    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/position/historyPosition/findPersonHistoryList")
    public Object positionHistoryPositionFindPersonHistoryList(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/position/historyPosition/findPersonHistoryList", HttpMethod.POST, body);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/system/risk/listPage")
    public Object systemRiskListPage(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/system/risk/listPage", HttpMethod.POST, body);
    }

    /**
     * 获取风险信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/system/risk/findRisk")
    public Object systemRiskFindRisk(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/system/risk/findRisk", HttpMethod.POST, body);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/system/risk/addRisk")
    public Object systemRiskAddRisk(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/system/risk/addRisk", HttpMethod.POST, body);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/system/risk/updateRisk")
    public Object systemRiskUpdateRisk(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/system/risk/updateRisk", HttpMethod.POST, body);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/system/risk/delRisk")
    public Object systemRiskDelRisk(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/system/risk/delRisk", HttpMethod.POST, body);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/device/layer/listLayer")
    public Object deviceLayerListLayer(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/device/layer/listLayer", HttpMethod.POST, body);
    }

    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/device/beacon/preview")
    public Object deviceBeaconPreview(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/device/beacon/preview", HttpMethod.POST, body);
    }

    /**
     * 获取今日位置统计
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/statistics/personStatistics/todayPerson")
    public Object statisticsPersonStatisticsTodayPerson(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/statistics/personStatistics/todayPerson", HttpMethod.POST, body);
    }

    /**
     * 获取当前月位置统计
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/statistics/personStatistics/monthPerson")
    public Object statisticsPersonStatisticsMonthPerson(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/statistics/personStatistics/monthPerson", HttpMethod.POST, body);
    }

    /**
     *
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/statistics/personStatistics/staffStatistics")
    public Object statisticsPersonStatisticsStaffStatistics(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/statistics/personStatistics/staffStatistics", HttpMethod.POST, body);
    }

    /**
     * 获取当前位置统计
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @PostMapping("/statistics/positionStatistics/currentStatistics")
    public Object statisticsPositionStatisticsCurrentStatistics(@RequestBody JSONObject body) throws JsonProcessingException {
        return thirdPartyAuth.callThirdParty("/statistics/positionStatistics/currentStatistics", HttpMethod.POST, body);
    }
}
