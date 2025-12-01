package com.ruoyi.system.wzgs.sync;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.WzjtViewDbSkMapper;
import com.ruoyi.system.mapper.WzjtViewJlSkMapper;
import com.ruoyi.system.mapper.WzjtViewKcSkMapper;
import com.ruoyi.system.mapper.WzjtViewYySkMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.HttpUtil;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class StoreDataSync implements ResourceTransformer {

    private static final Logger log = LoggerFactory.getLogger("wzgs-sync");

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
     * 调拨照片同步是否启用
     */
    @Value("${wzgs.db.sync.enabled-file:false}")
    private boolean fileSyncEnable;

    /**
     * httpUtil
     */
    @Autowired
    HttpUtil httpUtil;

    @Value("${wzgs.db.sync.file-api-url:http://localhost:10050/download/resource?resource=}")
    private String fileApiUrl;

    private CloseableHttpClient httpClient;

    private String dbFilePath;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() throws Exception {
        if (RuoYiConfig.getProfile() == null) {
            log.error("StoreDataSync初始化-未设置profile");
            fileSyncEnable = false;
        } else {
            dbFilePath = Paths.get(RuoYiConfig.getProfile(), "skpic").toFile().getPath();
        }
        log.info("StoreDataSync初始化-本地调拨文件存储路径:{}", dbFilePath);
        log.info("StoreDataSync初始化-启用调拨文件同步开关:{}", fileSyncEnable);
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

        if (fileSyncEnable) {
            scheduler.scheduleAtFixedRate(this::syncDbFile, 0, 1, TimeUnit.SECONDS);
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

                String imagePath = w.getImagePath();
                if (!Strings.isEmpty(imagePath)) {
                    w.setImagePath(Paths.get(dbFilePath, w.getImagePath().replace("d:/skpic/", "")).toFile().getPath());
                }
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

                if (!Strings.isEmpty(w.getAllotId())) {
                    WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue = new WmsMaterialOutFileSyncQueue();
                    wmsMaterialOutFileSyncQueue.set调拨明细编号(w.getAllotId());
                    if (!Strings.isEmpty(w.getMeasureImageFile()) && !Strings.isEmpty(imagePath)) {
                        for (String file : w.getMeasureImageFile().split(",")) {
                            if (!Strings.isEmpty(file)) {
                                wmsMaterialOutFileSyncQueue.set字段名称("计量图片文件");
                                wmsMaterialOutFileSyncQueue.set文件路径(Paths.get(imagePath, file).toFile().getPath());
                                wmsMaterialOutFileSyncQueueService.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
                            }
                        }
                    }

                    if (!Strings.isEmpty(w.getTareImageFile()) && !Strings.isEmpty(imagePath)) {
                        for (String file : w.getTareImageFile().split(",")) {
                            if (!Strings.isEmpty(file)) {
                                wmsMaterialOutFileSyncQueue.set字段名称("皮重图片文件");
                                wmsMaterialOutFileSyncQueue.set文件路径(Paths.get(imagePath, file).toFile().getPath());
                                wmsMaterialOutFileSyncQueueService.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue);
                            }
                        }
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

        log.info("开始同步库存数据...");
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

        log.info("开始同步车辆预约数据...");

        // 获取新的预约数据
        List<WmsVehicleGatepass> appointments = wzjtViewYySkMapper.selectViewYySkList();

        log.info("获取车辆预约数据完成，共{}条数据", appointments.size());

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
        log.info("同步车辆预约数据完成");
    }

    private void syncDbFile() {

        log.info("开始同步调拨文件数据...");
        List<WmsMaterialOutFileSyncQueue> wmsMaterialOutFileSyncQueues = wmsMaterialOutFileSyncQueueService.selectWmsMaterialOutFileSyncQueueListByCount(10);
        log.info("获取调拨文件数据完成，共{}条数据", wmsMaterialOutFileSyncQueues.size());
        for (WmsMaterialOutFileSyncQueue w : wmsMaterialOutFileSyncQueues) {
            if (Strings.isEmpty(w.get调拨明细编号()) || Strings.isEmpty(w.get文件路径())) {
                continue;
            }
            String url = (this.fileApiUrl + w.get文件路径()).replace("\\", "/");
            log.info("开始同步调拨文件数据,数据url:{}", url);
            HttpGet get = new HttpGet(url);
            get.addHeader("Accept", "image/jpeg,image/jpg,image/png,image/gif,*/*;q=0.8");
            get.addHeader("Accept-Encoding", "gzip, deflate");
            get.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
            get.addHeader("Cache-Control", "no-cache");
            get.addHeader("Connection", "keep-alive");
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                int statusCode = response.getStatusLine().getStatusCode();
                log.info("开始同步调拨文件数据,数据状态码:{}", statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    Path filePath = Paths.get(dbFilePath, w.get文件路径().replace("d:\\skpic\\", ""));
                    File file = filePath.toFile();
                    File parentFile = file.getParentFile();
                    log.info("开始同步调拨文件数据,文件保存路径:{}", filePath);
                    if (parentFile != null && !parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        response.getEntity().writeTo(fos);
                        log.info("同步调拨文件数据写入完成:{}", file);
                        wmsMaterialOutFileSyncQueueService.deleteWmsMaterialOutFileSyncQueueBy调拨明细编号(w.get调拨明细编号());
                    }
                } else {
                    throw new RuntimeException("HTTP Get 下载调拨相关文件失败: " + statusCode + "[ " + url + " ]");
                }
            } catch (Exception e) {
                log.error("下载文件失败: {}", e.getMessage(), e);
            }
        }
        log.info("同步调拨文件数据完成");
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
            wmsMaterialDesc.setAreaCodes("");

            // 插入到大仓物资描述
            wmsMaterialDescService.insertNewMaterialDesc(wmsMaterialDesc);

            // 重新获取大仓物资描述
            wmsMaterialDesc = wmsMaterialDescService.getMaterialDescMap().get(wz.getWzmc());
        }

        // 大仓区域名称列表
        List<String> areaCodes = new ArrayList<>();

        // 获取大仓区域IDs
        if (!Strings.isEmpty(wmsMaterialDesc.getAreaCodes())) {

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

    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        // 在这里可以对资源进行处理
        // 例如：添加水印、修改内容、记录日志等

        // 调用链中的下一个转换器
        Resource transformedResource = transformerChain.transform(request, resource);

        // 可以在这里添加后处理逻辑
        // 比如记录资源访问日志
        System.out.println("Accessing resource: " + resource.getFilename());

        return transformedResource;
    }
}
