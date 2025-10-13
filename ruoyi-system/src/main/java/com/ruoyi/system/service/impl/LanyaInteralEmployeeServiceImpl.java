package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaInteralEmployeeMapper;
import com.ruoyi.system.domain.LanyaInteralEmployee;
import com.ruoyi.system.service.ILanyaInteralEmployeeService;

/**
 * 内部员工Service业务层处理
 * 
 * @author 吕涛
 * @date 2025-10-12
 */
@Service
public class LanyaInteralEmployeeServiceImpl implements ILanyaInteralEmployeeService 
{
    @Autowired
    private LanyaInteralEmployeeMapper lanyaInteralEmployeeMapper;

    /**
     * 查询内部员工
     * 
     * @param personId 内部员工主键
     * @return 内部员工
     */
    @Override
    public LanyaInteralEmployee selectLanyaInteralEmployeeByPersonId(Long personId)
    {
        return lanyaInteralEmployeeMapper.selectLanyaInteralEmployeeByPersonId(personId);
    }

    /**
     * 查询内部员工列表
     * 
     * @param lanyaInteralEmployee 内部员工
     * @return 内部员工
     */
    @Override
    public List<LanyaInteralEmployee> selectLanyaInteralEmployeeList(LanyaInteralEmployee lanyaInteralEmployee)
    {
        return lanyaInteralEmployeeMapper.selectLanyaInteralEmployeeList(lanyaInteralEmployee);
    }

    /**
     * 新增内部员工
     * 
     * @param lanyaInteralEmployee 内部员工
     * @return 结果
     */
    @Override
    public int insertLanyaInteralEmployee(LanyaInteralEmployee lanyaInteralEmployee)
    {
        lanyaInteralEmployee.setCreateTime(DateUtils.getNowDate());
        return lanyaInteralEmployeeMapper.insertLanyaInteralEmployee(lanyaInteralEmployee);
    }

    /**
     * 修改内部员工
     * 
     * @param lanyaInteralEmployee 内部员工
     * @return 结果
     */
    @Override
    public int updateLanyaInteralEmployee(LanyaInteralEmployee lanyaInteralEmployee)
    {
        lanyaInteralEmployee.setUpdateTime(DateUtils.getNowDate());
        return lanyaInteralEmployeeMapper.updateLanyaInteralEmployee(lanyaInteralEmployee);
    }

    /**
     * 批量删除内部员工
     * 
     * @param personIds 需要删除的内部员工主键
     * @return 结果
     */
    @Override
    public int deleteLanyaInteralEmployeeByPersonIds(Long[] personIds)
    {
        return lanyaInteralEmployeeMapper.deleteLanyaInteralEmployeeByPersonIds(personIds);
    }

    /**
     * 删除内部员工信息
     * 
     * @param personId 内部员工主键
     * @return 结果
     */
    @Override
    public int deleteLanyaInteralEmployeeByPersonId(Long personId)
    {
        return lanyaInteralEmployeeMapper.deleteLanyaInteralEmployeeByPersonId(personId);
    }
}
