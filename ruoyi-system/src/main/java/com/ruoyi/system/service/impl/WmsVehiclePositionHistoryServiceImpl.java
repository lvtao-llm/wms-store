package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsVehiclePositionHistoryMapper;
import com.ruoyi.system.domain.WmsVehiclePositionHistory;
import com.ruoyi.system.service.IWmsVehiclePositionHistoryService;

/**
 * 车辆路径点历史Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-19
 */
@Service
public class WmsVehiclePositionHistoryServiceImpl implements IWmsVehiclePositionHistoryService 
{
    @Autowired
    private WmsVehiclePositionHistoryMapper wmsVehiclePositionHistoryMapper;

    /**
     * 查询车辆路径点历史
     * 
     * @param id 车辆路径点历史主键
     * @return 车辆路径点历史
     */
    @Override
    public WmsVehiclePositionHistory selectWmsVehiclePositionHistoryById(Long id)
    {
        return wmsVehiclePositionHistoryMapper.selectWmsVehiclePositionHistoryById(id);
    }

    /**
     * 查询车辆路径点历史列表
     * 
     * @param wmsVehiclePositionHistory 车辆路径点历史
     * @return 车辆路径点历史
     */
    @Override
    public List<WmsVehiclePositionHistory> selectWmsVehiclePositionHistoryList(WmsVehiclePositionHistory wmsVehiclePositionHistory)
    {
        return wmsVehiclePositionHistoryMapper.selectWmsVehiclePositionHistoryList(wmsVehiclePositionHistory);
    }

    /**
     * 新增车辆路径点历史
     * 
     * @param wmsVehiclePositionHistory 车辆路径点历史
     * @return 结果
     */
    @Override
    public int insertWmsVehiclePositionHistory(WmsVehiclePositionHistory wmsVehiclePositionHistory)
    {
        return wmsVehiclePositionHistoryMapper.insertWmsVehiclePositionHistory(wmsVehiclePositionHistory);
    }

    /**
     * 修改车辆路径点历史
     * 
     * @param wmsVehiclePositionHistory 车辆路径点历史
     * @return 结果
     */
    @Override
    public int updateWmsVehiclePositionHistory(WmsVehiclePositionHistory wmsVehiclePositionHistory)
    {
        return wmsVehiclePositionHistoryMapper.updateWmsVehiclePositionHistory(wmsVehiclePositionHistory);
    }

    /**
     * 批量删除车辆路径点历史
     * 
     * @param ids 需要删除的车辆路径点历史主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehiclePositionHistoryByIds(Long[] ids)
    {
        return wmsVehiclePositionHistoryMapper.deleteWmsVehiclePositionHistoryByIds(ids);
    }

    /**
     * 删除车辆路径点历史信息
     * 
     * @param id 车辆路径点历史主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehiclePositionHistoryById(Long id)
    {
        return wmsVehiclePositionHistoryMapper.deleteWmsVehiclePositionHistoryById(id);
    }
}
