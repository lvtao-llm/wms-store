package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsPerson;

/**
 * 人员档案Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface IWmsPersonService 
{
    /**
     * 查询人员档案
     * 
     * @param personId 人员档案主键
     * @return 人员档案
     */
    public WmsPerson selectWmsPersonByPersonId(Long personId);

    /**
     * 查询人员档案列表
     * 
     * @param wmsPerson 人员档案
     * @return 人员档案集合
     */
    public List<WmsPerson> selectWmsPersonList(WmsPerson wmsPerson);

    /**
     * 新增人员档案
     * 
     * @param wmsPerson 人员档案
     * @return 结果
     */
    public int insertWmsPerson(WmsPerson wmsPerson);

    /**
     * 修改人员档案
     * 
     * @param wmsPerson 人员档案
     * @return 结果
     */
    public int updateWmsPerson(WmsPerson wmsPerson);

    /**
     * 批量删除人员档案
     * 
     * @param personIds 需要删除的人员档案主键集合
     * @return 结果
     */
    public int deleteWmsPersonByPersonIds(Long[] personIds);

    /**
     * 删除人员档案信息
     * 
     * @param personId 人员档案主键
     * @return 结果
     */
    public int deleteWmsPersonByPersonId(Long personId);
}
