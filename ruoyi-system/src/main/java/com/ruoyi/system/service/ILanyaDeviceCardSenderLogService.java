package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.LanyaDeviceCardSenderLog;

/**
 * 人脸发卡记录Service接口
 *
 * @author ruoyi
 * @date 2025-10-10
 */

public interface ILanyaDeviceCardSenderLogService {
    /**
     * 查询人脸发卡记录
     *
     * @param id 人脸发卡记录主键
     * @return 人脸发卡记录
     */
    public LanyaDeviceCardSenderLog selectLanyaDeviceCardSenderLogById(Long id);

    /**
     * 查询人脸发卡记录列表
     *
     * @param lanyaDeviceCardSenderLog 人脸发卡记录
     * @return 人脸发卡记录集合
     */
    public List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogList(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog);

    /**
     * 查询人脸发卡记录列表
     *
     * @param cardLogOffset 人脸发卡记录
     * @param count         查询数量
     * @return 人脸发卡记录集合
     */
    List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogListStartId(long cardLogOffset, int count);

    /**
     * 新增人脸发卡记录
     *
     * @param lanyaDeviceCardSenderLog 人脸发卡记录
     * @return 结果
     */
    public int insertLanyaDeviceCardSenderLog(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog);

    /**
     * 修改人脸发卡记录
     *
     * @param lanyaDeviceCardSenderLog 人脸发卡记录
     * @return 结果
     */
    public int updateLanyaDeviceCardSenderLog(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog);

    /**
     * 批量删除人脸发卡记录
     *
     * @param ids 需要删除的人脸发卡记录主键集合
     * @return 结果
     */
    public int deleteLanyaDeviceCardSenderLogByIds(Long[] ids);

    /**
     * 删除人脸发卡记录信息
     *
     * @param id 人脸发卡记录主键
     * @return 结果
     */
    public int deleteLanyaDeviceCardSenderLogById(Long id);

    List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogListByNameCardType(String param);


}

