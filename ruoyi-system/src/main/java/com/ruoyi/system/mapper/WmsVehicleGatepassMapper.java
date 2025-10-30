package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsVehicleGatepass;

/**
 * 车辆预约Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public interface WmsVehicleGatepassMapper 
{
    /**
     * 查询车辆预约
     * 
     * @param gatepassId 车辆预约主键
     * @return 车辆预约
     */
    public WmsVehicleGatepass selectWmsVehicleGatepassByGatepassId(Long gatepassId);

    /**
     * 查询车辆预约列表
     * 
     * @param wmsVehicleGatepass 车辆预约
     * @return 车辆预约集合
     */
    public List<WmsVehicleGatepass> selectWmsVehicleGatepassList(WmsVehicleGatepass wmsVehicleGatepass);

    /**
     * 新增车辆预约
     * 
     * @param wmsVehicleGatepass 车辆预约
     * @return 结果
     */
    public int insertWmsVehicleGatepass(WmsVehicleGatepass wmsVehicleGatepass);

    /**
     * 修改车辆预约
     * 
     * @param wmsVehicleGatepass 车辆预约
     * @return 结果
     */
    public int updateWmsVehicleGatepass(WmsVehicleGatepass wmsVehicleGatepass);

    /**
     * 删除车辆预约
     * 
     * @param gatepassId 车辆预约主键
     * @return 结果
     */
    public int deleteWmsVehicleGatepassByGatepassId(Long gatepassId);

    /**
     * 批量删除车辆预约
     * 
     * @param gatepassIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsVehicleGatepassByGatepassIds(Long[] gatepassIds);
}
