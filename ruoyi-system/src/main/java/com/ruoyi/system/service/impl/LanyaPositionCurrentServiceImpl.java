package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaPositionCurrentMapper;
import com.ruoyi.system.domain.LanyaPositionCurrent;
import com.ruoyi.system.service.ILanyaPositionCurrentService;

/**
 * 实时定位Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
@Service
@DataSource(DataSourceType.SLAVE)
public class LanyaPositionCurrentServiceImpl implements ILanyaPositionCurrentService 
{
    @Autowired
    private LanyaPositionCurrentMapper lanyaPositionCurrentMapper;

    /**
     * 查询实时定位
     * 
     * @param id 实时定位主键
     * @return 实时定位
     */
    @Override
    public LanyaPositionCurrent selectLanyaPositionCurrentById(Long id)
    {
        return lanyaPositionCurrentMapper.selectLanyaPositionCurrentById(id);
    }

    /**
     * 查询实时定位列表
     * 
     * @param lanyaPositionCurrent 实时定位
     * @return 实时定位
     */
    @Override
    public List<LanyaPositionCurrent> selectLanyaPositionCurrentList(LanyaPositionCurrent lanyaPositionCurrent)
    {
        return lanyaPositionCurrentMapper.selectLanyaPositionCurrentList(lanyaPositionCurrent);
    }

    /**
     * 新增实时定位
     * 
     * @param lanyaPositionCurrent 实时定位
     * @return 结果
     */
    @Override
    public int insertLanyaPositionCurrent(LanyaPositionCurrent lanyaPositionCurrent)
    {
        lanyaPositionCurrent.setCreateTime(DateUtils.getNowDate());
        return lanyaPositionCurrentMapper.insertLanyaPositionCurrent(lanyaPositionCurrent);
    }

    /**
     * 修改实时定位
     * 
     * @param lanyaPositionCurrent 实时定位
     * @return 结果
     */
    @Override
    public int updateLanyaPositionCurrent(LanyaPositionCurrent lanyaPositionCurrent)
    {
        return lanyaPositionCurrentMapper.updateLanyaPositionCurrent(lanyaPositionCurrent);
    }

    /**
     * 批量删除实时定位
     * 
     * @param ids 需要删除的实时定位主键
     * @return 结果
     */
    @Override
    public int deleteLanyaPositionCurrentByIds(Long[] ids)
    {
        return lanyaPositionCurrentMapper.deleteLanyaPositionCurrentByIds(ids);
    }

    /**
     * 删除实时定位信息
     * 
     * @param id 实时定位主键
     * @return 结果
     */
    @Override
    public int deleteLanyaPositionCurrentById(Long id)
    {
        return lanyaPositionCurrentMapper.deleteLanyaPositionCurrentById(id);
    }

    @Override
    public int deleteLanyaPositionCurrentAll() {
        return lanyaPositionCurrentMapper.deleteLanyaPositionCurrentAll();
    }
}
