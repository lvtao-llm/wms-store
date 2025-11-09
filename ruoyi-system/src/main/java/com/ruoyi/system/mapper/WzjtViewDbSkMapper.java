package com.ruoyi.system.mapper;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.WmsMaterialOut;
//import com.ruoyi.system.domain.WzjtViewDbSk;

import java.util.List;

/**
 * 访客信息Mapper接口
 *
 * @author ruoyi
 * @date 2025-09-26
 */
public interface WzjtViewDbSkMapper {
    /**
     * 查询访客信息列表
     *
     * @param wmsVisitor 访客信息
     * @return 访客信息集合
     */
    @DataSource(value = DataSourceType.STOREHOUSE)
    public List<WmsMaterialOut> selectViewDbSkList(WmsMaterialOut wmsVisitor);


}
