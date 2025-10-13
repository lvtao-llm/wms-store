package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaCoreAlarmPeopleMapper;
import com.ruoyi.system.domain.LanyaCoreAlarmPeople;
import com.ruoyi.system.service.ILanyaCoreAlarmPeopleService;

/**
 * 人员报警Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@Service
public class LanyaCoreAlarmPeopleServiceImpl implements ILanyaCoreAlarmPeopleService 
{
    @Autowired
    private LanyaCoreAlarmPeopleMapper lanyaCoreAlarmPeopleMapper;

    /**
     * 查询人员报警
     * 
     * @param id 人员报警主键
     * @return 人员报警
     */
    @Override
    public LanyaCoreAlarmPeople selectLanyaCoreAlarmPeopleById(Long id)
    {
        return lanyaCoreAlarmPeopleMapper.selectLanyaCoreAlarmPeopleById(id);
    }

    /**
     * 查询人员报警列表
     * 
     * @param lanyaCoreAlarmPeople 人员报警
     * @return 人员报警
     */
    @Override
    public List<LanyaCoreAlarmPeople> selectLanyaCoreAlarmPeopleList(LanyaCoreAlarmPeople lanyaCoreAlarmPeople)
    {
        return lanyaCoreAlarmPeopleMapper.selectLanyaCoreAlarmPeopleList(lanyaCoreAlarmPeople);
    }

    /**
     * 新增人员报警
     * 
     * @param lanyaCoreAlarmPeople 人员报警
     * @return 结果
     */
    @Override
    public int insertLanyaCoreAlarmPeople(LanyaCoreAlarmPeople lanyaCoreAlarmPeople)
    {
        lanyaCoreAlarmPeople.setCreateTime(DateUtils.getNowDate());
        return lanyaCoreAlarmPeopleMapper.insertLanyaCoreAlarmPeople(lanyaCoreAlarmPeople);
    }

    /**
     * 修改人员报警
     * 
     * @param lanyaCoreAlarmPeople 人员报警
     * @return 结果
     */
    @Override
    public int updateLanyaCoreAlarmPeople(LanyaCoreAlarmPeople lanyaCoreAlarmPeople)
    {
        lanyaCoreAlarmPeople.setUpdateTime(DateUtils.getNowDate());
        return lanyaCoreAlarmPeopleMapper.updateLanyaCoreAlarmPeople(lanyaCoreAlarmPeople);
    }

    /**
     * 批量删除人员报警
     * 
     * @param ids 需要删除的人员报警主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreAlarmPeopleByIds(Long[] ids)
    {
        return lanyaCoreAlarmPeopleMapper.deleteLanyaCoreAlarmPeopleByIds(ids);
    }

    /**
     * 删除人员报警信息
     * 
     * @param id 人员报警主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreAlarmPeopleById(Long id)
    {
        return lanyaCoreAlarmPeopleMapper.deleteLanyaCoreAlarmPeopleById(id);
    }
}
