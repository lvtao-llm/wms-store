package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LanyaPositionCurrent;

/**
 * 实时定位Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
public interface LanyaPositionCurrentMapper 
{
    /**
     * 查询实时定位
     * 
     * @param id 实时定位主键
     * @return 实时定位
     */
    public LanyaPositionCurrent selectLanyaPositionCurrentById(Long id);

    /**
     * 查询实时定位列表
     * 
     * @param lanyaPositionCurrent 实时定位
     * @return 实时定位集合
     */
    public List<LanyaPositionCurrent> selectLanyaPositionCurrentList(LanyaPositionCurrent lanyaPositionCurrent);

    /**
     * 新增实时定位
     * 
     * @param lanyaPositionCurrent 实时定位
     * @return 结果
     */
    public int insertLanyaPositionCurrent(LanyaPositionCurrent lanyaPositionCurrent);

    /**
     * 修改实时定位
     * 
     * @param lanyaPositionCurrent 实时定位
     * @return 结果
     */
    public int updateLanyaPositionCurrent(LanyaPositionCurrent lanyaPositionCurrent);

    /**
     * 删除实时定位
     * 
     * @param id 实时定位主键
     * @return 结果
     */
    public int deleteLanyaPositionCurrentById(Long id);

    /**
     * 批量删除实时定位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanyaPositionCurrentByIds(Long[] ids);

    int deleteLanyaPositionCurrentAll();
}
