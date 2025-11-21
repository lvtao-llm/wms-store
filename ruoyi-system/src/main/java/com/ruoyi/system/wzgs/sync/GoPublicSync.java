package com.ruoyi.system.wzgs.sync;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsCardContentSend;
import com.ruoyi.system.mapper.WmsCardContentSendMapper;
import com.ruoyi.system.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private String apiUrl;

    /**
     * 卡信息播报同步
     *
     * @throws IOException
     */
    public void CardMessageBroadCase() throws IOException {
        List<WmsCardContentSend> wmsCardContentSends = wmsCardContentSendMapper.selectWmsCardContentNew();
        for (WmsCardContentSend wmsCardContentSend : wmsCardContentSends) {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(wmsCardContentSend);
            httpUtil.executePost(this.apiUrl, jsonObject);
        }
    }
}
