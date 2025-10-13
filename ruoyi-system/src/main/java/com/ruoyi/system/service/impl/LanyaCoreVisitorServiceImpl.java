package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaCoreVisitorMapper;
import com.ruoyi.system.domain.LanyaCoreVisitor;
import com.ruoyi.system.service.ILanyaCoreVisitorService;

/**
 * 访客记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@Service
public class LanyaCoreVisitorServiceImpl implements ILanyaCoreVisitorService 
{
    @Autowired
    private LanyaCoreVisitorMapper lanyaCoreVisitorMapper;

    /**
     * 查询访客记录
     * 
     * @param visitorId 访客记录主键
     * @return 访客记录
     */
    @Override
    public LanyaCoreVisitor selectLanyaCoreVisitorByVisitorId(Long visitorId)
    {
        return lanyaCoreVisitorMapper.selectLanyaCoreVisitorByVisitorId(visitorId);
    }

    /**
     * 查询访客记录列表
     * 
     * @param lanyaCoreVisitor 访客记录
     * @return 访客记录
     */
    @Override
    public List<LanyaCoreVisitor> selectLanyaCoreVisitorList(LanyaCoreVisitor lanyaCoreVisitor)
    {
        return lanyaCoreVisitorMapper.selectLanyaCoreVisitorList(lanyaCoreVisitor);
    }

    /**
     * 新增访客记录
     * 
     * @param lanyaCoreVisitor 访客记录
     * @return 结果
     */
    @Override
    public int insertLanyaCoreVisitor(LanyaCoreVisitor lanyaCoreVisitor)
    {
        lanyaCoreVisitor.setCreateTime(DateUtils.getNowDate());
        return lanyaCoreVisitorMapper.insertLanyaCoreVisitor(lanyaCoreVisitor);
    }

    /**
     * 修改访客记录
     * 
     * @param lanyaCoreVisitor 访客记录
     * @return 结果
     */
    @Override
    public int updateLanyaCoreVisitor(LanyaCoreVisitor lanyaCoreVisitor)
    {
        lanyaCoreVisitor.setUpdateTime(DateUtils.getNowDate());
        return lanyaCoreVisitorMapper.updateLanyaCoreVisitor(lanyaCoreVisitor);
    }

    /**
     * 批量删除访客记录
     * 
     * @param visitorIds 需要删除的访客记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreVisitorByVisitorIds(Long[] visitorIds)
    {
        return lanyaCoreVisitorMapper.deleteLanyaCoreVisitorByVisitorIds(visitorIds);
    }

    /**
     * 删除访客记录信息
     * 
     * @param visitorId 访客记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreVisitorByVisitorId(Long visitorId)
    {
        return lanyaCoreVisitorMapper.deleteLanyaCoreVisitorByVisitorId(visitorId);
    }
}
