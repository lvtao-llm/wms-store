package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaDeviceCard;

/**
 * 定位卡管理Service接口
 * 
 * @author ruoyi
 * @date 2025-10-12
 */
public interface ILanyaDeviceCardService 
{
    /**
     * 查询定位卡管理
     * 
     * @param id 定位卡管理主键
     * @return 定位卡管理
     */
    public LanyaDeviceCard selectLanyaDeviceCardById(Long id);

    /**
     * 查询定位卡管理列表
     * 
     * @param lanyaDeviceCard 定位卡管理
     * @return 定位卡管理集合
     */
    public List<LanyaDeviceCard> selectLanyaDeviceCardList(LanyaDeviceCard lanyaDeviceCard);

    /**
     * 新增定位卡管理
     * 
     * @param lanyaDeviceCard 定位卡管理
     * @return 结果
     */
    public int insertLanyaDeviceCard(LanyaDeviceCard lanyaDeviceCard);

    /**
     * 修改定位卡管理
     * 
     * @param lanyaDeviceCard 定位卡管理
     * @return 结果
     */
    public int updateLanyaDeviceCard(LanyaDeviceCard lanyaDeviceCard);

    /**
     * 批量删除定位卡管理
     * 
     * @param ids 需要删除的定位卡管理主键集合
     * @return 结果
     */
    public int deleteLanyaDeviceCardByIds(Long[] ids);

    /**
     * 删除定位卡管理信息
     * 
     * @param id 定位卡管理主键
     * @return 结果
     */
    public int deleteLanyaDeviceCardById(Long id);
}
