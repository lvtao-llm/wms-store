package com.ruoyi.system.mapper;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.WmsMaterialIn;
import com.ruoyi.system.domain.WmsVisitor;
//import com.ruoyi.system.domain.WzjtViewJlSk;

import java.util.List;

/**
 * 访客信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface WzjtViewJlSkMapper
{
    /**
     * 查询访客信息列表
     * 
     * @param wmsVisitor 访客信息
     * @return 访客信息集合
     */
    @DataSource(value = DataSourceType.STOREHOUSE)
    public List<WmsMaterialIn> selectViewJlSkList(WmsMaterialIn wmsVisitor);


}
