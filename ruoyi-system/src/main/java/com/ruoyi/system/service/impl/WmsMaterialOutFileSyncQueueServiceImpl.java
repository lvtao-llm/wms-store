package com.ruoyi.system.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialOutFileSyncQueueMapper;
import com.ruoyi.system.domain.WmsMaterialOutFileSyncQueue;
import com.ruoyi.system.service.IWmsMaterialOutFileSyncQueueService;

/**
 * 物料出入库相关文件同步队列Service业务层处理
 *
 * @author ruoyi
 * @date 2025-11-20
 */
@Service
public class WmsMaterialOutFileSyncQueueServiceImpl implements IWmsMaterialOutFileSyncQueueService {
    @Autowired
    private WmsMaterialOutFileSyncQueueMapper wmsMaterialOutFileSyncQueueMapper;

    /**
     * 查询物料出入库相关文件同步队列
     *
     * @param 调拨明细编号 物料出入库相关文件同步队列主键
     * @return 物料出入库相关文件同步队列
     */
    @Override
    public WmsMaterialOutFileSyncQueue selectWmsMaterialOutFileSyncQueueBy调拨明细编号(String 调拨明细编号) {
        return wmsMaterialOutFileSyncQueueMapper.selectWmsMaterialOutFileSyncQueueBy调拨明细编号(调拨明细编号);
    }

    /**
     * 查询物料出入库相关文件同步队列列表
     *
     * @param wmsMaterialOutFileSyncQueue 物料出入库相关文件同步队列
     * @return 物料出入库相关文件同步队列
     */
    @Override
    public List<WmsMaterialOutFileSyncQueue> selectWmsMaterialOutFileSyncQueueList(WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue) {
        return wmsMaterialOutFileSyncQueueMapper.selectWmsMaterialOutFileSyncQueueList(wmsMaterialOutFileSyncQueue);
    }

    /**
     * 新增物料出入库相关文件同步队列
     *
     * @param wmsMaterialOutFileSyncQueue 物料出入库相关文件同步队列
     * @return 结果
     */
    @Override
    public int insertWmsMaterialOutFileSyncQueue(WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue) {
        return wmsMaterialOutFileSyncQueueMapper.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
    }

    /**
     * 修改物料出入库相关文件同步队列
     *
     * @param wmsMaterialOutFileSyncQueue 物料出入库相关文件同步队列
     * @return 结果
     */
    @Override
    public int updateWmsMaterialOutFileSyncQueue(WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue) {
        return wmsMaterialOutFileSyncQueueMapper.updateWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
    }

    /**
     * 批量删除物料出入库相关文件同步队列
     *
     * @param 调拨明细编号s 需要删除的物料出入库相关文件同步队列主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialOutFileSyncQueueBy调拨明细编号s(String[] 调拨明细编号s) {
        return wmsMaterialOutFileSyncQueueMapper.deleteWmsMaterialOutFileSyncQueueBy调拨明细编号s(调拨明细编号s);
    }

    /**
     * 删除物料出入库相关文件同步队列信息
     *
     * @param 调拨明细编号 物料出入库相关文件同步队列主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialOutFileSyncQueueBy调拨明细编号(String 调拨明细编号) {
        return wmsMaterialOutFileSyncQueueMapper.deleteWmsMaterialOutFileSyncQueueBy调拨明细编号(调拨明细编号);
    }

    @Override
    public List<WmsMaterialOutFileSyncQueue> selectWmsMaterialOutFileSyncQueueListByCount(int count) {
        return wmsMaterialOutFileSyncQueueMapper.selectWmsMaterialOutFileSyncQueueListByCount(count);
    }
}
