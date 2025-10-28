package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsDevice;

/**
 * 设备Service接口
 * 
 * @author ruoyi
 * @date 2025-10-27
 */
public interface IWmsDeviceService 
{
    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    public WmsDevice selectWmsDeviceById(Long id);

    /**
     * 查询设备列表
     * 
     * @param wmsDevice 设备
     * @return 设备集合
     */
    public List<WmsDevice> selectWmsDeviceList(WmsDevice wmsDevice);

    /**
     * 新增设备
     * 
     * @param wmsDevice 设备
     * @return 结果
     */
    public int insertWmsDevice(WmsDevice wmsDevice);

    /**
     * 修改设备
     * 
     * @param wmsDevice 设备
     * @return 结果
     */
    public int updateWmsDevice(WmsDevice wmsDevice);

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键集合
     * @return 结果
     */
    public int deleteWmsDeviceByIds(Long[] ids);

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    public int deleteWmsDeviceById(Long id);
}
