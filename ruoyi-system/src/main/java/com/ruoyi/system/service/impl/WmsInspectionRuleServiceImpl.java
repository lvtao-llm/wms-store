package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsInspectionRuleMapper;
import com.ruoyi.system.domain.WmsInspectionRule;
import com.ruoyi.system.service.IWmsInspectionRuleService;

/**
 * 巡检规则Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-25
 */
@Service
public class WmsInspectionRuleServiceImpl implements IWmsInspectionRuleService {
    @Autowired
    private WmsInspectionRuleMapper wmsInspectionRuleMapper;

    /**
     * 查询巡检规则
     *
     * @param id 巡检规则主键
     * @return 巡检规则
     */
    @Override
    public WmsInspectionRule selectWmsInspectionRuleById(Long id) {
        return wmsInspectionRuleMapper.selectWmsInspectionRuleById(id);
    }

    /**
     * 查询巡检规则列表
     *
     * @param wmsInspectionRule 巡检规则
     * @return 巡检规则
     */
    @Override
    public List<WmsInspectionRule> selectWmsInspectionRuleList(WmsInspectionRule wmsInspectionRule) {
        return wmsInspectionRuleMapper.selectWmsInspectionRuleList(wmsInspectionRule);
    }

    /**
     * 新增巡检规则
     *
     * @param wmsInspectionRule 巡检规则
     * @return 结果
     */
    @Override
    public int insertWmsInspectionRule(WmsInspectionRule wmsInspectionRule) {
        wmsInspectionRule.setCreateTime(DateUtils.getNowDate());
        return wmsInspectionRuleMapper.insertWmsInspectionRule(wmsInspectionRule);
    }

    /**
     * 修改巡检规则
     *
     * @param wmsInspectionRule 巡检规则
     * @return 结果
     */
    @Override
    public int updateWmsInspectionRule(WmsInspectionRule wmsInspectionRule) {
        wmsInspectionRule.setUpdateTime(DateUtils.getNowDate());
        return wmsInspectionRuleMapper.updateWmsInspectionRule(wmsInspectionRule);
    }

    /**
     * 修改巡检规则下次执行时间
     *
     * @param wmsInspectionRule
     * @return
     */
    @Override
    public int updateWmsInspectionRuleNext(WmsInspectionRule wmsInspectionRule) {
        return wmsInspectionRuleMapper.updateWmsInspectionRuleNext(wmsInspectionRule);
    }

    /**
     * 批量删除巡检规则
     *
     * @param ids 需要删除的巡检规则主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionRuleByIds(Long[] ids) {
        return wmsInspectionRuleMapper.deleteWmsInspectionRuleByIds(ids);
    }

    /**
     * 删除巡检规则信息
     *
     * @param id 巡检规则主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionRuleById(Long id) {
        return wmsInspectionRuleMapper.deleteWmsInspectionRuleById(id);
    }
}
