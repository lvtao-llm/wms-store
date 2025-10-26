package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsInspectionTask;

/**
 * 巡检任务Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
public interface WmsInspectionTaskMapper 
{
    /**
     * 查询巡检任务
     * 
     * @param id 巡检任务主键
     * @return 巡检任务
     */
    public WmsInspectionTask selectWmsInspectionTaskById(Long id);

    /**
     * 查询巡检任务列表
     * 
     * @param wmsInspectionTask 巡检任务
     * @return 巡检任务集合
     */
    public List<WmsInspectionTask> selectWmsInspectionTaskList(WmsInspectionTask wmsInspectionTask);

    /**
     * 新增巡检任务
     * 
     * @param wmsInspectionTask 巡检任务
     * @return 结果
     */
    public int insertWmsInspectionTask(WmsInspectionTask wmsInspectionTask);

    /**
     * 修改巡检任务
     * 
     * @param wmsInspectionTask 巡检任务
     * @return 结果
     */
    public int updateWmsInspectionTask(WmsInspectionTask wmsInspectionTask);

    /**
     * 删除巡检任务
     * 
     * @param id 巡检任务主键
     * @return 结果
     */
    public int deleteWmsInspectionTaskById(Long id);

    /**
     * 批量删除巡检任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsInspectionTaskByIds(Long[] ids);
}
