package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaCoreAreaMapper;
import com.ruoyi.system.domain.LanyaCoreArea;
import com.ruoyi.system.service.ILanyaCoreAreaService;

/**
 * 区域信息Service业务层处理
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
@Service
public class LanyaCoreAreaServiceImpl implements ILanyaCoreAreaService 
{
    @Autowired
    private LanyaCoreAreaMapper lanyaCoreAreaMapper;

    /**
     * 查询区域信息
     * 
     * @param areaId 区域信息主键
     * @return 区域信息
     */
    @Override
    public LanyaCoreArea selectLanyaCoreAreaByAreaId(Long areaId)
    {
        return lanyaCoreAreaMapper.selectLanyaCoreAreaByAreaId(areaId);
    }

    /**
     * 查询区域信息列表
     * 
     * @param lanyaCoreArea 区域信息
     * @return 区域信息
     */
    @Override
    public List<LanyaCoreArea> selectLanyaCoreAreaList(LanyaCoreArea lanyaCoreArea)
    {
        return lanyaCoreAreaMapper.selectLanyaCoreAreaList(lanyaCoreArea);
    }

    /**
     * 新增区域信息
     * 
     * @param lanyaCoreArea 区域信息
     * @return 结果
     */
    @Override
    public int insertLanyaCoreArea(LanyaCoreArea lanyaCoreArea)
    {
        lanyaCoreArea.setCreateTime(DateUtils.getNowDate());
        return lanyaCoreAreaMapper.insertLanyaCoreArea(lanyaCoreArea);
    }

    /**
     * 修改区域信息
     * 
     * @param lanyaCoreArea 区域信息
     * @return 结果
     */
    @Override
    public int updateLanyaCoreArea(LanyaCoreArea lanyaCoreArea)
    {
        lanyaCoreArea.setUpdateTime(DateUtils.getNowDate());
        return lanyaCoreAreaMapper.updateLanyaCoreArea(lanyaCoreArea);
    }

    /**
     * 批量删除区域信息
     * 
     * @param areaIds 需要删除的区域信息主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreAreaByAreaIds(Long[] areaIds)
    {
        return lanyaCoreAreaMapper.deleteLanyaCoreAreaByAreaIds(areaIds);
    }

    /**
     * 删除区域信息信息
     * 
     * @param areaId 区域信息主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreAreaByAreaId(Long areaId)
    {
        return lanyaCoreAreaMapper.deleteLanyaCoreAreaByAreaId(areaId);
    }
}
