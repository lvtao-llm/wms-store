package com.ruoyi.system.wzjt.data;

import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.WzjtViewDbSkMapper;
import com.ruoyi.system.mapper.WzjtViewJlSkMapper;
import com.ruoyi.system.mapper.WzjtViewKcSkMapper;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 吕涛
 * 物资公司进出存数据同步
 * @version 1.0
 * @since 2025/11/8
 */
@Service
public class DataSync {

    /**
     * 物资公司调拨数据同步
     */
    @Autowired
    private WzjtViewDbSkMapper wzjtViewDbSkMapper;

    /**
     * 物资公司接料数据同步
     */
    @Autowired
    private WzjtViewJlSkMapper wzjtViewJlSkMapper;

    /**
     * 物资公司库存数据同步
     */
    @Autowired
    private WzjtViewKcSkMapper wzjtViewKcSkMapper;

    /**
     * 大仓入库数据服务
     */
    @Autowired
    private IWmsMaterialInService wmsMaterialInService;

    /**
     * 大仓出库数据服务
     */
    @Autowired
    private IWmsMaterialOutService wmsMaterialOutService;

    /**
     * 大仓库存数据服务
     */
    @Autowired
    private IWmsMaterialStockService wmsMaterialStockService;

    /**
     * 大仓物资描述服务
     */
    @Autowired
    private IWmsMaterialDescService wmsMaterialDescService;

    /**
     * 大仓物资日统计服务
     */
    @Autowired
    private IWmsMaterialStaticsDayService wmsMaterialStaticsDayService;

    /**
     * 大仓区域服务
     */
    @Autowired
    private IWmsAreaService wmsAreaService;

    /**
     * 不同偏移量默认值
     */
    String jlOffsetKey = "wzjt.jl.sync.offset";
    String dbOffsetKey = "wzjt.db.sync.offset";
    String kcOffsetKey = "wzjt.kc.sync.offset";
    SysConfig jlConfig = new SysConfig(jlOffsetKey);
    SysConfig dbConfig = new SysConfig(dbOffsetKey);
    SysConfig kcConfig = new SysConfig(kcOffsetKey);

    /**
     * 系统配置服务
     */
    @Autowired
    private ISysConfigService configService;

    /**
     * 时间格式
     */
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 时间格式
     */
    SimpleDateFormat sdfYearMonDay = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 接料同步是否启用
     */
    @Value("${wzgs.jl.sync.enabled:false}")
    private boolean jlSyncEnable;

    /**
     * 调拨同步是否启用
     */
    @Value("${wzgs.db.sync.enabled:false}")
    private boolean dbSyncEnable;

    /**
     * 库存同步是否启用
     */
    @Value("${wzgs.kc.sync.enabled:false}")
    private boolean kcSyncEnable;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() throws Exception {
        List<SysConfig> sysConfigs = configService.selectConfigList(new SysConfig());
        for (SysConfig sysConfig : sysConfigs) {
            if (sysConfig.getConfigKey().equals(jlOffsetKey)) {
                // 初始化接料偏移量
                jlConfig = sysConfig;
            }
            if (sysConfig.getConfigKey().equals(dbOffsetKey)) {
                // 初始化调拨偏移量
                dbConfig = sysConfig;
            }
            if (sysConfig.getConfigKey().equals(kcOffsetKey)) {
                // 初始化库存偏移量
                kcConfig = sysConfig;
            }
        }
    }

    /**
     * 同步接料数据
     */
    public void syncJLData() throws ParseException {
        if (!jlSyncEnable) {
            return;
        }

        // 接料时间偏移量
        Date jlOffsetDate = sdfDateTime.parse(jlConfig.getConfigValue());

        // 是否继续同步
        boolean continueGet = true;

        // 同步position_history表数据
        while (continueGet) {

            // 查询参数
            WmsMaterialIn viewJlSk = new WmsMaterialIn();
            viewJlSk.setJlsjT(jlOffsetDate);

            // 查询新的接料数据
            List<WmsMaterialIn> wzjtViewDbSks = wzjtViewJlSkMapper.selectViewJlSkList(viewJlSk);

            // 遍历接料数据
            for (WmsMaterialIn w : wzjtViewDbSks) {

                // 插入到大仓接料数据表
                wmsMaterialInService.insertWmsMaterialIn(w);

                // 插入到大仓物资描述
                w.setCreateTime(new Date());
                wmsMaterialDescService.insertNewMaterialDesc(w);

                // 当前数据的时间offset
                jlOffsetDate = w.getJlsjT();
                jlConfig.setConfigValue(sdfDateTime.format(jlOffsetDate));
                configService.updateConfig(jlConfig);

                // 插入到大仓物资日统计
                WmsMaterialStaticsDay wmsMaterialStaticsDay = new WmsMaterialStaticsDay();
                wmsMaterialStaticsDay.setDay(sdfYearMonDay.format(w.getJlsjT()));
                wmsMaterialStaticsDay.setWzbm(w.getWzbm());
                wmsMaterialStaticsDay.setWzmc(w.getWzmc());
                wmsMaterialStaticsDay.setWzlb(w.getWzlb());
                wmsMaterialStaticsDay.setJldw(w.getJldw());
                wmsMaterialStaticsDay.setJl(w.getDhsl());
                wmsMaterialStaticsDay.setAreaCodes(getAreaName(w));
                wmsMaterialStaticsDayService.updateWmsMaterialStaticsDay(wmsMaterialStaticsDay);
            }
            // 检查是否继续获取数据
            continueGet = !wzjtViewDbSks.isEmpty();
        }
    }

