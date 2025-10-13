package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaDeviceCardMachineMapper;
import com.ruoyi.system.domain.LanyaDeviceCardMachine;
import com.ruoyi.system.service.ILanyaDeviceCardMachineService;

/**
 * 发卡机Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-12
 */
@Service
public class LanyaDeviceCardMachineServiceImpl implements ILanyaDeviceCardMachineService 
{
    @Autowired
    private LanyaDeviceCardMachineMapper lanyaDeviceCardMachineMapper;

    /**
     * 查询发卡机
     * 
     * @param cardSenderId 发卡机主键
     * @return 发卡机
     */
    @Override
    public LanyaDeviceCardMachine selectLanyaDeviceCardMachineByCardSenderId(Long cardSenderId)
    {
        return lanyaDeviceCardMachineMapper.selectLanyaDeviceCardMachineByCardSenderId(cardSenderId);
    }

    /**
     * 查询发卡机列表
     * 
     * @param lanyaDeviceCardMachine 发卡机
     * @return 发卡机
     */
    @Override
    public List<LanyaDeviceCardMachine> selectLanyaDeviceCardMachineList(LanyaDeviceCardMachine lanyaDeviceCardMachine)
    {
        return lanyaDeviceCardMachineMapper.selectLanyaDeviceCardMachineList(lanyaDeviceCardMachine);
    }

    /**
     * 新增发卡机
     * 
     * @param lanyaDeviceCardMachine 发卡机
     * @return 结果
     */
    @Override
    public int insertLanyaDeviceCardMachine(LanyaDeviceCardMachine lanyaDeviceCardMachine)
    {
        lanyaDeviceCardMachine.setCreateTime(DateUtils.getNowDate());
        return lanyaDeviceCardMachineMapper.insertLanyaDeviceCardMachine(lanyaDeviceCardMachine);
    }

    /**
     * 修改发卡机
     * 
     * @param lanyaDeviceCardMachine 发卡机
     * @return 结果
     */
    @Override
    public int updateLanyaDeviceCardMachine(LanyaDeviceCardMachine lanyaDeviceCardMachine)
    {
        lanyaDeviceCardMachine.setUpdateTime(DateUtils.getNowDate());
        return lanyaDeviceCardMachineMapper.updateLanyaDeviceCardMachine(lanyaDeviceCardMachine);
    }

    /**
     * 批量删除发卡机
     * 
     * @param cardSenderIds 需要删除的发卡机主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardMachineByCardSenderIds(Long[] cardSenderIds)
    {
        return lanyaDeviceCardMachineMapper.deleteLanyaDeviceCardMachineByCardSenderIds(cardSenderIds);
    }

    /**
     * 删除发卡机信息
     * 
     * @param cardSenderId 发卡机主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardMachineByCardSenderId(Long cardSenderId)
    {
        return lanyaDeviceCardMachineMapper.deleteLanyaDeviceCardMachineByCardSenderId(cardSenderId);
    }
}
