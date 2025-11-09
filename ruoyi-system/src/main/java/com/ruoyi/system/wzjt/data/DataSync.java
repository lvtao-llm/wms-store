package com.ruoyi.system.wzjt.data;

import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.WmsMaterialDescMapper;
import com.ruoyi.system.mapper.WzjtViewDbSkMapper;
import com.ruoyi.system.mapper.WzjtViewJlSkMapper;
import com.ruoyi.system.mapper.WzjtViewKcSkMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.service.impl.WmsMaterialDescServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/8
 */
@Service
public class DataSync {
    @Autowired
    private WzjtViewDbSkMapper wzjtViewDbSkMapper;

    @Autowired
    private WzjtViewJlSkMapper wzjtViewJlSkMapper;

    @Autowired
    private WzjtViewKcSkMapper wzjtViewKcSkMapper;

    @Autowired
    private IWmsMaterialInService wmsMaterialInService;

    @Autowired
    private IWmsMaterialOutService wmsMaterialOutService;

    @Autowired
    private IWmsMaterialStockService wmsMaterialStockService;

    @Autowired
    private IWmsMaterialDescService wmsMaterialDescService;

    String jlOffsetKey = "wzjt.jl.sync.offset";
    String dbOffsetKey = "wzjt.db.sync.offset";
    String kcOffsetKey = "wzjt.kc.sync.offset";
    SysConfig jlConfig = new SysConfig(jlOffsetKey);
    SysConfig dbConfig = new SysConfig(dbOffsetKey);
    SysConfig kcConfig = new SysConfig(kcOffsetKey);

    @Autowired
    private ISysConfigService configService;

    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @PostConstruct
    public void init() throws Exception {
        List<SysConfig> sysConfigs = configService.selectConfigList(new SysConfig());
        for (SysConfig sysConfig : sysConfigs) {
            if (sysConfig.getConfigKey().equals(jlOffsetKey)) {
                jlConfig = sysConfig;
            }
            if (sysConfig.getConfigKey().equals(dbOffsetKey)) {
                dbConfig = sysConfig;
            }
            if (sysConfig.getConfigKey().equals(kcOffsetKey)) {
                kcConfig = sysConfig;
            }
        }
    }

    @Scheduled(cron = "* * 0/1 * * ?")
    public void syncJLData() throws ParseException {
        Date jlOffsetDate = sdfDateTime.parse(jlConfig.getConfigValue());

        // 是否继续同步
        boolean continueGet = true;

        // 同步position_history表数据
        while (continueGet) {
            WmsMaterialIn viewJlSk = new WmsMaterialIn();
            viewJlSk.setJlsjT(jlOffsetDate);
            List<WmsMaterialIn> wzjtViewDbSks = wzjtViewJlSkMapper.selectViewJlSkList(viewJlSk);
            for (WmsMaterialIn w : wzjtViewDbSks) {
                wmsMaterialInService.insertWmsMaterialIn(w);
                w.setCreateTime(new Date());
                wmsMaterialDescService.insertNewMaterialDesc(w);
                jlOffsetDate = w.getJlsjT();
                jlConfig.setConfigValue(sdfDateTime.format(jlOffsetDate));
                configService.updateConfig(jlConfig);
            }
            // 检查是否继续获取数据
            continueGet = !wzjtViewDbSks.isEmpty();
        }
    }

    @Scheduled(cron = "* * 0/1 * * ?")
    public void syncDBData() throws ParseException {
        Date dbOffsetDate = sdfDateTime.parse(dbConfig.getConfigValue());

        // 是否继续同步
        boolean continueGet = true;

        // 同步position_history表数据
        while (continueGet) {
            WmsMaterialOut viewDbSk = new WmsMaterialOut();
            viewDbSk.setOutboundTime(dbOffsetDate);
            List<WmsMaterialOut> wzjtViewDbSks = wzjtViewDbSkMapper.selectViewDbSkList(viewDbSk);
            for (WmsMaterialOut w : wzjtViewDbSks) {
                wmsMaterialOutService.insertWmsMaterialOut(w);

                dbOffsetDate = w.getOutboundTime();
                dbConfig.setConfigValue(sdfDateTime.format(dbOffsetDate));
                configService.updateConfig(dbConfig);
            }
            // 检查是否继续获取数据
            continueGet = !wzjtViewDbSks.isEmpty();
        }
    }

    @Scheduled(cron = "* * 0/1 * * ?")
    public void syncKCData() {
        WmsMaterialStock wzjtViewDbSk = new WmsMaterialStock();
        List<WmsMaterialStock> wzjtViewDbSks = wzjtViewKcSkMapper.selectViewKcSkList(wzjtViewDbSk);
        int count = wmsMaterialStockService.deleteWmsMaterialStockAll();
        for (WmsMaterialStock w : wzjtViewDbSks) {
            wmsMaterialStockService.insertWmsMaterialStock(w);
        }
    }
}