    /**
     * 同步调拨数据
     */
    public void syncDBData() throws ParseException {
        if (!dbSyncEnable) {
            return;
        }

        // 调拨时间偏移量
        Date dbOffsetDate = sdfDateTime.parse(dbConfig.getConfigValue());

        // 是否继续同步
        boolean continueGet = true;

        // 同步position_history表数据
        while (continueGet) {

            // 查询参数
            WmsMaterialOut viewDbSk = new WmsMaterialOut();
            viewDbSk.setOutboundTime(dbOffsetDate);

            // 获取新的调拨数据
            List<WmsMaterialOut> wzjtViewDbSks = wzjtViewDbSkMapper.selectViewDbSkList(viewDbSk);

            // 遍历调拨数据
            for (WmsMaterialOut w : wzjtViewDbSks) {

                // 插入到大仓出库数据表
                wmsMaterialOutService.insertWmsMaterialOut(w);

                // 当前数据的时间offset
                dbOffsetDate = w.getOutboundTime();
                dbConfig.setConfigValue(sdfDateTime.format(dbOffsetDate));
                configService.updateConfig(dbConfig);

                // 插入到大仓物资日统计
                WmsMaterialStaticsDay wmsMaterialStaticsDay = new WmsMaterialStaticsDay();
                wmsMaterialStaticsDay.setDay(sdfYearMonDay.format(w.getOutboundTime()));
                wmsMaterialStaticsDay.setWzbm(w.getWzbm());
                wmsMaterialStaticsDay.setWzmc(w.getWzmc());
                wmsMaterialStaticsDay.setJldw(w.getJldw());
                wmsMaterialStaticsDay.setDb(w.getAllotQuantity());
                wmsMaterialStaticsDay.setAreaCodes(getAreaName(w));
                wmsMaterialStaticsDayService.updateWmsMaterialStaticsDay(wmsMaterialStaticsDay);
            }
            // 检查是否继续获取数据
            continueGet = !wzjtViewDbSks.isEmpty();
        }
    }

    /**
     * 同步库存数据
     */
    public void syncKCData() {
        if (!kcSyncEnable) {
            return;
        }

        // 查询参数
        WmsMaterialStock wzjtViewDbSk = new WmsMaterialStock();

        // 获取新的库存数据
        List<WmsMaterialStock> wzjtViewDbSks = wzjtViewKcSkMapper.selectViewKcSkList(wzjtViewDbSk);

        // 删除大仓库存数据
        int count = wmsMaterialStockService.deleteWmsMaterialStockAll();

        // 遍历库存数据
        for (WmsMaterialStock w : wzjtViewDbSks) {

            // 插入到大仓库存数据表
            wmsMaterialStockService.insertWmsMaterialStock(w);

            // 插入到大仓物资日统计
            WmsMaterialStaticsDay wmsMaterialStaticsDay = new WmsMaterialStaticsDay();
            wmsMaterialStaticsDay.setDay(sdfYearMonDay.format(new Date()));
            wmsMaterialStaticsDay.setWzbm(w.getWzbm());
            wmsMaterialStaticsDay.setWzmc(w.getWzmc());
            wmsMaterialStaticsDay.setJldw(w.getJldw());
            wmsMaterialStaticsDay.setKc(w.getActualWeight());
            wmsMaterialStaticsDay.setAreaCodes(getAreaName(w));
            wmsMaterialStaticsDayService.updateWmsMaterialStaticsDay(wmsMaterialStaticsDay);
        }
    }

    /**
     * 获取大仓区域名称
     *
     * @param wz 物资描述
     * @return
     */
    private String getAreaName(WmsMaterialDesc wz) {

        // 查询物资描述
        WmsMaterialDesc wmsMaterialDesc = wmsMaterialDescService.getMaterialDescMap().get(wz.getWzbm());

        // 如果找不到，则新建物资描述
        if (wmsMaterialDesc == null) {
            wmsMaterialDesc = new WmsMaterialDesc();
            wmsMaterialDesc.setWzbm(wz.getWzbm());
            wmsMaterialDesc.setWzmc(wz.getWzmc());
            wmsMaterialDesc.setWzlb(wz.getWzlb());
            wmsMaterialDesc.setJldw(wz.getJldw());
            wmsMaterialDesc.setPzmc(wz.getPzmc());

            // 插入到大仓物资描述
            wmsMaterialDescService.insertNewMaterialDesc(wmsMaterialDesc);

            // 重新获取大仓物资描述
            wmsMaterialDesc = wmsMaterialDescService.getMaterialDescMap().get(wz.getWzbm());
        }

        // 大仓区域名称列表
        List<String> areaCodes = new ArrayList<>();

        // 获取大仓区域IDs
        if (wmsMaterialDesc.getAreaCodes() != null && !wmsMaterialDesc.getAreaCodes().isEmpty()) {

            // 拆分IDs到Array
            String[] codes = wmsMaterialDesc.getAreaCodes().split(",");

            // 遍历IDs
            for (String code : codes) {

                // 获取大仓区域
                WmsArea wmsArea = wmsAreaService.getAreaMap().get(Long.parseLong(code));
                if (wmsArea != null) {
                    // 添加大仓区域名称
                    areaCodes.add(wmsArea.getAreaName());
                }
            }
        }
        return String.join(",", areaCodes);
    }
}
