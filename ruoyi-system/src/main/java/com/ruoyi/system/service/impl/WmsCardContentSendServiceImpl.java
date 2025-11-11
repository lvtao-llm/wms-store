package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsCardContentSendMapper;
import com.ruoyi.system.domain.WmsCardContentSend;
import com.ruoyi.system.service.IWmsCardContentSendService;

/**
 * 发送内容给卡播报Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-10
 */
@Service
public class WmsCardContentSendServiceImpl implements IWmsCardContentSendService 
{
    @Autowired
    private WmsCardContentSendMapper wmsCardContentSendMapper;

    /**
     * 查询发送内容给卡播报
     * 
     * @param id 发送内容给卡播报主键
     * @return 发送内容给卡播报
     */
    @Override
    public WmsCardContentSend selectWmsCardContentSendById(Long id)
    {
        return wmsCardContentSendMapper.selectWmsCardContentSendById(id);
    }

    /**
     * 查询发送内容给卡播报列表
     * 
     * @param wmsCardContentSend 发送内容给卡播报
     * @return 发送内容给卡播报
     */
    @Override
    public List<WmsCardContentSend> selectWmsCardContentSendList(WmsCardContentSend wmsCardContentSend)
    {
        return wmsCardContentSendMapper.selectWmsCardContentSendList(wmsCardContentSend);
    }

    /**
     * 新增发送内容给卡播报
     * 
     * @param wmsCardContentSend 发送内容给卡播报
     * @return 结果
     */
    @Override
    public int insertWmsCardContentSend(WmsCardContentSend wmsCardContentSend)
    {
        wmsCardContentSend.setCreateTime(DateUtils.getNowDate());
        return wmsCardContentSendMapper.insertWmsCardContentSend(wmsCardContentSend);
    }

    /**
     * 修改发送内容给卡播报
     * 
     * @param wmsCardContentSend 发送内容给卡播报
     * @return 结果
     */
    @Override
    public int updateWmsCardContentSend(WmsCardContentSend wmsCardContentSend)
    {
        return wmsCardContentSendMapper.updateWmsCardContentSend(wmsCardContentSend);
    }

    /**
     * 批量删除发送内容给卡播报
     * 
     * @param ids 需要删除的发送内容给卡播报主键
     * @return 结果
     */
    @Override
    public int deleteWmsCardContentSendByIds(Long[] ids)
    {
        return wmsCardContentSendMapper.deleteWmsCardContentSendByIds(ids);
    }

    /**
     * 删除发送内容给卡播报信息
     * 
     * @param id 发送内容给卡播报主键
     * @return 结果
     */
    @Override
    public int deleteWmsCardContentSendById(Long id)
    {
        return wmsCardContentSendMapper.deleteWmsCardContentSendById(id);
    }
}
