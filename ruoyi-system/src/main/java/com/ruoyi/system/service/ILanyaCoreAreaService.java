package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaCoreArea;

/**
 * 区域信息Service接口
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
public interface ILanyaCoreAreaService 
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
     * 批量删除区域信息
     * 
     * @param areaIds 需要删除的区域信息主键集合
     * @return 结果
     */
    public int deleteLanyaCoreAreaByAreaIds(Long[] areaIds);

    /**
     * 删除区域信息信息
     * 
     * @param areaId 区域信息主键
     * @return 结果
     */
    public int deleteLanyaCoreAreaByAreaId(Long areaId);
}
