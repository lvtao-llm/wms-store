package com.ruoyi.system.wzgs.sync;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsCardContentSend;
import com.ruoyi.system.mapper.WmsCardContentSendMapper;
import com.ruoyi.system.utils.HttpUtil;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/14
 */
@Component
public class GoPublicSync {
    private static final Logger log = LoggerFactory.getLogger("wzgs-sync");
    /**
     * httpUtil
     */
    @Autowired
    HttpUtil httpUtil;

    /**
     * 卡信息播报
     */
    @Autowired
    WmsCardContentSendMapper wmsCardContentSendMapper;

    @Value("${lanya.card-message.sync-api.api-url:http://112.98.110.101:10030/system/wms_card_content_send}")
    private String card_msgSyncUrl;

    @Value("${lanya.card-message-broadcast.sync.enabled:false}")
    private boolean card_msgSyncEnabled;

    @PostConstruct
    public void init() {
        log.info("同步物资公司定位卡信息播报开关：{}", card_msgSyncEnabled);
        log.info("同步物资公司定位卡信息播报URL:{}", card_msgSyncUrl);
    }

    /**
     * 卡信息播报同步
     *
     * @throws IOException
     */
    public void CardMessageBroadCase() throws IOException {
        if (!card_msgSyncEnabled) {
            return;
        }
        log.info("开始同步物资公司定位卡信息播报");
        List<WmsCardContentSend> wmsCardContentSends = wmsCardContentSendMapper.selectWmsCardContentNew();
        log.info("待同步的卡信息播报数量：{}", wmsCardContentSends.size());
        for (WmsCardContentSend wmsCardContentSend : wmsCardContentSends) {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(wmsCardContentSend);
            try (CloseableHttpResponse response = httpUtil.executePost(this.card_msgSyncUrl, jsonObject)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    wmsCardContentSendMapper.deleteWmsCardContentSendById(wmsCardContentSend.getId());
                }
            }
        }
        log.info("定位卡信息播报同步结束");
    }
}
