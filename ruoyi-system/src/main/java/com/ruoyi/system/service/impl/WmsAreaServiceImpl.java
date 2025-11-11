package com.ruoyi.system.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsAreaMapper;
import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.service.IWmsAreaService;

import javax.annotation.PostConstruct;

/**
 * 区域Service业务层处理
 *
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsAreaServiceImpl implements IWmsAreaService {
    @Autowired
    private WmsAreaMapper wmsAreaMapper;

    private Map<Long, WmsArea> areaMap = new LinkedHashMap<>();

    @PostConstruct
    public void loadAreaMap() {
        List<WmsArea> wmsAreas = wmsAreaMapper.selectWmsAreaList(new WmsArea());
        for (WmsArea wmsArea : wmsAreas) {
            areaMap.put(wmsArea.getAreaId(), wmsArea);
        }
    }

    /**
     * 查询区域
     *
     * @param areaId 区域主键
     * @return 区域
     */
    @Override
    public WmsArea selectWmsAreaByAreaId(Long areaId) {
        return wmsAreaMapper.selectWmsAreaByAreaId(areaId);
    }

    /**
     * 查询区域列表
     *
     * @param wmsArea 区域
     * @return 区域
     */
    @Override
    public List<WmsArea> selectWmsAreaList(WmsArea wmsArea) {
        return wmsAreaMapper.selectWmsAreaList(wmsArea);
    }

    /**
     * 新增区域
     *
     * @param wmsArea 区域
     * @return 结果
     */
    @Override
    public int insertWmsArea(WmsArea wmsArea) {
        wmsArea.setCreateTime(DateUtils.getNowDate());
        int i = wmsAreaMapper.insertWmsArea(wmsArea);
        if (i > 0) {
            areaMap.put(wmsArea.getAreaId(), wmsArea);
        }
        return i;
    }

    /**
     * 修改区域
     *
     * @param wmsArea 区域
     * @return 结果
     */
    @Override
    public int updateWmsArea(WmsArea wmsArea) {
        wmsArea.setUpdateTime(DateUtils.getNowDate());
        int i = wmsAreaMapper.updateWmsArea(wmsArea);
        if (i > 0) {
            areaMap.put(wmsArea.getAreaId(), wmsArea);
        }
        return i;
    }

    /**
     * 批量删除区域
     *
     * @param areaIds 需要删除的区域主键
     * @return 结果
     */
    @Override
    public int deleteWmsAreaByAreaIds(Long[] areaIds) {
        int i = wmsAreaMapper.deleteWmsAreaByAreaIds(areaIds);
        if (i > 0) {
            for (Long areaId : areaIds) {
                areaMap.remove(areaId);
            }
        }
        return i;
    }

    /**
     * 删除区域信息
     *
     * @param areaId 区域主键
     * @return 结果
     */
    @Override
    public int deleteWmsAreaByAreaId(Long areaId) {
        int i = wmsAreaMapper.deleteWmsAreaByAreaId(areaId);
        if (i > 0) {
            areaMap.remove(areaId);
        }
        return i;
    }

    @Override
    public Map<Long, WmsArea> getAreaMap() {
        return areaMap;
    }
}
