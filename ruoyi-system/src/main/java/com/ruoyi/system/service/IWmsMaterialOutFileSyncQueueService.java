package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.WmsMaterialOutFileSyncQueue;

/**
 * 物料出入库相关文件同步队列Service接口
 *
 * @author ruoyi
 * @date 2025-11-20
 */
public interface IWmsMaterialOutFileSyncQueueService {
    /**
     * 查询物料出入库相关文件同步队列
     *
     * @param 调拨明细编号 物料出入库相关文件同步队列主键
     * @return 物料出入库相关文件同步队列
     */
    public WmsMaterialOutFileSyncQueue selectWmsMaterialOutFileSyncQueueBy调拨明细编号(String 调拨明细编号);

    /**
     * 查询物料出入库相关文件同步队列列表
     *
     * @param wmsMaterialOutFileSyncQueue 物料出入库相关文件同步队列
     * @return 物料出入库相关文件同步队列集合
     */
    public List<WmsMaterialOutFileSyncQueue> selectWmsMaterialOutFileSyncQueueList(WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue);

    /**
     * 新增物料出入库相关文件同步队列
     *
     * @param wmsMaterialOutFileSyncQueue 物料出入库相关文件同步队列
     * @return 结果
     */
    public int insertWmsMaterialOutFileSyncQueue(WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue);

    /**
     * 修改物料出入库相关文件同步队列
     *
     * @param wmsMaterialOutFileSyncQueue 物料出入库相关文件同步队列
     * @return 结果
     */
    public int updateWmsMaterialOutFileSyncQueue(WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue);

    /**
     * 批量删除物料出入库相关文件同步队列
     *
     * @param 调拨明细编号s 需要删除的物料出入库相关文件同步队列主键集合
     * @return 结果
     */
    public int deleteWmsMaterialOutFileSyncQueueBy调拨明细编号s(String[] 调拨明细编号s);

    /**
     * 删除物料出入库相关文件同步队列信息
     *
     * @param 调拨明细编号 物料出入库相关文件同步队列主键
     * @return 结果
     */
    public int deleteWmsMaterialOutFileSyncQueueBy调拨明细编号(String 调拨明细编号);

    List<WmsMaterialOutFileSyncQueue> selectWmsMaterialOutFileSyncQueueListByCount(int count);
}
