package com.ruoyi.quartz.mapper;

import java.util.List;

import com.ruoyi.quartz.domain.MlPositionHistory;

/**
 * 历史轨迹Mapper接口
 *
 * @author ruoyi
 * @date 2025-09-29
 */
public interface MlPositionHistoryMapper {
    /**
     * 查询历史轨迹
     *
     * @param id 历史轨迹主键
     * @return 历史轨迹
     */
    public MlPositionHistory selectMlPositionHistoryById(Long id);

    /**
     * 查询历史轨迹列表
     *
     * @param mlPositionHistory 历史轨迹
     * @return 历史轨迹集合
     */
    public List<MlPositionHistory> selectMlPositionHistoryList(MlPositionHistory mlPositionHistory);

    /**
     * 查询历史轨迹列表
     *
     * @param id 历史轨迹
     * @return 历史轨迹集合
     */
    public List<MlPositionHistory> selectMlPositionHistoryListFromId(Long id);

    /**
     * 新增历史轨迹
     *
     * @param mlPositionHistory 历史轨迹
     * @return 结果
     */
    public int insertMlPositionHistory(MlPositionHistory mlPositionHistory);

    /**
     * 修改历史轨迹
     *
     * @param mlPositionHistory 历史轨迹
     * @return 结果
     */
    public int updateMlPositionHistory(MlPositionHistory mlPositionHistory);

    /**
     * 删除历史轨迹
     *
     * @param id 历史轨迹主键
     * @return 结果
     */
    public int deleteMlPositionHistoryById(Long id);

    /**
     * 批量删除历史轨迹
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMlPositionHistoryByIds(Long[] ids);
}
