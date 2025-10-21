package com.ruoyi.system.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.WmsDeviceCardWorkLog;
import org.apache.ibatis.annotations.Param;

/**
 * 人脸发卡记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-19
 */
public interface WmsDeviceCardWorkLogMapper {
    /**
     * 查询人脸发卡记录
     *
     * @param id 人脸发卡记录主键
     * @return 人脸发卡记录
     */
    public WmsDeviceCardWorkLog selectWmsDeviceCardWorkLogById(Long id);

    /**
     * 查询人脸发卡记录列表
     *
     * @param wmsDeviceCardWorkLog 人脸发卡记录
     * @return 人脸发卡记录集合
     */
    public List<WmsDeviceCardWorkLog> selectWmsDeviceCardWorkLogList(WmsDeviceCardWorkLog wmsDeviceCardWorkLog);

    /**
     * 新增人脸发卡记录
     *
     * @param wmsDeviceCardWorkLog 人脸发卡记录
     * @return 结果
     */
    public int insertWmsDeviceCardWorkLog(WmsDeviceCardWorkLog wmsDeviceCardWorkLog);

    /**
     * 修改人脸发卡记录
     *
     * @param wmsDeviceCardWorkLog 人脸发卡记录
     * @return 结果
     */
    public int updateWmsDeviceCardWorkLog(WmsDeviceCardWorkLog wmsDeviceCardWorkLog);

    /**
     * 删除人脸发卡记录
     *
     * @param id 人脸发卡记录主键
     * @return 结果
     */
    public int deleteWmsDeviceCardWorkLogById(Long id);

    /**
     * 批量删除人脸发卡记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsDeviceCardWorkLogByIds(Long[] ids);

    Long selectWmsDeviceCardWorkLogIdInTimeByCardAndName(@Param("time") Date time, @Param("cardId") Long cardId, @Param("realName") String realName);

    List<WmsDeviceCardWorkLog> selectWmsDeviceCardWorkLogListEnd(WmsDeviceCardWorkLog query);
}
