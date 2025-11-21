package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialIdentifyRecordMapper;
import com.ruoyi.system.domain.WmsMaterialIdentifyRecord;
import com.ruoyi.system.service.IWmsMaterialIdentifyRecordService;

/**
 * 物料识别记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
@Service
public class WmsMaterialIdentifyRecordServiceImpl implements IWmsMaterialIdentifyRecordService 
{
    @Autowired
    private WmsMaterialIdentifyRecordMapper wmsMaterialIdentifyRecordMapper;

    /**
     * 查询物料识别记录
     * 
     * @param ymd 物料识别记录主键
     * @return 物料识别记录
     */
    @Override
    public WmsMaterialIdentifyRecord selectWmsMaterialIdentifyRecordByYmd(String ymd)
    {
        return wmsMaterialIdentifyRecordMapper.selectWmsMaterialIdentifyRecordByYmd(ymd);
    }

    /**
     * 查询物料识别记录列表
     * 
     * @param wmsMaterialIdentifyRecord 物料识别记录
     * @return 物料识别记录
     */
    @Override
    public List<WmsMaterialIdentifyRecord> selectWmsMaterialIdentifyRecordList(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord)
    {
        return wmsMaterialIdentifyRecordMapper.selectWmsMaterialIdentifyRecordList(wmsMaterialIdentifyRecord);
    }

    /**
     * 新增物料识别记录
     * 
     * @param wmsMaterialIdentifyRecord 物料识别记录
     * @return 结果
     */
    @Override
    public int insertWmsMaterialIdentifyRecord(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord)
    {
        return wmsMaterialIdentifyRecordMapper.insertWmsMaterialIdentifyRecord(wmsMaterialIdentifyRecord);
    }

    /**
     * 修改物料识别记录
     * 
     * @param wmsMaterialIdentifyRecord 物料识别记录
     * @return 结果
     */
    @Override
    public int updateWmsMaterialIdentifyRecord(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord)
    {
        return wmsMaterialIdentifyRecordMapper.updateWmsMaterialIdentifyRecord(wmsMaterialIdentifyRecord);
    }

    /**
     * 批量删除物料识别记录
     * 
     * @param ymds 需要删除的物料识别记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialIdentifyRecordByYmds(String[] ymds)
    {
        return wmsMaterialIdentifyRecordMapper.deleteWmsMaterialIdentifyRecordByYmds(ymds);
    }

    /**
     * 删除物料识别记录信息
     * 
     * @param ymd 物料识别记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialIdentifyRecordByYmd(String ymd)
    {
        return wmsMaterialIdentifyRecordMapper.deleteWmsMaterialIdentifyRecordByYmd(ymd);
    }
}
