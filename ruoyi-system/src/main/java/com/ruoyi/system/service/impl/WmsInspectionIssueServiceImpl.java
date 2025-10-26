package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsInspectionIssueMapper;
import com.ruoyi.system.domain.WmsInspectionIssue;
import com.ruoyi.system.service.IWmsInspectionIssueService;

/**
 * 巡检问题Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
@Service
public class WmsInspectionIssueServiceImpl implements IWmsInspectionIssueService 
{
    @Autowired
    private WmsInspectionIssueMapper wmsInspectionIssueMapper;

    /**
     * 查询巡检问题
     * 
     * @param id 巡检问题主键
     * @return 巡检问题
     */
    @Override
    public WmsInspectionIssue selectWmsInspectionIssueById(Long id)
    {
        return wmsInspectionIssueMapper.selectWmsInspectionIssueById(id);
    }

    /**
     * 查询巡检问题列表
     * 
     * @param wmsInspectionIssue 巡检问题
     * @return 巡检问题
     */
    @Override
    public List<WmsInspectionIssue> selectWmsInspectionIssueList(WmsInspectionIssue wmsInspectionIssue)
    {
        return wmsInspectionIssueMapper.selectWmsInspectionIssueList(wmsInspectionIssue);
    }

    /**
     * 新增巡检问题
     * 
     * @param wmsInspectionIssue 巡检问题
     * @return 结果
     */
    @Override
    public int insertWmsInspectionIssue(WmsInspectionIssue wmsInspectionIssue)
    {
        wmsInspectionIssue.setCreateTime(DateUtils.getNowDate());
        return wmsInspectionIssueMapper.insertWmsInspectionIssue(wmsInspectionIssue);
    }

    /**
     * 修改巡检问题
     * 
     * @param wmsInspectionIssue 巡检问题
     * @return 结果
     */
    @Override
    public int updateWmsInspectionIssue(WmsInspectionIssue wmsInspectionIssue)
    {
        wmsInspectionIssue.setUpdateTime(DateUtils.getNowDate());
        return wmsInspectionIssueMapper.updateWmsInspectionIssue(wmsInspectionIssue);
    }

    /**
     * 批量删除巡检问题
     * 
     * @param ids 需要删除的巡检问题主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionIssueByIds(Long[] ids)
    {
        return wmsInspectionIssueMapper.deleteWmsInspectionIssueByIds(ids);
    }

    /**
     * 删除巡检问题信息
     * 
     * @param id 巡检问题主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionIssueById(Long id)
    {
        return wmsInspectionIssueMapper.deleteWmsInspectionIssueById(id);
    }
}
