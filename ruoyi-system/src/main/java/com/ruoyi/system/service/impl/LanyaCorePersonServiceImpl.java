package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaCorePersonMapper;
import com.ruoyi.system.domain.LanyaCorePerson;
import com.ruoyi.system.service.ILanyaCorePersonService;

/**
 * 人员（员工/访客/承包商人员）Service业务层处理
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
@Service
public class LanyaCorePersonServiceImpl implements ILanyaCorePersonService 
{
    @Autowired
    private LanyaCorePersonMapper lanyaCorePersonMapper;

    /**
     * 查询人员（员工/访客/承包商人员）
     * 
     * @param personId 人员（员工/访客/承包商人员）主键
     * @return 人员（员工/访客/承包商人员）
     */
    @Override
    public LanyaCorePerson selectLanyaCorePersonByPersonId(Long personId)
    {
        return lanyaCorePersonMapper.selectLanyaCorePersonByPersonId(personId);
    }

    /**
     * 查询人员（员工/访客/承包商人员）列表
     * 
     * @param lanyaCorePerson 人员（员工/访客/承包商人员）
     * @return 人员（员工/访客/承包商人员）
     */
    @Override
    public List<LanyaCorePerson> selectLanyaCorePersonList(LanyaCorePerson lanyaCorePerson)
    {
        return lanyaCorePersonMapper.selectLanyaCorePersonList(lanyaCorePerson);
    }

    /**
     * 新增人员（员工/访客/承包商人员）
     * 
     * @param lanyaCorePerson 人员（员工/访客/承包商人员）
     * @return 结果
     */
    @Override
    public int insertLanyaCorePerson(LanyaCorePerson lanyaCorePerson)
    {
        lanyaCorePerson.setCreateTime(DateUtils.getNowDate());
        return lanyaCorePersonMapper.insertLanyaCorePerson(lanyaCorePerson);
    }

    /**
     * 修改人员（员工/访客/承包商人员）
     * 
     * @param lanyaCorePerson 人员（员工/访客/承包商人员）
     * @return 结果
     */
    @Override
    public int updateLanyaCorePerson(LanyaCorePerson lanyaCorePerson)
    {
        lanyaCorePerson.setUpdateTime(DateUtils.getNowDate());
        return lanyaCorePersonMapper.updateLanyaCorePerson(lanyaCorePerson);
    }

    /**
     * 批量删除人员（员工/访客/承包商人员）
     * 
     * @param personIds 需要删除的人员（员工/访客/承包商人员）主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCorePersonByPersonIds(Long[] personIds)
    {
        return lanyaCorePersonMapper.deleteLanyaCorePersonByPersonIds(personIds);
    }

    /**
     * 删除人员（员工/访客/承包商人员）信息
     * 
     * @param personId 人员（员工/访客/承包商人员）主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCorePersonByPersonId(Long personId)
    {
        return lanyaCorePersonMapper.deleteLanyaCorePersonByPersonId(personId);
    }
}
