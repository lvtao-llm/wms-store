package com.ruoyi.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/8
 */
@Component
public class ThirdPartyAuth {
    @Value("${third-party.base-url:http://112.98.110.101:8091}")
    private String baseUrl;
    @Value("${third-party.username:admin}")
    private String username;
    @Value("${third-party.password:xrkc168168}")
    private String password;

    private String token = null;

    private ObjectMapper mapper = new ObjectMapper();

    private RestTemplate restTemplate = new RestTemplate();

    public Map callThirdPartyLogin() throws JsonProcessingException {

        String url = baseUrl + "/auth/login";

        // 1. JSON 体
        Map<String, String> sendMap = new HashMap<>();
        sendMap.put("userName", username);
        sendMap.put("password", password);
        sendMap.put("source", "API");
        String sendBody = mapper.writeValueAsString(sendMap);

        // 2. 头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("app-key", "abc123");

        // 3. 组装请求
        HttpEntity<String> entity = new HttpEntity<>(sendBody, headers);

        // 4. 发 POST
        ResponseEntity<String> resp = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        String respBody = resp.getBody();
        Map respMap = mapper.readValue(respBody, HashMap.class);
        if (respMap.containsKey("data")) {
            Map dataMap = (Map) respMap.get("data");
            if (dataMap.containsKey("token")) {
                token = (String) dataMap.get("token");
            }
            return dataMap;
        }
        return null;
    }

    public Object callThirdParty(String path, HttpMethod method, Map<String, Object> body) throws JsonProcessingException {
        String url = baseUrl + path;

        // 4. 发 POST
        Map respMap = exchange(url, method, entity(body));

        if (respMap.containsKey("code") && !"200".equals(respMap.get("code") + "")) {
            if ("令牌不能为空".equals(respMap.get("msg"))) {
                callThirdPartyLogin();
                respMap = exchange(url, method, entity(body));
            } else {
                return null;
            }
        }

        if (!respMap.containsKey("data")) {
            return null;
        }

        Object data =  respMap.get("data");
        return data;
    }

    private HttpEntity<String> entity(Map<String, Object> body) throws JsonProcessingException {
        // 1. JSON 体
        String sendBody = mapper.writeValueAsString(body);

        // 2. 头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        // 3. 组装请求
        HttpEntity<String> entity = new HttpEntity<>(sendBody, headers);
        return entity;
    }

    private Map exchange(String url, HttpMethod method, HttpEntity<String> entity) throws JsonProcessingException {
        ResponseEntity<String> resp = restTemplate.exchange(url, method, entity, String.class);
        String respBody = resp.getBody();
        Map respMap = mapper.readValue(respBody, HashMap.class);
        return respMap;
    }
}
