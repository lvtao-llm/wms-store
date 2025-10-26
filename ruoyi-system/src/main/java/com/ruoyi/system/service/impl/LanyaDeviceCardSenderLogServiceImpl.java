package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaDeviceCardSenderLogMapper;
import com.ruoyi.system.domain.LanyaDeviceCardSenderLog;
import com.ruoyi.system.service.ILanyaDeviceCardSenderLogService;

/**
 * 人脸发卡记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class LanyaDeviceCardSenderLogServiceImpl implements ILanyaDeviceCardSenderLogService {
    @Autowired
    private LanyaDeviceCardSenderLogMapper lanyaDeviceCardSenderLogMapper;

    /**
     * 查询人脸发卡记录
     *
     * @param id 人脸发卡记录主键
     * @return 人脸发卡记录
     */
    @Override
    public LanyaDeviceCardSenderLog selectLanyaDeviceCardSenderLogById(Long id) {
        return lanyaDeviceCardSenderLogMapper.selectLanyaDeviceCardSenderLogById(id);
    }

    /**
     * 查询人脸发卡记录列表
     *
     * @param lanyaDeviceCardSenderLog 人脸发卡记录
     * @return 人脸发卡记录
     */
    @Override
    public List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogList(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog) {
        return lanyaDeviceCardSenderLogMapper.selectLanyaDeviceCardSenderLogList(lanyaDeviceCardSenderLog);
    }

    /**
     * 查询人脸发卡记录列表
     *
     * @param time 开始ID
     * @param count         获取数量
     * @return 人脸发卡记录集合
     */
    @Override
    public List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogListStartTime(Date time, int count) {
        return lanyaDeviceCardSenderLogMapper.selectLanyaDeviceCardSenderLogListStartTime(time, count);
    }

    /**
     * 新增人脸发卡记录
     *
     * @param lanyaDeviceCardSenderLog 人脸发卡记录
     * @return 结果
     */
    @Override
    public int insertLanyaDeviceCardSenderLog(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog) {
        lanyaDeviceCardSenderLog.setCreateTime(DateUtils.getNowDate());
        return lanyaDeviceCardSenderLogMapper.insertLanyaDeviceCardSenderLog(lanyaDeviceCardSenderLog);
    }

    /**
     * 修改人脸发卡记录
     *
     * @param lanyaDeviceCardSenderLog 人脸发卡记录
     * @return 结果
     */
    @Override
    public int updateLanyaDeviceCardSenderLog(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog) {
        lanyaDeviceCardSenderLog.setUpdateTime(DateUtils.getNowDate());
        return lanyaDeviceCardSenderLogMapper.updateLanyaDeviceCardSenderLog(lanyaDeviceCardSenderLog);
    }

    /**
     * 批量删除人脸发卡记录
     *
     * @param ids 需要删除的人脸发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardSenderLogByIds(Long[] ids) {
        return lanyaDeviceCardSenderLogMapper.deleteLanyaDeviceCardSenderLogByIds(ids);
    }

    /**
     * 删除人脸发卡记录信息
     *
     * @param id 人脸发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardSenderLogById(Long id) {
        return lanyaDeviceCardSenderLogMapper.deleteLanyaDeviceCardSenderLogById(id);
    }

    @Override
    public List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogListByNameCardType(String param) {
        return lanyaDeviceCardSenderLogMapper.selectLanyaDeviceCardSenderLogListByNameCardType(param);
    }
}
