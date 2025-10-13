package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsPersonMapper;
import com.ruoyi.system.domain.WmsPerson;
import com.ruoyi.system.service.IWmsPersonService;

/**
 * 人员档案Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsPersonServiceImpl implements IWmsPersonService 
{
    @Autowired
    private WmsPersonMapper wmsPersonMapper;

    /**
     * 查询人员档案
     * 
     * @param personId 人员档案主键
     * @return 人员档案
     */
    @Override
    public WmsPerson selectWmsPersonByPersonId(Long personId)
    {
        return wmsPersonMapper.selectWmsPersonByPersonId(personId);
    }

    /**
     * 查询人员档案列表
     * 
     * @param wmsPerson 人员档案
     * @return 人员档案
     */
    @Override
    public List<WmsPerson> selectWmsPersonList(WmsPerson wmsPerson)
    {
        return wmsPersonMapper.selectWmsPersonList(wmsPerson);
    }

    /**
     * 新增人员档案
     * 
     * @param wmsPerson 人员档案
     * @return 结果
     */
    @Override
    public int insertWmsPerson(WmsPerson wmsPerson)
    {
        wmsPerson.setCreateTime(DateUtils.getNowDate());
        return wmsPersonMapper.insertWmsPerson(wmsPerson);
    }

    /**
     * 修改人员档案
     * 
     * @param wmsPerson 人员档案
     * @return 结果
     */
    @Override
    public int updateWmsPerson(WmsPerson wmsPerson)
    {
        wmsPerson.setUpdateTime(DateUtils.getNowDate());
        return wmsPersonMapper.updateWmsPerson(wmsPerson);
    }

    /**
     * 批量删除人员档案
     * 
     * @param personIds 需要删除的人员档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsPersonByPersonIds(Long[] personIds)
    {
        return wmsPersonMapper.deleteWmsPersonByPersonIds(personIds);
    }

    /**
     * 删除人员档案信息
     * 
     * @param personId 人员档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsPersonByPersonId(Long personId)
    {
        return wmsPersonMapper.deleteWmsPersonByPersonId(personId);
    }
}
