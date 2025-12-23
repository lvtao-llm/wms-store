package com.ruoyi.system.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.LanyaPositionHistory;
import org.apache.ibatis.annotations.Param;

/**
 * 历史轨迹Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-10
 */
public interface LanyaPositionHistoryMapper {
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
     * 新增历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    public int insertLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory);

    /**
     * 修改历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    public int updateLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory);

    /**
     * 删除历史轨迹
     *
     * @param id 历史轨迹主键
     * @return 结果
     */
    public int deleteLanyaPositionHistoryById(Long id);

    /**
     * 批量删除历史轨迹
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanyaPositionHistoryByIds(Long[] ids);

    List<LanyaPositionHistory> selectLanyaPositionHistoryListStartTime(@Param("time") Date time, @Param("count") int count, @Param("tableName") String tableName);

    List<LanyaPositionHistory> selectLanyaPositionHistoryListStartTimeByCardAndName(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("cardId") Long cardId, @Param("realName") String realName);

    List<String> showPositionHistoryTableNames();

    int insertLanyaPositionHistoryByTable(@Param("history") LanyaPositionHistory history, @Param("tableName") String tableName);

    int createTable(@Param("tableName") String tableName);

    List<LanyaPositionHistory> selectLanyaPositionHistoryListByTable(@Param("tableName") String tableName, @Param("userId") Long userId, @Param("date") Date date);

    int checkTableExists(@Param("tableName") String tableName);

    List<LanyaPositionHistory> selectLanyaPositionHistoryListByTableTimeRange(@Param("begin") Date begin, @Param("end") Date end, @Param("personId") Long personId, @Param("tableName") String tableName);

    List<LanyaPositionHistory> selectNewLanyaPositionHistoryListByTable(@Param("tableName") String tableName);

    List<LanyaPositionHistory> selectLanyaPositionHistoryListByTableTimeRangeCardId(@Param("begin") Date begin, @Param("end") Date end, @Param("cardId") Long cardId, @Param("tableName") String tableName);
}
