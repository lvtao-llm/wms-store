package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PositionCurrent;

/**
 * 实时定位Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
public interface PositionCurrentMapper 
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
     * 删除实时定位
     * 
     * @param id 实时定位主键
     * @return 结果
     */
    public int deletePositionCurrentById(Long id);

    /**
     * 批量删除实时定位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePositionCurrentByIds(Long[] ids);
}
