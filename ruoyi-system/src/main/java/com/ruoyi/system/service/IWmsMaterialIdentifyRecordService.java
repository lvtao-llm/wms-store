package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsMaterialIdentifyRecord;

/**
 * 物料识别记录Service接口
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
public interface IWmsMaterialIdentifyRecordService 
{
    /**
     * 查询物料识别记录
     * 
     * @param ymd 物料识别记录主键
     * @return 物料识别记录
     */
    public WmsMaterialIdentifyRecord selectWmsMaterialIdentifyRecordByYmd(String ymd);

    /**
     * 查询物料识别记录列表
     * 
     * @param wmsMaterialIdentifyRecord 物料识别记录
     * @return 物料识别记录集合
     */
    public List<WmsMaterialIdentifyRecord> selectWmsMaterialIdentifyRecordList(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord);

    /**
     * 新增物料识别记录
     * 
     * @param wmsMaterialIdentifyRecord 物料识别记录
     * @return 结果
     */
    public int insertWmsMaterialIdentifyRecord(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord);

    /**
     * 修改物料识别记录
     * 
     * @param wmsMaterialIdentifyRecord 物料识别记录
     * @return 结果
     */
    public int updateWmsMaterialIdentifyRecord(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord);

    /**
     * 批量删除物料识别记录
     * 
     * @param ymds 需要删除的物料识别记录主键集合
     * @return 结果
     */
    public int deleteWmsMaterialIdentifyRecordByYmds(String[] ymds);

    /**
     * 删除物料识别记录信息
     * 
     * @param ymd 物料识别记录主键
     * @return 结果
     */
    public int deleteWmsMaterialIdentifyRecordByYmd(String ymd);
}
