package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsInspectionTaskMapper;
import com.ruoyi.system.domain.WmsInspectionTask;
import com.ruoyi.system.service.IWmsInspectionTaskService;

/**
 * 巡检任务Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
@Service
public class WmsInspectionTaskServiceImpl implements IWmsInspectionTaskService 
{
    @Autowired
    private WmsInspectionTaskMapper wmsInspectionTaskMapper;

    /**
     * 查询巡检任务
     * 
     * @param id 巡检任务主键
     * @return 巡检任务
     */
    @Override
    public WmsInspectionTask selectWmsInspectionTaskById(Long id)
    {
        return wmsInspectionTaskMapper.selectWmsInspectionTaskById(id);
    }

    /**
     * 查询巡检任务列表
     * 
     * @param wmsInspectionTask 巡检任务
     * @return 巡检任务
     */
    @Override
    public List<WmsInspectionTask> selectWmsInspectionTaskList(WmsInspectionTask wmsInspectionTask)
    {
        return wmsInspectionTaskMapper.selectWmsInspectionTaskList(wmsInspectionTask);
    }

    /**
     * 新增巡检任务
     * 
     * @param wmsInspectionTask 巡检任务
     * @return 结果
     */
    @Override
    public int insertWmsInspectionTask(WmsInspectionTask wmsInspectionTask)
    {
        wmsInspectionTask.setCreateTime(DateUtils.getNowDate());
        return wmsInspectionTaskMapper.insertWmsInspectionTask(wmsInspectionTask);
    }

    /**
     * 修改巡检任务
     * 
     * @param wmsInspectionTask 巡检任务
     * @return 结果
     */
    @Override
    public int updateWmsInspectionTask(WmsInspectionTask wmsInspectionTask)
    {
        wmsInspectionTask.setUpdateTime(DateUtils.getNowDate());
        return wmsInspectionTaskMapper.updateWmsInspectionTask(wmsInspectionTask);
    }

    /**
     * 批量删除巡检任务
     * 
     * @param ids 需要删除的巡检任务主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionTaskByIds(Long[] ids)
    {
        return wmsInspectionTaskMapper.deleteWmsInspectionTaskByIds(ids);
    }

    /**
     * 删除巡检任务信息
     * 
     * @param id 巡检任务主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionTaskById(Long id)
    {
        return wmsInspectionTaskMapper.deleteWmsInspectionTaskById(id);
    }
}
