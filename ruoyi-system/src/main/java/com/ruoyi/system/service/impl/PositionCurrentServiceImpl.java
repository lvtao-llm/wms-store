package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PositionCurrentMapper;
import com.ruoyi.system.domain.PositionCurrent;
import com.ruoyi.system.service.IPositionCurrentService;

/**
 * 实时定位Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
@Service
@DataSource(DataSourceType.SLAVE)
public class PositionCurrentServiceImpl implements IPositionCurrentService 
{
    @Autowired
    private PositionCurrentMapper positionCurrentMapper;

    /**
     * 查询实时定位
     * 
     * @param id 实时定位主键
     * @return 实时定位
     */
    @Override
    public PositionCurrent selectPositionCurrentById(Long id)
    {
        return positionCurrentMapper.selectPositionCurrentById(id);
    }

    /**
     * 查询实时定位列表
     * 
     * @param positionCurrent 实时定位
     * @return 实时定位
     */
    @Override
    public List<PositionCurrent> selectPositionCurrentList(PositionCurrent positionCurrent)
    {
        return positionCurrentMapper.selectPositionCurrentList(positionCurrent);
    }

    /**
     * 新增实时定位
     * 
     * @param positionCurrent 实时定位
     * @return 结果
     */
    @Override
    public int insertPositionCurrent(PositionCurrent positionCurrent)
    {
        positionCurrent.setCreateTime(DateUtils.getNowDate());
        return positionCurrentMapper.insertPositionCurrent(positionCurrent);
    }

    /**
     * 修改实时定位
     * 
     * @param positionCurrent 实时定位
     * @return 结果
     */
    @Override
    public int updatePositionCurrent(PositionCurrent positionCurrent)
    {
        return positionCurrentMapper.updatePositionCurrent(positionCurrent);
    }

    /**
     * 批量删除实时定位
     * 
     * @param ids 需要删除的实时定位主键
     * @return 结果
     */
    @Override
    public int deletePositionCurrentByIds(Long[] ids)
    {
        return positionCurrentMapper.deletePositionCurrentByIds(ids);
    }

    /**
     * 删除实时定位信息
     * 
     * @param id 实时定位主键
     * @return 结果
     */
    @Override
    public int deletePositionCurrentById(Long id)
    {
        return positionCurrentMapper.deletePositionCurrentById(id);
    }
}
