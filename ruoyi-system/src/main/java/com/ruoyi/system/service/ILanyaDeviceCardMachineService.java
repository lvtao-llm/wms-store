package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaDeviceCardMachine;

/**
 * 发卡机Service接口
 * 
 * @author ruoyi
 * @date 2025-10-12
 */
public interface ILanyaDeviceCardMachineService 
{
    /**
     * 查询发卡机
     * 
     * @param cardSenderId 发卡机主键
     * @return 发卡机
     */
    public LanyaDeviceCardMachine selectLanyaDeviceCardMachineByCardSenderId(Long cardSenderId);

    /**
     * 查询发卡机列表
     * 
     * @param lanyaDeviceCardMachine 发卡机
     * @return 发卡机集合
     */
    public List<LanyaDeviceCardMachine> selectLanyaDeviceCardMachineList(LanyaDeviceCardMachine lanyaDeviceCardMachine);

    /**
     * 新增发卡机
     * 
     * @param lanyaDeviceCardMachine 发卡机
     * @return 结果
     */
    public int insertLanyaDeviceCardMachine(LanyaDeviceCardMachine lanyaDeviceCardMachine);

    /**
     * 修改发卡机
     * 
     * @param lanyaDeviceCardMachine 发卡机
     * @return 结果
     */
    public int updateLanyaDeviceCardMachine(LanyaDeviceCardMachine lanyaDeviceCardMachine);

    /**
     * 批量删除发卡机
     * 
     * @param cardSenderIds 需要删除的发卡机主键集合
     * @return 结果
     */
    public int deleteLanyaDeviceCardMachineByCardSenderIds(Long[] cardSenderIds);

    /**
     * 删除发卡机信息
     * 
     * @param cardSenderId 发卡机主键
     * @return 结果
     */
    public int deleteLanyaDeviceCardMachineByCardSenderId(Long cardSenderId);
}
