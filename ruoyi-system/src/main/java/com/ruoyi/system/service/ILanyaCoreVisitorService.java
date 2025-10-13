package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaCoreVisitor;

/**
 * 访客记录Service接口
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public interface ILanyaCoreVisitorService 
{
    /**
     * 查询访客记录
     * 
     * @param visitorId 访客记录主键
     * @return 访客记录
     */
    public LanyaCoreVisitor selectLanyaCoreVisitorByVisitorId(Long visitorId);

    /**
     * 查询访客记录列表
     * 
     * @param lanyaCoreVisitor 访客记录
     * @return 访客记录集合
     */
    public List<LanyaCoreVisitor> selectLanyaCoreVisitorList(LanyaCoreVisitor lanyaCoreVisitor);

    /**
     * 新增访客记录
     * 
     * @param lanyaCoreVisitor 访客记录
     * @return 结果
     */
    public int insertLanyaCoreVisitor(LanyaCoreVisitor lanyaCoreVisitor);

    /**
     * 修改访客记录
     * 
     * @param lanyaCoreVisitor 访客记录
     * @return 结果
     */
    public int updateLanyaCoreVisitor(LanyaCoreVisitor lanyaCoreVisitor);

    /**
     * 批量删除访客记录
     * 
     * @param visitorIds 需要删除的访客记录主键集合
     * @return 结果
     */
    public int deleteLanyaCoreVisitorByVisitorIds(Long[] visitorIds);

    /**
     * 删除访客记录信息
     * 
     * @param visitorId 访客记录主键
     * @return 结果
     */
    public int deleteLanyaCoreVisitorByVisitorId(Long visitorId);
}
