package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsPathsDefinetionMapper;
import com.ruoyi.system.domain.WmsPathsDefinetion;
import com.ruoyi.system.service.IWmsPathsDefinetionService;

/**
 * 虚拟路径点Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-19
 */
@Service
public class WmsPathsDefinetionServiceImpl implements IWmsPathsDefinetionService 
{
    @Autowired
    private WmsPathsDefinetionMapper wmsPathsDefinetionMapper;

    /**
     * 查询虚拟路径点
     * 
     * @param id 虚拟路径点主键
     * @return 虚拟路径点
     */
    @Override
    public WmsPathsDefinetion selectWmsPathsDefinetionById(Long id)
    {
        return wmsPathsDefinetionMapper.selectWmsPathsDefinetionById(id);
    }

    /**
     * 查询虚拟路径点列表
     * 
     * @param wmsPathsDefinetion 虚拟路径点
     * @return 虚拟路径点
     */
    @Override
    public List<WmsPathsDefinetion> selectWmsPathsDefinetionList(WmsPathsDefinetion wmsPathsDefinetion)
    {
        return wmsPathsDefinetionMapper.selectWmsPathsDefinetionList(wmsPathsDefinetion);
    }

    /**
     * 新增虚拟路径点
     * 
     * @param wmsPathsDefinetion 虚拟路径点
     * @return 结果
     */
    @Override
    public int insertWmsPathsDefinetion(WmsPathsDefinetion wmsPathsDefinetion)
    {
        return wmsPathsDefinetionMapper.insertWmsPathsDefinetion(wmsPathsDefinetion);
    }

    /**
     * 修改虚拟路径点
     * 
     * @param wmsPathsDefinetion 虚拟路径点
     * @return 结果
     */
    @Override
    public int updateWmsPathsDefinetion(WmsPathsDefinetion wmsPathsDefinetion)
    {
        return wmsPathsDefinetionMapper.updateWmsPathsDefinetion(wmsPathsDefinetion);
    }

    /**
     * 批量删除虚拟路径点
     * 
     * @param ids 需要删除的虚拟路径点主键
     * @return 结果
     */
    @Override
    public int deleteWmsPathsDefinetionByIds(Long[] ids)
    {
        return wmsPathsDefinetionMapper.deleteWmsPathsDefinetionByIds(ids);
    }

    /**
     * 删除虚拟路径点信息
     * 
     * @param id 虚拟路径点主键
     * @return 结果
     */
    @Override
    public int deleteWmsPathsDefinetionById(Long id)
    {
        return wmsPathsDefinetionMapper.deleteWmsPathsDefinetionById(id);
    }
}
