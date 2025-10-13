package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsTrajectory;

/**
 * 轨迹Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface IWmsTrajectoryService 
{
    /**
     * 查询轨迹
     * 
     * @param trajectoryId 轨迹主键
     * @return 轨迹
     */
    public WmsTrajectory selectWmsTrajectoryByTrajectoryId(Long trajectoryId);

    /**
     * 查询轨迹列表
     * 
     * @param wmsTrajectory 轨迹
     * @return 轨迹集合
     */
    public List<WmsTrajectory> selectWmsTrajectoryList(WmsTrajectory wmsTrajectory);

    /**
     * 新增轨迹
     * 
     * @param wmsTrajectory 轨迹
     * @return 结果
     */
    public int insertWmsTrajectory(WmsTrajectory wmsTrajectory);

    /**
     * 修改轨迹
     * 
     * @param wmsTrajectory 轨迹
     * @return 结果
     */
    public int updateWmsTrajectory(WmsTrajectory wmsTrajectory);

    /**
     * 批量删除轨迹
     * 
     * @param trajectoryIds 需要删除的轨迹主键集合
     * @return 结果
     */
    public int deleteWmsTrajectoryByTrajectoryIds(Long[] trajectoryIds);

    /**
     * 删除轨迹信息
     * 
     * @param trajectoryId 轨迹主键
     * @return 结果
     */
    public int deleteWmsTrajectoryByTrajectoryId(Long trajectoryId);
}
