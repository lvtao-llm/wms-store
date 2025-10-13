package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LanyaCoreArea;

/**
 * 区域信息Mapper接口
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
public interface LanyaCoreAreaMapper 
{
    /**
     * 查询区域信息
     * 
     * @param areaId 区域信息主键
     * @return 区域信息
     */
    public LanyaCoreArea selectLanyaCoreAreaByAreaId(Long areaId);

    /**
     * 查询区域信息列表
     * 
     * @param lanyaCoreArea 区域信息
     * @return 区域信息集合
     */
    public List<LanyaCoreArea> selectLanyaCoreAreaList(LanyaCoreArea lanyaCoreArea);

    /**
     * 新增区域信息
     * 
     * @param lanyaCoreArea 区域信息
     * @return 结果
     */
    public int insertLanyaCoreArea(LanyaCoreArea lanyaCoreArea);

    /**
     * 修改区域信息
     * 
     * @param lanyaCoreArea 区域信息
     * @return 结果
     */
    public int updateLanyaCoreArea(LanyaCoreArea lanyaCoreArea);

    /**
     * 删除区域信息
     * 
     * @param areaId 区域信息主键
     * @return 结果
     */
    public int deleteLanyaCoreAreaByAreaId(Long areaId);

    /**
     * 批量删除区域信息
     * 
     * @param areaIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanyaCoreAreaByAreaIds(Long[] areaIds);
}
