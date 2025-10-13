package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsVisitor;

/**
 * 访客信息Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface IWmsVisitorService 
{
    /**
     * 查询访客信息
     * 
     * @param visitorId 访客信息主键
     * @return 访客信息
     */
    public WmsVisitor selectWmsVisitorByVisitorId(Long visitorId);

    /**
     * 查询访客信息列表
     * 
     * @param wmsVisitor 访客信息
     * @return 访客信息集合
     */
    public List<WmsVisitor> selectWmsVisitorList(WmsVisitor wmsVisitor);

    /**
     * 新增访客信息
     * 
     * @param wmsVisitor 访客信息
     * @return 结果
     */
    public int insertWmsVisitor(WmsVisitor wmsVisitor);

    /**
     * 修改访客信息
     * 
     * @param wmsVisitor 访客信息
     * @return 结果
     */
    public int updateWmsVisitor(WmsVisitor wmsVisitor);

    /**
     * 批量删除访客信息
     * 
     * @param visitorIds 需要删除的访客信息主键集合
     * @return 结果
     */
    public int deleteWmsVisitorByVisitorIds(Long[] visitorIds);

    /**
     * 删除访客信息信息
     * 
     * @param visitorId 访客信息主键
     * @return 结果
     */
    public int deleteWmsVisitorByVisitorId(Long visitorId);
}
