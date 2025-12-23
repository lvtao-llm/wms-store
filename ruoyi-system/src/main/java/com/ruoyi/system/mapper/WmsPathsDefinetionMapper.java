package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsPathsDefinetion;

/**
 * 虚拟路径点Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-19
 */
public interface WmsPathsDefinetionMapper 
{
    /**
     * 查询虚拟路径点
     * 
     * @param id 虚拟路径点主键
     * @return 虚拟路径点
     */
    public WmsPathsDefinetion selectWmsPathsDefinetionById(Long id);

    /**
     * 查询虚拟路径点列表
     * 
     * @param wmsPathsDefinetion 虚拟路径点
     * @return 虚拟路径点集合
     */
    public List<WmsPathsDefinetion> selectWmsPathsDefinetionList(WmsPathsDefinetion wmsPathsDefinetion);

    /**
     * 新增虚拟路径点
     * 
     * @param wmsPathsDefinetion 虚拟路径点
     * @return 结果
     */
    public int insertWmsPathsDefinetion(WmsPathsDefinetion wmsPathsDefinetion);

    /**
     * 修改虚拟路径点
     * 
     * @param wmsPathsDefinetion 虚拟路径点
     * @return 结果
     */
    public int updateWmsPathsDefinetion(WmsPathsDefinetion wmsPathsDefinetion);

    /**
     * 删除虚拟路径点
     * 
     * @param id 虚拟路径点主键
     * @return 结果
     */
    public int deleteWmsPathsDefinetionById(Long id);

    /**
     * 批量删除虚拟路径点
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsPathsDefinetionByIds(Long[] ids);
}
