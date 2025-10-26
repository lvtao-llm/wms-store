package com.ruoyi.system.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.domain.WmsDeviceCardWorkLog;

/**
 * 人脸发卡记录Service接口
 * 
 * @author ruoyi
 * @date 2025-10-19
 */
public interface IWmsDeviceCardWorkLogService 
{
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
     * 批量删除人脸发卡记录
     * 
     * @param ids 需要删除的人脸发卡记录主键集合
     * @return 结果
     */
    public int deleteWmsDeviceCardWorkLogByIds(Long[] ids);

    /**
     * 删除人脸发卡记录信息
     * 
     * @param id 人脸发卡记录主键
     * @return 结果
     */
    public int deleteWmsDeviceCardWorkLogById(Long id);

    public Long selectWmsDeviceCardWorkLogIdInTimeByCardAndName(Date acceptTime, Long cardId, String realName);

    List<WmsDeviceCardWorkLog> selectWmsDeviceCardWorkLogListEnd(WmsDeviceCardWorkLog query);

    int selectWmsDeviceCardWorkLogCountBySenderLanyaLogId(Long id);
}
