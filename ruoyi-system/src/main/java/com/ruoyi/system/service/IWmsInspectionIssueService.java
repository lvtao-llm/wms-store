package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsInspectionIssue;

/**
 * 巡检问题Service接口
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
public interface IWmsInspectionIssueService 
{
    /**
     * 查询巡检问题
     * 
     * @param id 巡检问题主键
     * @return 巡检问题
     */
    public WmsInspectionIssue selectWmsInspectionIssueById(Long id);

    /**
     * 查询巡检问题列表
     * 
     * @param wmsInspectionIssue 巡检问题
     * @return 巡检问题集合
     */
    public List<WmsInspectionIssue> selectWmsInspectionIssueList(WmsInspectionIssue wmsInspectionIssue);

    /**
     * 新增巡检问题
     * 
     * @param wmsInspectionIssue 巡检问题
     * @return 结果
     */
    public int insertWmsInspectionIssue(WmsInspectionIssue wmsInspectionIssue);

    /**
     * 修改巡检问题
     * 
     * @param wmsInspectionIssue 巡检问题
     * @return 结果
     */
    public int updateWmsInspectionIssue(WmsInspectionIssue wmsInspectionIssue);

    /**
     * 批量删除巡检问题
     * 
     * @param ids 需要删除的巡检问题主键集合
     * @return 结果
     */
    public int deleteWmsInspectionIssueByIds(Long[] ids);

    /**
     * 删除巡检问题信息
     * 
     * @param id 巡检问题主键
     * @return 结果
     */
    public int deleteWmsInspectionIssueById(Long id);
}
