package com.ruoyi.system.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialInMapper;
import com.ruoyi.system.domain.WmsMaterialIn;
import com.ruoyi.system.service.IWmsMaterialInService;

/**
 * 接料视图Service业务层处理
 *
 * @author ruoyi
 * @date 2025-11-08
 */
@Service
public class WmsMaterialInServiceImpl implements IWmsMaterialInService {
    @Autowired
    private WmsMaterialInMapper wmsMaterialInMapper;

    /**
     * 查询接料视图
     *
     * @param jlBh 接料视图主键
     * @return 接料视图
     */
    @Override
    public WmsMaterialIn selectWmsMaterialInByJlBh(String jlBh) {
        return wmsMaterialInMapper.selectWmsMaterialInByJlBh(jlBh);
    }

    /**
     * 查询接料视图列表
     *
     * @param wmsMaterialIn 接料视图
     * @return 接料视图
     */
    @Override
    public List<WmsMaterialIn> selectWmsMaterialInList(WmsMaterialIn wmsMaterialIn) {
        return wmsMaterialInMapper.selectWmsMaterialInList(wmsMaterialIn);
    }

    /**
     * 新增接料视图
     *
     * @param wmsMaterialIn 接料视图
     * @return 结果
     */
    @Override
    public int insertWmsMaterialIn(WmsMaterialIn wmsMaterialIn) {
        return wmsMaterialInMapper.insertWmsMaterialIn(wmsMaterialIn);
    }

    /**
     * 修改接料视图
     *
     * @param wmsMaterialIn 接料视图
     * @return 结果
     */
    @Override
    public int updateWmsMaterialIn(WmsMaterialIn wmsMaterialIn) {
        return wmsMaterialInMapper.updateWmsMaterialIn(wmsMaterialIn);
    }

    /**
     * 批量删除接料视图
     *
     * @param jlBhs 需要删除的接料视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialInByJlBhs(String[] jlBhs) {
        return wmsMaterialInMapper.deleteWmsMaterialInByJlBhs(jlBhs);
    }

    /**
     * 删除接料视图信息
     *
     * @param jlBh 接料视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialInByJlBh(String jlBh) {
        return wmsMaterialInMapper.deleteWmsMaterialInByJlBh(jlBh);
    }

    @Override
    public List<WmsMaterialIn> selectWmsMaterialInListByAreaNames(WmsMaterialIn wmsMaterialIn, List<String> areaNames) {
        return wmsMaterialInMapper.selectWmsMaterialInListByAreaNames(wmsMaterialIn, areaNames);
    }
}
