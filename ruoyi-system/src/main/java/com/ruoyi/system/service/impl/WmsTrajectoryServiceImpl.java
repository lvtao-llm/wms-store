package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsTrajectoryMapper;
import com.ruoyi.system.domain.WmsTrajectory;
import com.ruoyi.system.service.IWmsTrajectoryService;

/**
 * 轨迹Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsTrajectoryServiceImpl implements IWmsTrajectoryService 
{
    @Autowired
    private WmsTrajectoryMapper wmsTrajectoryMapper;

    /**
     * 查询轨迹
     * 
     * @param trajectoryId 轨迹主键
     * @return 轨迹
     */
    @Override
    public WmsTrajectory selectWmsTrajectoryByTrajectoryId(Long trajectoryId)
    {
        return wmsTrajectoryMapper.selectWmsTrajectoryByTrajectoryId(trajectoryId);
    }

    /**
     * 查询轨迹列表
     * 
     * @param wmsTrajectory 轨迹
     * @return 轨迹
     */
    @Override
    public List<WmsTrajectory> selectWmsTrajectoryList(WmsTrajectory wmsTrajectory)
    {
        return wmsTrajectoryMapper.selectWmsTrajectoryList(wmsTrajectory);
    }

    /**
     * 新增轨迹
     * 
     * @param wmsTrajectory 轨迹
     * @return 结果
     */
    @Override
    public int insertWmsTrajectory(WmsTrajectory wmsTrajectory)
    {
        return wmsTrajectoryMapper.insertWmsTrajectory(wmsTrajectory);
    }

    /**
     * 修改轨迹
     * 
     * @param wmsTrajectory 轨迹
     * @return 结果
     */
    @Override
    public int updateWmsTrajectory(WmsTrajectory wmsTrajectory)
    {
        return wmsTrajectoryMapper.updateWmsTrajectory(wmsTrajectory);
    }

    /**
     * 批量删除轨迹
     * 
     * @param trajectoryIds 需要删除的轨迹主键
     * @return 结果
     */
    @Override
    public int deleteWmsTrajectoryByTrajectoryIds(Long[] trajectoryIds)
    {
        return wmsTrajectoryMapper.deleteWmsTrajectoryByTrajectoryIds(trajectoryIds);
    }

    /**
     * 删除轨迹信息
     * 
     * @param trajectoryId 轨迹主键
     * @return 结果
     */
    @Override
    public int deleteWmsTrajectoryByTrajectoryId(Long trajectoryId)
    {
        return wmsTrajectoryMapper.deleteWmsTrajectoryByTrajectoryId(trajectoryId);
    }
}
