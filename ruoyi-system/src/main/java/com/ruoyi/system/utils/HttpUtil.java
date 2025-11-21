package com.ruoyi.system.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/14
 */
@Component
public class HttpUtil {

    /**
     * HTTP客户端
     */
    public CloseableHttpClient httpClient;

    @PostConstruct
    public void init() {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(20);

        // 创建请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .setProxy(new HttpHost("proxy2.dq.petrochina", 8080))
                .build();

        // 创建HTTP客户端
        this.httpClient = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();

    }

    public CloseableHttpResponse executeGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate");
        httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.addHeader("Cache-Control", "max-age=0");
        httpGet.addHeader("Host", "112.98.110.101:10030");
        httpGet.addHeader("proxy-connection", "keep-alive");
        httpGet.addHeader("upgrade-insecure-requests", "1");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");
        return httpClient.execute(httpGet);
    }

    public CloseableHttpResponse executePost(String url, JSONObject body) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate");
        httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpPost.addHeader("Cache-Control", "max-age=0");
        httpPost.addHeader("Host", "112.98.110.101:10030");
        httpPost.addHeader("proxy-connection", "keep-alive");
        httpPost.addHeader("upgrade-insecure-requests", "1");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");
        // 添加Content-Type头
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");

        // 将JSONObject转换为字符串并设置请求体
        if (body != null) {
            StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
            httpPost.setEntity(entity);
        }
        return httpClient.execute(httpPost);
    }
}
