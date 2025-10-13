package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaVehicleInfoVisitor;

/**
 * 访客车辆Service接口
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
public interface ILanyaVehicleInfoVisitorService 
{
    /**
     * 查询访客车辆
     * 
     * @param id 访客车辆主键
     * @return 访客车辆
     */
    public LanyaVehicleInfoVisitor selectLanyaVehicleInfoVisitorById(Long id);

    /**
     * 查询访客车辆列表
     * 
     * @param lanyaVehicleInfoVisitor 访客车辆
     * @return 访客车辆集合
     */
    public List<LanyaVehicleInfoVisitor> selectLanyaVehicleInfoVisitorList(LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor);

    /**
     * 新增访客车辆
     * 
     * @param lanyaVehicleInfoVisitor 访客车辆
     * @return 结果
     */
    public int insertLanyaVehicleInfoVisitor(LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor);

    /**
     * 修改访客车辆
     * 
     * @param lanyaVehicleInfoVisitor 访客车辆
     * @return 结果
     */
    public int updateLanyaVehicleInfoVisitor(LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor);

    /**
     * 批量删除访客车辆
     * 
     * @param ids 需要删除的访客车辆主键集合
     * @return 结果
     */
    public int deleteLanyaVehicleInfoVisitorByIds(Long[] ids);

    /**
     * 删除访客车辆信息
     * 
     * @param id 访客车辆主键
     * @return 结果
     */
    public int deleteLanyaVehicleInfoVisitorById(Long id);
}
