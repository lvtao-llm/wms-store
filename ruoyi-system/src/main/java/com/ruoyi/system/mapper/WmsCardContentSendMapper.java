package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsCardContentSend;

/**
 * 发送内容给卡播报Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-10
 */
public interface WmsCardContentSendMapper 
{
    /**
     * 查询发送内容给卡播报
     * 
     * @param id 发送内容给卡播报主键
     * @return 发送内容给卡播报
     */
    public WmsCardContentSend selectWmsCardContentSendById(Long id);

    /**
     * 查询发送内容给卡播报列表
     * 
     * @param wmsCardContentSend 发送内容给卡播报
     * @return 发送内容给卡播报集合
     */
    public List<WmsCardContentSend> selectWmsCardContentSendList(WmsCardContentSend wmsCardContentSend);

    /**
     * 新增发送内容给卡播报
     * 
     * @param wmsCardContentSend 发送内容给卡播报
     * @return 结果
     */
    public int insertWmsCardContentSend(WmsCardContentSend wmsCardContentSend);

    /**
     * 修改发送内容给卡播报
     * 
     * @param wmsCardContentSend 发送内容给卡播报
     * @return 结果
     */
    public int updateWmsCardContentSend(WmsCardContentSend wmsCardContentSend);

    /**
     * 删除发送内容给卡播报
     * 
     * @param id 发送内容给卡播报主键
     * @return 结果
     */
    public int deleteWmsCardContentSendById(Long id);

    /**
     * 批量删除发送内容给卡播报
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsCardContentSendByIds(Long[] ids);
}
