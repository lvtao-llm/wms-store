package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsDeviceMapper;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.service.IWmsDeviceService;

/**
 * 设备Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-27
 */
@Service
public class WmsDeviceServiceImpl implements IWmsDeviceService 
{
    @Autowired
    private WmsDeviceMapper wmsDeviceMapper;

    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    @Override
    public WmsDevice selectWmsDeviceById(Long id)
    {
        return wmsDeviceMapper.selectWmsDeviceById(id);
    }

    /**
     * 查询设备列表
     * 
     * @param wmsDevice 设备
     * @return 设备
     */
    @Override
    public List<WmsDevice> selectWmsDeviceList(WmsDevice wmsDevice)
    {
        return wmsDeviceMapper.selectWmsDeviceList(wmsDevice);
    }

    /**
     * 新增设备
     * 
     * @param wmsDevice 设备
     * @return 结果
     */
    @Override
    public int insertWmsDevice(WmsDevice wmsDevice)
    {
        wmsDevice.setCreateTime(DateUtils.getNowDate());
        return wmsDeviceMapper.insertWmsDevice(wmsDevice);
    }

    /**
     * 修改设备
     * 
     * @param wmsDevice 设备
     * @return 结果
     */
    @Override
    public int updateWmsDevice(WmsDevice wmsDevice)
    {
        wmsDevice.setUpdateTime(DateUtils.getNowDate());
        return wmsDeviceMapper.updateWmsDevice(wmsDevice);
    }

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteWmsDeviceByIds(Long[] ids)
    {
        return wmsDeviceMapper.deleteWmsDeviceByIds(ids);
    }

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    @Override
    public int deleteWmsDeviceById(Long id)
    {
        return wmsDeviceMapper.deleteWmsDeviceById(id);
    }

    @Override
    public WmsDevice selectWmsDeviceByIp(String ip) {
        return wmsDeviceMapper.selectWmsDeviceByIp(ip);
    }
}
