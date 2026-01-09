package com.ruoyi.system.mapper;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.domain.WmsDeviceCameraLog;
import org.apache.ibatis.annotations.Param;

/**
 * 摄像头识别日志Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-11
 */
public interface WmsDeviceCameraLogMapper
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
     * 删除摄像头识别日志
     *
     * @param id 摄像头识别日志主键
     * @return 结果
     */
    public int deleteWmsDeviceCameraLogById(Long id);

    /**
     * 批量删除摄像头识别日志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsDeviceCameraLogByIds(Long[] ids);

    List<WmsDeviceCameraLog> selectWmsDeviceCameraLogInId( @Param("ids") String[] ids,
                                                           @Param("begin") Date begin,
                                                           @Param("end") Date end);
}
