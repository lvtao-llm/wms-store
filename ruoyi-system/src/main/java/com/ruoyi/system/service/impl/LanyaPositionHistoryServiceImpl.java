package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaPositionHistoryMapper;
import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.service.ILanyaPositionHistoryService;

/**
 * 历史轨迹Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@Service
public class LanyaPositionHistoryServiceImpl implements ILanyaPositionHistoryService 
{
    @Autowired
    private LanyaPositionHistoryMapper lanyaPositionHistoryMapper;

    /**
     * 查询历史轨迹
     * 
     * @param id 历史轨迹主键
     * @return 历史轨迹
     */
    @Override
    public LanyaPositionHistory selectLanyaPositionHistoryById(Long id)
    {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryById(id);
    }

    /**
     * 查询历史轨迹列表
     * 
     * @param lanyaPositionHistory 历史轨迹
     * @return 历史轨迹
     */
    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryList(LanyaPositionHistory lanyaPositionHistory)
    {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryList(lanyaPositionHistory);
    }

    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryListStartById(Long id) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryListStartById(id);
    }

    /**
     * 新增历史轨迹
     * 
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    @Override
    public int insertLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory)
    {
        lanyaPositionHistory.setCreateTime(DateUtils.getNowDate());
        return lanyaPositionHistoryMapper.insertLanyaPositionHistory(lanyaPositionHistory);
    }

    /**
     * 修改历史轨迹
     * 
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    @Override
    public int updateLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory)
    {
        return lanyaPositionHistoryMapper.updateLanyaPositionHistory(lanyaPositionHistory);
    }

    /**
     * 批量删除历史轨迹
     * 
     * @param ids 需要删除的历史轨迹主键
     * @return 结果
     */
    @Override
    public int deleteLanyaPositionHistoryByIds(Long[] ids)
    {
        return lanyaPositionHistoryMapper.deleteLanyaPositionHistoryByIds(ids);
    }

    /**
     * 删除历史轨迹信息
     * 
     * @param id 历史轨迹主键
     * @return 结果
     */
    @Override
    public int deleteLanyaPositionHistoryById(Long id)
    {
        return lanyaPositionHistoryMapper.deleteLanyaPositionHistoryById(id);
    }
}
