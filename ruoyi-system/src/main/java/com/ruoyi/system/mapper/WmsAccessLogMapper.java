package com.ruoyi.system.mapper;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsAccessLog;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2026/1/18
 */
public interface WmsAccessLogMapper {
    @SelectProvider(type = WmsAccessLogMapper.class, method = "selectWmsAccessLogListProvider")
    public List<JSONObject> selectWmsAccessLogList(String jssjS, String jssjE);

    public static String selectWmsAccessLogListProvider(String jssjS, String jssjE) {
        return "select * from " +
                "( " +
                "select cph as name, '车辆' as log_type, kssj, jssj, '' as person_id, '外部' as identity_type from wms_vehicle_position_history vehicle " +
                "UNION " +
                "select real_name as name, '人员' as log_type, sender_command_time as kssj, return_command_time as jssj, person_id, case when person_photo is null then '外部' else '内部' end identity_type from wms_device_card_work_log person " +
                ") log " +
                "where jssj is not null " +
                (jssjS != null ? "and jssj >= '" + jssjS + "'" : "") + " " +
                (jssjE != null ? "and jssj <= '" + jssjE + "'" : "") + " " +
                "order by jssj DESC";
    }
}
