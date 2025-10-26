package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsDeviceCardWorkLogMapper;
import com.ruoyi.system.domain.WmsDeviceCardWorkLog;
import com.ruoyi.system.service.IWmsDeviceCardWorkLogService;

/**
 * 人脸发卡记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-19
 */
@Service
public class WmsDeviceCardWorkLogServiceImpl implements IWmsDeviceCardWorkLogService 
{
    @Autowired
    private WmsDeviceCardWorkLogMapper wmsDeviceCardWorkLogMapper;

    /**
     * 查询人脸发卡记录
     * 
     * @param id 人脸发卡记录主键
     * @return 人脸发卡记录
     */
    @Override
    public WmsDeviceCardWorkLog selectWmsDeviceCardWorkLogById(Long id)
    {
        return wmsDeviceCardWorkLogMapper.selectWmsDeviceCardWorkLogById(id);
    }

    /**
     * 查询人脸发卡记录列表
     * 
     * @param wmsDeviceCardWorkLog 人脸发卡记录
     * @return 人脸发卡记录
     */
    @Override
    public List<WmsDeviceCardWorkLog> selectWmsDeviceCardWorkLogList(WmsDeviceCardWorkLog wmsDeviceCardWorkLog)
    {
        return wmsDeviceCardWorkLogMapper.selectWmsDeviceCardWorkLogList(wmsDeviceCardWorkLog);
    }

    /**
     * 新增人脸发卡记录
     * 
     * @param wmsDeviceCardWorkLog 人脸发卡记录
     * @return 结果
     */
    @Override
    public int insertWmsDeviceCardWorkLog(WmsDeviceCardWorkLog wmsDeviceCardWorkLog)
    {
        wmsDeviceCardWorkLog.setCreateTime(DateUtils.getNowDate());
        return wmsDeviceCardWorkLogMapper.insertWmsDeviceCardWorkLog(wmsDeviceCardWorkLog);
    }

    /**
     * 修改人脸发卡记录
     * 
     * @param wmsDeviceCardWorkLog 人脸发卡记录
     * @return 结果
     */
    @Override
    public int updateWmsDeviceCardWorkLog(WmsDeviceCardWorkLog wmsDeviceCardWorkLog)
    {
        wmsDeviceCardWorkLog.setUpdateTime(DateUtils.getNowDate());
        return wmsDeviceCardWorkLogMapper.updateWmsDeviceCardWorkLog(wmsDeviceCardWorkLog);
    }

    /**
     * 批量删除人脸发卡记录
     * 
     * @param ids 需要删除的人脸发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsDeviceCardWorkLogByIds(Long[] ids)
    {
        return wmsDeviceCardWorkLogMapper.deleteWmsDeviceCardWorkLogByIds(ids);
    }

    /**
     * 删除人脸发卡记录信息
     * 
     * @param id 人脸发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsDeviceCardWorkLogById(Long id)
    {
        return wmsDeviceCardWorkLogMapper.deleteWmsDeviceCardWorkLogById(id);
    }

    @Override
    public Long selectWmsDeviceCardWorkLogIdInTimeByCardAndName(Date acceptTime, Long cardId, String realName) {
        return wmsDeviceCardWorkLogMapper.selectWmsDeviceCardWorkLogIdInTimeByCardAndName(acceptTime, cardId, realName);
    }

    @Override
    public List<WmsDeviceCardWorkLog> selectWmsDeviceCardWorkLogListEnd(WmsDeviceCardWorkLog query) {
        return wmsDeviceCardWorkLogMapper.selectWmsDeviceCardWorkLogListEnd(query);
    }

    @Override
    public int selectWmsDeviceCardWorkLogCountBySenderLanyaLogId(Long id) {
        return wmsDeviceCardWorkLogMapper.selectWmsDeviceCardWorkLogCountBySenderLanyaLogId(id);
    }
}
