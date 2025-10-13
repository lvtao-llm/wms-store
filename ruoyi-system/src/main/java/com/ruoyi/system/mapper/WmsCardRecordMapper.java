package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsCardRecord;

/**
 * 发卡记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface WmsCardRecordMapper 
{
    /**
     * 查询发卡记录
     * 
     * @param cardRecordId 发卡记录主键
     * @return 发卡记录
     */
    public WmsCardRecord selectWmsCardRecordByCardRecordId(Long cardRecordId);

    /**
     * 查询发卡记录列表
     * 
     * @param wmsCardRecord 发卡记录
     * @return 发卡记录集合
     */
    public List<WmsCardRecord> selectWmsCardRecordList(WmsCardRecord wmsCardRecord);

    /**
     * 新增发卡记录
     * 
     * @param wmsCardRecord 发卡记录
     * @return 结果
     */
    public int insertWmsCardRecord(WmsCardRecord wmsCardRecord);

    /**
     * 修改发卡记录
     * 
     * @param wmsCardRecord 发卡记录
     * @return 结果
     */
    public int updateWmsCardRecord(WmsCardRecord wmsCardRecord);

    /**
     * 删除发卡记录
     * 
     * @param cardRecordId 发卡记录主键
     * @return 结果
     */
    public int deleteWmsCardRecordByCardRecordId(Long cardRecordId);

    /**
     * 批量删除发卡记录
     * 
     * @param cardRecordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsCardRecordByCardRecordIds(Long[] cardRecordIds);
}
