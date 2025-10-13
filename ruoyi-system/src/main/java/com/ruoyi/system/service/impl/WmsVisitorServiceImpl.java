package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsVisitorMapper;
import com.ruoyi.system.domain.WmsVisitor;
import com.ruoyi.system.service.IWmsVisitorService;

/**
 * 访客信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsVisitorServiceImpl implements IWmsVisitorService 
{
    @Autowired
    private WmsVisitorMapper wmsVisitorMapper;

    /**
     * 查询访客信息
     * 
     * @param visitorId 访客信息主键
     * @return 访客信息
     */
    @Override
    public WmsVisitor selectWmsVisitorByVisitorId(Long visitorId)
    {
        return wmsVisitorMapper.selectWmsVisitorByVisitorId(visitorId);
    }

    /**
     * 查询访客信息列表
     * 
     * @param wmsVisitor 访客信息
     * @return 访客信息
     */
    @Override
    public List<WmsVisitor> selectWmsVisitorList(WmsVisitor wmsVisitor)
    {
        return wmsVisitorMapper.selectWmsVisitorList(wmsVisitor);
    }

    /**
     * 新增访客信息
     * 
     * @param wmsVisitor 访客信息
     * @return 结果
     */
    @Override
    public int insertWmsVisitor(WmsVisitor wmsVisitor)
    {
        return wmsVisitorMapper.insertWmsVisitor(wmsVisitor);
    }

    /**
     * 修改访客信息
     * 
     * @param wmsVisitor 访客信息
     * @return 结果
     */
    @Override
    public int updateWmsVisitor(WmsVisitor wmsVisitor)
    {
        return wmsVisitorMapper.updateWmsVisitor(wmsVisitor);
    }

    /**
     * 批量删除访客信息
     * 
     * @param visitorIds 需要删除的访客信息主键
     * @return 结果
     */
    @Override
    public int deleteWmsVisitorByVisitorIds(Long[] visitorIds)
    {
        return wmsVisitorMapper.deleteWmsVisitorByVisitorIds(visitorIds);
    }

    /**
     * 删除访客信息信息
     * 
     * @param visitorId 访客信息主键
     * @return 结果
     */
    @Override
    public int deleteWmsVisitorByVisitorId(Long visitorId)
    {
        return wmsVisitorMapper.deleteWmsVisitorByVisitorId(visitorId);
    }
}
