package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PositionCurrent;

/**
 * 实时定位Service接口
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
public interface IPositionCurrentService 
{
    /**
     * 查询实时定位
     * 
     * @param id 实时定位主键
     * @return 实时定位
     */
    public PositionCurrent selectPositionCurrentById(Long id);

    /**
     * 查询实时定位列表
     * 
     * @param positionCurrent 实时定位
     * @return 实时定位集合
     */
    public List<PositionCurrent> selectPositionCurrentList(PositionCurrent positionCurrent);

    /**
     * 新增实时定位
     * 
     * @param positionCurrent 实时定位
     * @return 结果
     */
    public int insertPositionCurrent(PositionCurrent positionCurrent);

    /**
     * 修改实时定位
     * 
     * @param positionCurrent 实时定位
     * @return 结果
     */
    public int updatePositionCurrent(PositionCurrent positionCurrent);

    /**
     * 批量删除实时定位
     * 
     * @param ids 需要删除的实时定位主键集合
     * @return 结果
     */
    public int deletePositionCurrentByIds(Long[] ids);

    /**
     * 删除实时定位信息
     * 
     * @param id 实时定位主键
     * @return 结果
     */
    public int deletePositionCurrentById(Long id);
}
