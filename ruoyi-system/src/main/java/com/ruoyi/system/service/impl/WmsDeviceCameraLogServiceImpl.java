package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsDeviceCameraLogMapper;
import com.ruoyi.system.domain.WmsDeviceCameraLog;
import com.ruoyi.system.service.IWmsDeviceCameraLogService;

/**
 * 摄像头识别日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-11
 */
@Service
public class WmsDeviceCameraLogServiceImpl implements IWmsDeviceCameraLogService 
{
    @Autowired
    private WmsDeviceCameraLogMapper wmsDeviceCameraLogMapper;

    /**
     * 查询摄像头识别日志
     * 
     * @param id 摄像头识别日志主键
     * @return 摄像头识别日志
     */
    @Override
    public WmsDeviceCameraLog selectWmsDeviceCameraLogById(Long id)
    {
        return wmsDeviceCameraLogMapper.selectWmsDeviceCameraLogById(id);
    }

    /**
     * 查询摄像头识别日志列表
     * 
     * @param wmsDeviceCameraLog 摄像头识别日志
     * @return 摄像头识别日志
     */
    @Override
    public List<WmsDeviceCameraLog> selectWmsDeviceCameraLogList(WmsDeviceCameraLog wmsDeviceCameraLog)
    {
        return wmsDeviceCameraLogMapper.selectWmsDeviceCameraLogList(wmsDeviceCameraLog);
    }

    /**
     * 新增摄像头识别日志
     * 
     * @param wmsDeviceCameraLog 摄像头识别日志
     * @return 结果
     */
    @Override
    public int insertWmsDeviceCameraLog(WmsDeviceCameraLog wmsDeviceCameraLog)
    {
        return wmsDeviceCameraLogMapper.insertWmsDeviceCameraLog(wmsDeviceCameraLog);
    }

    /**
     * 修改摄像头识别日志
     * 
     * @param wmsDeviceCameraLog 摄像头识别日志
     * @return 结果
     */
    @Override
    public int updateWmsDeviceCameraLog(WmsDeviceCameraLog wmsDeviceCameraLog)
    {
        return wmsDeviceCameraLogMapper.updateWmsDeviceCameraLog(wmsDeviceCameraLog);
    }

    /**
     * 批量删除摄像头识别日志
     * 
     * @param ids 需要删除的摄像头识别日志主键
     * @return 结果
     */
    @Override
    public int deleteWmsDeviceCameraLogByIds(Long[] ids)
    {
        return wmsDeviceCameraLogMapper.deleteWmsDeviceCameraLogByIds(ids);
    }

    /**
     * 删除摄像头识别日志信息
     * 
     * @param id 摄像头识别日志主键
     * @return 结果
     */
    @Override
    public int deleteWmsDeviceCameraLogById(Long id)
    {
        return wmsDeviceCameraLogMapper.deleteWmsDeviceCameraLogById(id);
    }

    @Override
    public List<WmsDeviceCameraLog> selectWmsDeviceCameraLogInId(String[] ids, Date begin, Date end) {
        return wmsDeviceCameraLogMapper.selectWmsDeviceCameraLogInId(ids, begin, end);
    }
}
