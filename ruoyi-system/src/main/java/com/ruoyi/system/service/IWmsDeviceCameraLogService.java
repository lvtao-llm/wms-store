package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsDeviceCameraLog;

/**
 * 摄像头识别日志Service接口
 * 
 * @author ruoyi
 * @date 2025-11-11
 */
public interface IWmsDeviceCameraLogService 
{
    /**
     * 查询摄像头识别日志
     * 
     * @param id 摄像头识别日志主键
     * @return 摄像头识别日志
     */
    public WmsDeviceCameraLog selectWmsDeviceCameraLogById(Long id);

    /**
     * 查询摄像头识别日志列表
     * 
     * @param wmsDeviceCameraLog 摄像头识别日志
     * @return 摄像头识别日志集合
     */
    public List<WmsDeviceCameraLog> selectWmsDeviceCameraLogList(WmsDeviceCameraLog wmsDeviceCameraLog);

    /**
     * 新增摄像头识别日志
     * 
     * @param wmsDeviceCameraLog 摄像头识别日志
     * @return 结果
     */
    public int insertWmsDeviceCameraLog(WmsDeviceCameraLog wmsDeviceCameraLog);

    /**
     * 修改摄像头识别日志
     * 
     * @param wmsDeviceCameraLog 摄像头识别日志
     * @return 结果
     */
    public int updateWmsDeviceCameraLog(WmsDeviceCameraLog wmsDeviceCameraLog);

    /**
     * 批量删除摄像头识别日志
     * 
     * @param ids 需要删除的摄像头识别日志主键集合
     * @return 结果
     */
    public int deleteWmsDeviceCameraLogByIds(Long[] ids);

    /**
     * 删除摄像头识别日志信息
     * 
     * @param id 摄像头识别日志主键
     * @return 结果
     */
    public int deleteWmsDeviceCameraLogById(Long id);
}
