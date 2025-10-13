package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LanyaVehicleInfoVisitor;

/**
 * 访客车辆Mapper接口
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
public interface LanyaVehicleInfoVisitorMapper 
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
     * 删除访客车辆
     * 
     * @param id 访客车辆主键
     * @return 结果
     */
    public int deleteLanyaVehicleInfoVisitorById(Long id);

    /**
     * 批量删除访客车辆
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanyaVehicleInfoVisitorByIds(Long[] ids);
}
