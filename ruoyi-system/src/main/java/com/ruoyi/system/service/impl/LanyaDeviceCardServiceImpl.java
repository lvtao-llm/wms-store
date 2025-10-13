package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaDeviceCardMapper;
import com.ruoyi.system.domain.LanyaDeviceCard;
import com.ruoyi.system.service.ILanyaDeviceCardService;

/**
 * 定位卡管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-12
 */
@Service
public class LanyaDeviceCardServiceImpl implements ILanyaDeviceCardService 
{
    @Autowired
    private LanyaDeviceCardMapper lanyaDeviceCardMapper;

    /**
     * 查询定位卡管理
     * 
     * @param id 定位卡管理主键
     * @return 定位卡管理
     */
    @Override
    public LanyaDeviceCard selectLanyaDeviceCardById(Long id)
    {
        return lanyaDeviceCardMapper.selectLanyaDeviceCardById(id);
    }

    /**
     * 查询定位卡管理列表
     * 
     * @param lanyaDeviceCard 定位卡管理
     * @return 定位卡管理
     */
    @Override
    public List<LanyaDeviceCard> selectLanyaDeviceCardList(LanyaDeviceCard lanyaDeviceCard)
    {
        return lanyaDeviceCardMapper.selectLanyaDeviceCardList(lanyaDeviceCard);
    }

    /**
     * 新增定位卡管理
     * 
     * @param lanyaDeviceCard 定位卡管理
     * @return 结果
     */
    @Override
    public int insertLanyaDeviceCard(LanyaDeviceCard lanyaDeviceCard)
    {
        lanyaDeviceCard.setCreateTime(DateUtils.getNowDate());
        return lanyaDeviceCardMapper.insertLanyaDeviceCard(lanyaDeviceCard);
    }

    /**
     * 修改定位卡管理
     * 
     * @param lanyaDeviceCard 定位卡管理
     * @return 结果
     */
    @Override
    public int updateLanyaDeviceCard(LanyaDeviceCard lanyaDeviceCard)
    {
        lanyaDeviceCard.setUpdateTime(DateUtils.getNowDate());
        return lanyaDeviceCardMapper.updateLanyaDeviceCard(lanyaDeviceCard);
    }

    /**
     * 批量删除定位卡管理
     * 
     * @param ids 需要删除的定位卡管理主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardByIds(Long[] ids)
    {
        return lanyaDeviceCardMapper.deleteLanyaDeviceCardByIds(ids);
    }

    /**
     * 删除定位卡管理信息
     * 
     * @param id 定位卡管理主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardById(Long id)
    {
        return lanyaDeviceCardMapper.deleteLanyaDeviceCardById(id);
    }
}
