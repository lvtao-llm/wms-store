package com.ruoyi.quartz.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.quartz.mapper.MlPositionHistoryMapper;
import com.ruoyi.quartz.domain.MlPositionHistory;
import com.ruoyi.quartz.service.IMlPositionHistoryService;

/**
 * 历史轨迹Service业务层处理
 *
 * @author ruoyi
 * @date 2025-09-29
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class MlPositionHistoryServiceImpl implements IMlPositionHistoryService {
    @Autowired
    private MlPositionHistoryMapper mlPositionHistoryMapper;

    /**
     * 查询历史轨迹
     *
     * @param id 历史轨迹主键
     * @return 历史轨迹
     */
    @Override
    public MlPositionHistory selectMlPositionHistoryById(Long id) {
        return mlPositionHistoryMapper.selectMlPositionHistoryById(id);
    }

    /**
     * 查询历史轨迹列表
     *
     * @param mlPositionHistory 历史轨迹
     * @return 历史轨迹
     */
    @Override
    public List<MlPositionHistory> selectMlPositionHistoryList(MlPositionHistory mlPositionHistory) {
        return mlPositionHistoryMapper.selectMlPositionHistoryList(mlPositionHistory);
    }

    /**
     * 查询历史轨迹列表
     *
     * @param id 历史轨迹
     * @return 历史轨迹
     */
    public List<MlPositionHistory> selectMlPositionHistoryListFromId(Long id) {
        return mlPositionHistoryMapper.selectMlPositionHistoryListFromId(id);
    }

    /**
     * 新增历史轨迹
     *
     * @param mlPositionHistory 历史轨迹
     * @return 结果
     */
    @Override
    public int insertMlPositionHistory(MlPositionHistory mlPositionHistory) {
        mlPositionHistory.setCreateTime(DateUtils.getNowDate());
        return mlPositionHistoryMapper.insertMlPositionHistory(mlPositionHistory);
    }

    /**
     * 修改历史轨迹
     *
     * @param mlPositionHistory 历史轨迹
     * @return 结果
     */
    @Override
    public int updateMlPositionHistory(MlPositionHistory mlPositionHistory) {
        return mlPositionHistoryMapper.updateMlPositionHistory(mlPositionHistory);
    }

    /**
     * 批量删除历史轨迹
     *
     * @param ids 需要删除的历史轨迹主键
     * @return 结果
     */
    @Override
    public int deleteMlPositionHistoryByIds(Long[] ids) {
        return mlPositionHistoryMapper.deleteMlPositionHistoryByIds(ids);
    }

    /**
     * 删除历史轨迹信息
     *
     * @param id 历史轨迹主键
     * @return 结果
     */
    @Override
    public int deleteMlPositionHistoryById(Long id) {
        return mlPositionHistoryMapper.deleteMlPositionHistoryById(id);
    }
}
