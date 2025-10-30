package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsVehicleGatepassMapper;
import com.ruoyi.system.domain.WmsVehicleGatepass;
import com.ruoyi.system.service.IWmsVehicleGatepassService;

/**
 * 车辆预约Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@Service
public class WmsVehicleGatepassServiceImpl implements IWmsVehicleGatepassService 
{
    @Autowired
    private WmsVehicleGatepassMapper wmsVehicleGatepassMapper;

    /**
     * 查询车辆预约
     * 
     * @param gatepassId 车辆预约主键
     * @return 车辆预约
     */
    @Override
    public WmsVehicleGatepass selectWmsVehicleGatepassByGatepassId(Long gatepassId)
    {
        return wmsVehicleGatepassMapper.selectWmsVehicleGatepassByGatepassId(gatepassId);
    }

    /**
     * 查询车辆预约列表
     * 
     * @param wmsVehicleGatepass 车辆预约
     * @return 车辆预约
     */
    @Override
    public List<WmsVehicleGatepass> selectWmsVehicleGatepassList(WmsVehicleGatepass wmsVehicleGatepass)
    {
        return wmsVehicleGatepassMapper.selectWmsVehicleGatepassList(wmsVehicleGatepass);
    }

    /**
     * 新增车辆预约
     * 
     * @param wmsVehicleGatepass 车辆预约
     * @return 结果
     */
    @Override
    public int insertWmsVehicleGatepass(WmsVehicleGatepass wmsVehicleGatepass)
    {
        wmsVehicleGatepass.setCreateTime(DateUtils.getNowDate());
        return wmsVehicleGatepassMapper.insertWmsVehicleGatepass(wmsVehicleGatepass);
    }

    /**
     * 修改车辆预约
     * 
     * @param wmsVehicleGatepass 车辆预约
     * @return 结果
     */
    @Override
    public int updateWmsVehicleGatepass(WmsVehicleGatepass wmsVehicleGatepass)
    {
        return wmsVehicleGatepassMapper.updateWmsVehicleGatepass(wmsVehicleGatepass);
    }

    /**
     * 批量删除车辆预约
     * 
     * @param gatepassIds 需要删除的车辆预约主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleGatepassByGatepassIds(Long[] gatepassIds)
    {
        return wmsVehicleGatepassMapper.deleteWmsVehicleGatepassByGatepassIds(gatepassIds);
    }

    /**
     * 删除车辆预约信息
     * 
     * @param gatepassId 车辆预约主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleGatepassByGatepassId(Long gatepassId)
    {
        return wmsVehicleGatepassMapper.deleteWmsVehicleGatepassByGatepassId(gatepassId);
    }
}
