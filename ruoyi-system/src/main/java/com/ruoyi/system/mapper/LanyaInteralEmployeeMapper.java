package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LanyaInteralEmployee;

/**
 * 内部员工Mapper接口
 * 
 * @author 吕涛
 * @date 2025-10-12
 */
public interface LanyaInteralEmployeeMapper 
{
    /**
     * 查询内部员工
     * 
     * @param personId 内部员工主键
     * @return 内部员工
     */
    public LanyaInteralEmployee selectLanyaInteralEmployeeByPersonId(Long personId);

    /**
     * 查询内部员工列表
     * 
     * @param lanyaInteralEmployee 内部员工
     * @return 内部员工集合
     */
    public List<LanyaInteralEmployee> selectLanyaInteralEmployeeList(LanyaInteralEmployee lanyaInteralEmployee);

    /**
     * 新增内部员工
     * 
     * @param lanyaInteralEmployee 内部员工
     * @return 结果
     */
    public int insertLanyaInteralEmployee(LanyaInteralEmployee lanyaInteralEmployee);

    /**
     * 修改内部员工
     * 
     * @param lanyaInteralEmployee 内部员工
     * @return 结果
     */
    public int updateLanyaInteralEmployee(LanyaInteralEmployee lanyaInteralEmployee);

    /**
     * 删除内部员工
     * 
     * @param personId 内部员工主键
     * @return 结果
     */
    public int deleteLanyaInteralEmployeeByPersonId(Long personId);

    /**
     * 批量删除内部员工
     * 
     * @param personIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanyaInteralEmployeeByPersonIds(Long[] personIds);
}
