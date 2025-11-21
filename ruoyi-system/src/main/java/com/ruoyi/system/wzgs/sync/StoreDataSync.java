package com.ruoyi.system.wzgs.sync;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.WzjtViewDbSkMapper;
import com.ruoyi.system.mapper.WzjtViewJlSkMapper;
import com.ruoyi.system.mapper.WzjtViewKcSkMapper;
import com.ruoyi.system.mapper.WzjtViewYySkMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 吕涛
 * 物资公司进出存数据同步
 * @version 1.0
 * @since 2025/11/8
 */
@Service
public class StoreDataSync {

    private static final Logger log = LoggerFactory.getLogger("camera-stream");

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
     * 物资公司车辆预约mapper
     */
    @Autowired
    private WzjtViewYySkMapper wzjtViewYySkMapper;

    /**
     * 车辆预约服务
     */
    @Autowired
    private IWmsVehicleGatepassService wmsVehicleGatepassService;

    /**
     * 大仓区域服务
     */
    @Autowired
    private IWmsAreaService wmsAreaService;

    @Autowired
    private IWmsMaterialOutFileSyncQueueService wmsMaterialOutFileSyncQueueService;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

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
     * 车辆预约同步是否启用
     */
    @Value("${wzgs.yy.sync.enabled:false}")
    private boolean yySyncEnable;

    /**
     * httpUtil
     */
    @Autowired
    HttpUtil httpUtil;

    @Value("${wzgs.db.sync.file-api-url:http://localhost:10050/download/resource?resource=}")
    private String fileApiUrl;
    private CloseableHttpClient httpClient;

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

        // 创建连接池管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(20);

        // 创建请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .build();

        // 创建HTTP客户端
        this.httpClient = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();

        scheduler.scheduleAtFixedRate(this::syncDbFile, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 同步接料数据
     */
    public void syncJLData() throws ParseException {
        if (!jlSyncEnable) {
            return;
        }

        // 接料时间偏移量
        String jlOffset = jlConfig.getConfigValue();
        Date jlOffsetDate = sdfDateTime.parse(jlOffset);

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
                wmsMaterialStaticsDay.setWzmc(w.getWzmc());
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
        String dbOffset = dbConfig.getConfigValue();
        Date dbOffsetDate = sdfDateTime.parse(dbOffset);

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
                wmsMaterialStaticsDay.setWzmc(w.getWzmc());
                wmsMaterialStaticsDay.setDb(w.getAllotQuantity());
                wmsMaterialStaticsDay.setAreaCodes(getAreaName(w));
                wmsMaterialStaticsDayService.updateWmsMaterialStaticsDay(wmsMaterialStaticsDay);

                if (!StringUtils.isEmpty(w.getWzbm())) {
                    WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue = new WmsMaterialOutFileSyncQueue();
                    wmsMaterialOutFileSyncQueue.set调拨明细编号(w.getAllotId());
                    if (w.getMeasureImageFile() != null) {
                        wmsMaterialOutFileSyncQueue.set字段名称("计量图片文件");
                        wmsMaterialOutFileSyncQueue.set文件路径(w.getMeasureImageFile());
                        wmsMaterialOutFileSyncQueueService.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
                    }

                    if (w.getMeasureVideoFile() != null) {
                        wmsMaterialOutFileSyncQueue.set字段名称("计量录像文件");
                        wmsMaterialOutFileSyncQueue.set文件路径(w.getMeasureVideoFile());
                        wmsMaterialOutFileSyncQueueService.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
                    }

                    if (w.getTareImageFile() != null) {
                        wmsMaterialOutFileSyncQueue.set字段名称("皮重图片文件");
                        wmsMaterialOutFileSyncQueue.set文件路径(w.getTareImageFile());
                        wmsMaterialOutFileSyncQueueService.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
                    }

                    if (w.getTareVideoFile() != null) {
                        wmsMaterialOutFileSyncQueue.set字段名称("皮重录像文件");
                        wmsMaterialOutFileSyncQueue.set文件路径(w.getTareVideoFile());
                        wmsMaterialOutFileSyncQueueService.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
                    }
                }
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
            wmsMaterialStaticsDay.setWzmc(w.getWzmc());
            wmsMaterialStaticsDay.setKc(w.getActualWeight());
            wmsMaterialStaticsDay.setAreaCodes(getAreaName(w));
            wmsMaterialStaticsDayService.updateWmsMaterialStaticsDay(wmsMaterialStaticsDay);
        }
    }

    /**
     * 同步车辆预约数据
     */
    public void syncYYData() {
        if (!yySyncEnable) {
            return;
        }

        SimpleDateFormat sdfTableSuffix = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdfTableSuffix.format(new Date());

        // 查询参数
        WmsMaterialStock wzjtViewDbSk = new WmsMaterialStock();

        // 获取新的库存数据
        List<WmsVehicleGatepass> appointments = wzjtViewYySkMapper.selectViewYySkList();

        // 删除大仓库存数据
        WmsVehicleGatepass query = new WmsVehicleGatepass();
        query.setGatepassAppointmentTime(ymd);
        List<WmsVehicleGatepass> wmsVehicleGatepasses = wmsVehicleGatepassService.selectWmsVehicleGatepassList(query);
        for (WmsVehicleGatepass w : wmsVehicleGatepasses) {
            wmsVehicleGatepassService.deleteWmsVehicleGatepassByGatepassId(w.getGatepassId());
        }

        // 遍历库存数据
        for (WmsVehicleGatepass w : appointments) {
            // 插入到大仓库存数据表
            w.setGatepassAppointmentTime(ymd);

            // 查询物资描述
            WmsMaterialDesc wmsMaterialDesc = wmsMaterialDescService.getMaterialDescMap().get(w.getMaterial());
            // 获取大仓区域IDs
            // 大仓区域名称列表
            List<String> areaCodes = new ArrayList<>();
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
            w.setAreaCodes(String.join(",", areaCodes));
            wmsVehicleGatepassService.insertWmsVehicleGatepass(w);
        }
    }

    private void syncDbFile() {
        PageUtils.startPage();
        WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue = new WmsMaterialOutFileSyncQueue();
        List<WmsMaterialOutFileSyncQueue> wmsMaterialOutFileSyncQueues = wmsMaterialOutFileSyncQueueService.selectWmsMaterialOutFileSyncQueueList(wmsMaterialOutFileSyncQueue);
        for (WmsMaterialOutFileSyncQueue w : wmsMaterialOutFileSyncQueues) {
            String url = this.fileApiUrl + w.get文件路径();
            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    String filePath = RuoYiConfig.getUploadPath();
                    File saveDir = new File(filePath);
                    try (FileOutputStream fos = new FileOutputStream(saveDir)) {
                        response.getEntity().writeTo(fos);
                        wmsMaterialOutFileSyncQueueService.deleteWmsMaterialOutFileSyncQueueBy调拨明细编号(w.get调拨明细编号());
                    }
                } else {
                    throw new RuntimeException("HTTP Get 下载调拨相关文件失败: " + statusCode + "[ " + url + " ]");
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
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
        WmsMaterialDesc wmsMaterialDesc = wmsMaterialDescService.getMaterialDescMap().get(wz.getWzmc());

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
            wmsMaterialDesc = wmsMaterialDescService.getMaterialDescMap().get(wz.getWzmc());
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
