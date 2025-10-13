package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LanyaCoreAlarmPeople;

/**
 * 人员报警Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public interface LanyaCoreAlarmPeopleMapper 
{
    /**
     * 查询人员报警
     * 
     * @param id 人员报警主键
     * @return 人员报警
     */
    public LanyaCoreAlarmPeople selectLanyaCoreAlarmPeopleById(Long id);

    /**
     * 查询人员报警列表
     * 
     * @param lanyaCoreAlarmPeople 人员报警
     * @return 人员报警集合
     */
    public List<LanyaCoreAlarmPeople> selectLanyaCoreAlarmPeopleList(LanyaCoreAlarmPeople lanyaCoreAlarmPeople);

    /**
     * 新增人员报警
     * 
     * @param lanyaCoreAlarmPeople 人员报警
     * @return 结果
     */
    public int insertLanyaCoreAlarmPeople(LanyaCoreAlarmPeople lanyaCoreAlarmPeople);

    /**
     * 修改人员报警
     * 
     * @param lanyaCoreAlarmPeople 人员报警
     * @return 结果
     */
    public int updateLanyaCoreAlarmPeople(LanyaCoreAlarmPeople lanyaCoreAlarmPeople);

    /**
     * 删除人员报警
     * 
     * @param id 人员报警主键
     * @return 结果
     */
    public int deleteLanyaCoreAlarmPeopleById(Long id);

    /**
     * 批量删除人员报警
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanyaCoreAlarmPeopleByIds(Long[] ids);
}
