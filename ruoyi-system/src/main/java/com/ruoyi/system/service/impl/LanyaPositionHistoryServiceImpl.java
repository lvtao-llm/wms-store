package com.ruoyi.system.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
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
@DataSource(DataSourceType.SLAVE)
public class LanyaPositionHistoryServiceImpl implements ILanyaPositionHistoryService {
    @Autowired
    private LanyaPositionHistoryMapper lanyaPositionHistoryMapper;

    /**
     * 查询历史轨迹
     *
     * @param id 历史轨迹主键
     * @return 历史轨迹
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public LanyaPositionHistory selectLanyaPositionHistoryById(Long id) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryById(id);
    }

    /**
     * 查询历史轨迹列表
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 历史轨迹
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryList(LanyaPositionHistory lanyaPositionHistory) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryList(lanyaPositionHistory);
    }

    /**
     * 查询历史轨迹列表
     *
     * @param time      历史轨迹
     * @param tableName
     * @return 历史轨迹集合
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryListStartTime(Date time, int count, String tableName) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryListStartTime(time, count, tableName);
    }

    /**
     * 新增历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public int insertLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory) {
        lanyaPositionHistory.setCreateTime(DateUtils.getNowDate());
        return lanyaPositionHistoryMapper.insertLanyaPositionHistory(lanyaPositionHistory);
    }

    /**
     * 新增历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public int insertLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory, String tableName) {
        if (lanyaPositionHistory.getCreateTime() == null) {
            lanyaPositionHistory.setCreateTime(DateUtils.getNowDate());
        }
        return lanyaPositionHistoryMapper.insertLanyaPositionHistoryByTable(lanyaPositionHistory, tableName);
    }

    /**
     * @param lanyaPositionHistory
     * @param tableName
     * @return
     */
    @DataSource(DataSourceType.LANYA90)
    @Override
    public int insertLanya90PositionHistory(LanyaPositionHistory lanyaPositionHistory, String tableName) {
        return lanyaPositionHistoryMapper.insertLanyaPositionHistoryByTable(lanyaPositionHistory, tableName);
    }

    /**
     * 创建表
     *
     * @param tableName
     * @return
     */
    @DataSource(DataSourceType.LANYA90)
    @Override
    public int createTable(String tableName) {
        return lanyaPositionHistoryMapper.createTable(tableName);
    }

    /**
     * 查询表
     *
     * @param tableName
     * @param userId
     * @param date
     * @return
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryListByTable(String tableName, Long userId, Date date) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryListByTable(tableName, userId, date);

    }

    /**
     * 检查表是否存在
     *
     * @param tableName
     * @return
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public int checkTableExists(String tableName) {
        return lanyaPositionHistoryMapper.checkTableExists(tableName);
    }

    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryListByTableTimeRange(Date begin, Date end, Long personId, String tableName) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryListByTableTimeRange(begin, end, personId, tableName);
    }

    @Override
    public List<LanyaPositionHistory> selectNewLanyaPositionHistoryListByTable(String tableName) {
        return lanyaPositionHistoryMapper.selectNewLanyaPositionHistoryListByTable(tableName);
    }

    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryListByTableTimeRangeCardId(Date begin, Date end, Long cardId, String tableName) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryListByTableTimeRangeCardId(begin, end, cardId, tableName);
    }

    /**
     * 修改历史轨迹
     *
     * @param lanyaPositionHistory 历史轨迹
     * @return 结果
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public int updateLanyaPositionHistory(LanyaPositionHistory lanyaPositionHistory) {
        return lanyaPositionHistoryMapper.updateLanyaPositionHistory(lanyaPositionHistory);
    }

    /**
     * 批量删除历史轨迹
     *
     * @param ids 需要删除的历史轨迹主键
     * @return 结果
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public int deleteLanyaPositionHistoryByIds(Long[] ids) {
        return lanyaPositionHistoryMapper.deleteLanyaPositionHistoryByIds(ids);
    }

    /**
     * 删除历史轨迹信息
     *
     * @param id 历史轨迹主键
     * @return 结果
     */
    @DataSource(DataSourceType.SLAVE)
    @Override
    public int deleteLanyaPositionHistoryById(Long id) {
        return lanyaPositionHistoryMapper.deleteLanyaPositionHistoryById(id);
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<LanyaPositionHistory> selectLanyaPositionHistoryListStartTimeByCardAndName(Date beginTime, Date endTime, Long cardId, String realName) {
        return lanyaPositionHistoryMapper.selectLanyaPositionHistoryListStartTimeByCardAndName(beginTime, endTime, cardId, realName);
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<String> showPositionHistoryTableNames() {
        return lanyaPositionHistoryMapper.showPositionHistoryTableNames();
    }


}
