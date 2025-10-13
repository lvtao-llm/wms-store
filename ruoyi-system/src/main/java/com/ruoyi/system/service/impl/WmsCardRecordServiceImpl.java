package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsCardRecordMapper;
import com.ruoyi.system.domain.WmsCardRecord;
import com.ruoyi.system.service.IWmsCardRecordService;

/**
 * 发卡记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsCardRecordServiceImpl implements IWmsCardRecordService 
{
    @Autowired
    private WmsCardRecordMapper wmsCardRecordMapper;

    /**
     * 查询发卡记录
     * 
     * @param cardRecordId 发卡记录主键
     * @return 发卡记录
     */
    @Override
    public WmsCardRecord selectWmsCardRecordByCardRecordId(Long cardRecordId)
    {
        return wmsCardRecordMapper.selectWmsCardRecordByCardRecordId(cardRecordId);
    }

    /**
     * 查询发卡记录列表
     * 
     * @param wmsCardRecord 发卡记录
     * @return 发卡记录
     */
    @Override
    public List<WmsCardRecord> selectWmsCardRecordList(WmsCardRecord wmsCardRecord)
    {
        return wmsCardRecordMapper.selectWmsCardRecordList(wmsCardRecord);
    }

    /**
     * 新增发卡记录
     * 
     * @param wmsCardRecord 发卡记录
     * @return 结果
     */
    @Override
    public int insertWmsCardRecord(WmsCardRecord wmsCardRecord)
    {
        return wmsCardRecordMapper.insertWmsCardRecord(wmsCardRecord);
    }

    /**
     * 修改发卡记录
     * 
     * @param wmsCardRecord 发卡记录
     * @return 结果
     */
    @Override
    public int updateWmsCardRecord(WmsCardRecord wmsCardRecord)
    {
        return wmsCardRecordMapper.updateWmsCardRecord(wmsCardRecord);
    }

    /**
     * 批量删除发卡记录
     * 
     * @param cardRecordIds 需要删除的发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsCardRecordByCardRecordIds(Long[] cardRecordIds)
    {
        return wmsCardRecordMapper.deleteWmsCardRecordByCardRecordIds(cardRecordIds);
    }

    /**
     * 删除发卡记录信息
     * 
     * @param cardRecordId 发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsCardRecordByCardRecordId(Long cardRecordId)
    {
        return wmsCardRecordMapper.deleteWmsCardRecordByCardRecordId(cardRecordId);
    }
}
