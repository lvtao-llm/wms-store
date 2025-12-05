package com.ruoyi.system.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.LanyaPositionHistory;

/**
 * 历史轨迹Service接口
 *
 * @author ruoyi
 * @date 2025-10-10
 */
public interface ILanyaPositionHistoryService {
    /**
     * 查询历史轨迹
     *
     * @param id 历史轨迹主键
     * @return 历史轨迹
     */
    public LanyaPositionHistory selectLanyaPositionHistoryById(Long id);

    /**
     * 查询历史轨迹列表
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 历史轨迹集合
     */
    public List<LanyaPositionHistory> selectLanyaPositionHistoryList(LanyaPositionHistory lanyaPositionHistory);

    /**
     * 查询历史轨迹列表
     *
     * @param time      历史轨迹
     * @param tableName
     * @return 历史轨迹集合
     */
    public List<LanyaPositionHistory> selectLanyaPositionHistoryListStartTime(Date time, int count, String tableName);

    /**
     * 新增历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    public int insertLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory);

    /**
     * 新增历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    public int insertLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory, String tableName);

    /**
     * 修改历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    public int updateLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory);

    /**
     * 批量删除历史轨迹
     *
     * @param ids 需要删除的历史轨迹主键集合
     * @return 结果
     */
    public int deleteLanyaPositionHistoryByIds(Long[] ids);

    /**
     * 删除历史轨迹信息
     *
     * @param id 历史轨迹主键
     * @return 结果
     */
    public int deleteLanyaPositionHistoryById(Long id);

    public List<LanyaPositionHistory> selectLanyaPositionHistoryListStartTimeByCardAndName(Date beginTime, Date endTime, Long cardId, String realName);

    List<String> showPositionHistoryTableNames();

    int insertLanya90PositionHistory(LanyaPositionHistory position, String tableName);

    int createTable(String tableName);

    List<LanyaPositionHistory> selectLanyaPositionHistoryListByTable(String tableName, Long userId, Date date);

    int checkTableExists(String tableName);

    List<LanyaPositionHistory> selectLanyaPositionHistoryListByTableTimeRange(Date begin, Date end, Long personId, String tableName);


    List<LanyaPositionHistory> selectNewLanyaPositionHistoryListByTable(String tableName);
}
