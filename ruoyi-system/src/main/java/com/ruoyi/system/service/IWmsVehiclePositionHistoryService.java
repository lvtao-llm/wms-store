package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsVehiclePositionHistory;

/**
 * 车辆路径点历史Service接口
 * 
 * @author ruoyi
 * @date 2025-12-19
 */
public interface IWmsVehiclePositionHistoryService 
{
    /**
     * 查询车辆路径点历史
     * 
     * @param id 车辆路径点历史主键
     * @return 车辆路径点历史
     */
    public WmsVehiclePositionHistory selectWmsVehiclePositionHistoryById(Long id);

    /**
     * 查询车辆路径点历史列表
     * 
     * @param wmsVehiclePositionHistory 车辆路径点历史
     * @return 车辆路径点历史集合
     */
    public List<WmsVehiclePositionHistory> selectWmsVehiclePositionHistoryList(WmsVehiclePositionHistory wmsVehiclePositionHistory);

    /**
     * 新增车辆路径点历史
     * 
     * @param wmsVehiclePositionHistory 车辆路径点历史
     * @return 结果
     */
    public int insertWmsVehiclePositionHistory(WmsVehiclePositionHistory wmsVehiclePositionHistory);

    /**
     * 修改车辆路径点历史
     * 
     * @param wmsVehiclePositionHistory 车辆路径点历史
     * @return 结果
     */
    public int updateWmsVehiclePositionHistory(WmsVehiclePositionHistory wmsVehiclePositionHistory);

    /**
     * 批量删除车辆路径点历史
     * 
     * @param ids 需要删除的车辆路径点历史主键集合
     * @return 结果
     */
    public int deleteWmsVehiclePositionHistoryByIds(Long[] ids);

    /**
     * 删除车辆路径点历史信息
     * 
     * @param id 车辆路径点历史主键
     * @return 结果
     */
    public int deleteWmsVehiclePositionHistoryById(Long id);
}
