package com.ruoyi.system.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.LanyaDeviceCardSenderLog;
import org.apache.ibatis.annotations.Param;

/**
 * 人脸发卡记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-10
 */
public interface LanyaDeviceCardSenderLogMapper {
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
     * @param time 开始ID
     * @param count         数量
     * @return 人脸发卡记录集合
     */
    List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogListStartTime(@Param("time") Date time, @Param("count") int count);

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
     * 删除人脸发卡记录
     *
     * @param id 人脸发卡记录主键
     * @return 结果
     */
    public int deleteLanyaDeviceCardSenderLogById(Long id);

    /**
     * 批量删除人脸发卡记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanyaDeviceCardSenderLogByIds(Long[] ids);

    List<LanyaDeviceCardSenderLog> selectLanyaDeviceCardSenderLogListByNameCardType(String param);


}
