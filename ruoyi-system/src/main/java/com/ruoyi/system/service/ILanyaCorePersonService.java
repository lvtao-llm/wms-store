package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaCorePerson;

/**
 * 人员（员工/访客/承包商人员）Service接口
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
public interface ILanyaCorePersonService 
{
    /**
     * 查询人员（员工/访客/承包商人员）
     * 
     * @param personId 人员（员工/访客/承包商人员）主键
     * @return 人员（员工/访客/承包商人员）
     */
    public LanyaCorePerson selectLanyaCorePersonByPersonId(Long personId);

    /**
     * 查询人员（员工/访客/承包商人员）列表
     * 
     * @param lanyaCorePerson 人员（员工/访客/承包商人员）
     * @return 人员（员工/访客/承包商人员）集合
     */
    public List<LanyaCorePerson> selectLanyaCorePersonList(LanyaCorePerson lanyaCorePerson);

    /**
     * 新增人员（员工/访客/承包商人员）
     * 
     * @param lanyaCorePerson 人员（员工/访客/承包商人员）
     * @return 结果
     */
    public int insertLanyaCorePerson(LanyaCorePerson lanyaCorePerson);

    /**
     * 修改人员（员工/访客/承包商人员）
     * 
     * @param lanyaCorePerson 人员（员工/访客/承包商人员）
     * @return 结果
     */
    public int updateLanyaCorePerson(LanyaCorePerson lanyaCorePerson);

    /**
     * 批量删除人员（员工/访客/承包商人员）
     * 
     * @param personIds 需要删除的人员（员工/访客/承包商人员）主键集合
     * @return 结果
     */
    public int deleteLanyaCorePersonByPersonIds(Long[] personIds);

    /**
     * 删除人员（员工/访客/承包商人员）信息
     * 
     * @param personId 人员（员工/访客/承包商人员）主键
     * @return 结果
     */
    public int deleteLanyaCorePersonByPersonId(Long personId);
}
