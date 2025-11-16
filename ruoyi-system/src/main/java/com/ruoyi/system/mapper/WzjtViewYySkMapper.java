package com.ruoyi.system.mapper;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.WmsMaterialStock;
import com.ruoyi.system.domain.WmsVehicleGatepass;

import java.util.List;

/**
 * 物资公司车辆预约Mapper接口
 *
 * @author ruoyi
 * @date 2025-09-26
 */
public interface WzjtViewYySkMapper {
    /**
     * 查询物资公司车辆预约
     *
     * @return 车辆预约列表
     */
    @DataSource(value = DataSourceType.STOREHOUSE)
    public List<WmsVehicleGatepass> selectViewYySkList();
}
