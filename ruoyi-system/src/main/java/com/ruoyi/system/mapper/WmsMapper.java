package com.ruoyi.system.mapper;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsMaterialIn;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 人脸发卡记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-19
 */
public interface WmsMapper {

    @SelectProvider(type = WmsMapper.class, method = "executeSqlProvider")
    List<JSONObject> executeSqlForList(String sql);

    public static String executeSqlProvider(String sql) {
        return sql;
    }
}
