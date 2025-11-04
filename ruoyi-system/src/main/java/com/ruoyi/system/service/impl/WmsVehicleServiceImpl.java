package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsVehicleMapper;
import com.ruoyi.system.domain.WmsVehicle;
import com.ruoyi.system.service.IWmsVehicleService;

/**
 * 车辆档案Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@Service
public class WmsVehicleServiceImpl implements IWmsVehicleService 
{
    @Autowired
    private WmsVehicleMapper wmsVehicleMapper;

    /**
     * 查询车辆档案
     * 
     * @param vehicleId 车辆档案主键
     * @return 车辆档案
     */
    @Override
    public WmsVehicle selectWmsVehicleByVehicleId(Long vehicleId)
    {
        return wmsVehicleMapper.selectWmsVehicleByVehicleId(vehicleId);
    }

    /**
     * 查询车辆档案列表
     * 
     * @param wmsVehicle 车辆档案
     * @return 车辆档案
     */
    @Override
    public List<WmsVehicle> selectWmsVehicleList(WmsVehicle wmsVehicle)
    {
        return wmsVehicleMapper.selectWmsVehicleList(wmsVehicle);
    }

    /**
     * 新增车辆档案
     * 
     * @param wmsVehicle 车辆档案
     * @return 结果
     */
    @Override
    public int insertWmsVehicle(WmsVehicle wmsVehicle)
    {
        wmsVehicle.setCreateTime(DateUtils.getNowDate());
        return wmsVehicleMapper.insertWmsVehicle(wmsVehicle);
    }

    /**
     * 修改车辆档案
     * 
     * @param wmsVehicle 车辆档案
     * @return 结果
     */
    @Override
    public int updateWmsVehicle(WmsVehicle wmsVehicle)
    {
        wmsVehicle.setUpdateTime(DateUtils.getNowDate());
        return wmsVehicleMapper.updateWmsVehicle(wmsVehicle);
    }

    /**
     * 批量删除车辆档案
     * 
     * @param vehicleIds 需要删除的车辆档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleByVehicleIds(Long[] vehicleIds)
    {
        return wmsVehicleMapper.deleteWmsVehicleByVehicleIds(vehicleIds);
    }

    /**
     * 删除车辆档案信息
     * 
     * @param vehicleId 车辆档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleByVehicleId(Long vehicleId)
    {
        return wmsVehicleMapper.deleteWmsVehicleByVehicleId(vehicleId);
    }
}
