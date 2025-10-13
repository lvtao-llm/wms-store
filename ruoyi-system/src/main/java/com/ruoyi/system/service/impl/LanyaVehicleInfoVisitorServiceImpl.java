package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaVehicleInfoVisitorMapper;
import com.ruoyi.system.domain.LanyaVehicleInfoVisitor;
import com.ruoyi.system.service.ILanyaVehicleInfoVisitorService;

/**
 * 访客车辆Service业务层处理
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
@Service
public class LanyaVehicleInfoVisitorServiceImpl implements ILanyaVehicleInfoVisitorService 
{
    @Autowired
    private LanyaVehicleInfoVisitorMapper lanyaVehicleInfoVisitorMapper;

    /**
     * 查询访客车辆
     * 
     * @param id 访客车辆主键
     * @return 访客车辆
     */
    @Override
    public LanyaVehicleInfoVisitor selectLanyaVehicleInfoVisitorById(Long id)
    {
        return lanyaVehicleInfoVisitorMapper.selectLanyaVehicleInfoVisitorById(id);
    }

    /**
     * 查询访客车辆列表
     * 
     * @param lanyaVehicleInfoVisitor 访客车辆
     * @return 访客车辆
     */
    @Override
    public List<LanyaVehicleInfoVisitor> selectLanyaVehicleInfoVisitorList(LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor)
    {
        return lanyaVehicleInfoVisitorMapper.selectLanyaVehicleInfoVisitorList(lanyaVehicleInfoVisitor);
    }

    /**
     * 新增访客车辆
     * 
     * @param lanyaVehicleInfoVisitor 访客车辆
     * @return 结果
     */
    @Override
    public int insertLanyaVehicleInfoVisitor(LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor)
    {
        lanyaVehicleInfoVisitor.setCreateTime(DateUtils.getNowDate());
        return lanyaVehicleInfoVisitorMapper.insertLanyaVehicleInfoVisitor(lanyaVehicleInfoVisitor);
    }

    /**
     * 修改访客车辆
     * 
     * @param lanyaVehicleInfoVisitor 访客车辆
     * @return 结果
     */
    @Override
    public int updateLanyaVehicleInfoVisitor(LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor)
    {
        lanyaVehicleInfoVisitor.setUpdateTime(DateUtils.getNowDate());
        return lanyaVehicleInfoVisitorMapper.updateLanyaVehicleInfoVisitor(lanyaVehicleInfoVisitor);
    }

    /**
     * 批量删除访客车辆
     * 
     * @param ids 需要删除的访客车辆主键
     * @return 结果
     */
    @Override
    public int deleteLanyaVehicleInfoVisitorByIds(Long[] ids)
    {
        return lanyaVehicleInfoVisitorMapper.deleteLanyaVehicleInfoVisitorByIds(ids);
    }

    /**
     * 删除访客车辆信息
     * 
     * @param id 访客车辆主键
     * @return 结果
     */
    @Override
    public int deleteLanyaVehicleInfoVisitorById(Long id)
    {
        return lanyaVehicleInfoVisitorMapper.deleteLanyaVehicleInfoVisitorById(id);
    }
}
