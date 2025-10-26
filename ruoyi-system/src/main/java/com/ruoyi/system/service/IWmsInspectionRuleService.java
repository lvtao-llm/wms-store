package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsInspectionRule;

/**
 * 巡检规则Service接口
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
public interface IWmsInspectionRuleService 
{
    /**
     * 查询巡检规则
     * 
     * @param id 巡检规则主键
     * @return 巡检规则
     */
    public WmsInspectionRule selectWmsInspectionRuleById(Long id);

    /**
     * 查询巡检规则列表
     * 
     * @param wmsInspectionRule 巡检规则
     * @return 巡检规则集合
     */
    public List<WmsInspectionRule> selectWmsInspectionRuleList(WmsInspectionRule wmsInspectionRule);

    /**
     * 新增巡检规则
     * 
     * @param wmsInspectionRule 巡检规则
     * @return 结果
     */
    public int insertWmsInspectionRule(WmsInspectionRule wmsInspectionRule);

    /**
     * 修改巡检规则
     * 
     * @param wmsInspectionRule 巡检规则
     * @return 结果
     */
    public int updateWmsInspectionRule(WmsInspectionRule wmsInspectionRule);

    int updateWmsInspectionRuleNext(WmsInspectionRule wmsInspectionRule);

    /**
     * 批量删除巡检规则
     * 
     * @param ids 需要删除的巡检规则主键集合
     * @return 结果
     */
    public int deleteWmsInspectionRuleByIds(Long[] ids);

    /**
     * 删除巡检规则信息
     * 
     * @param id 巡检规则主键
     * @return 结果
     */
    public int deleteWmsInspectionRuleById(Long id);
}
