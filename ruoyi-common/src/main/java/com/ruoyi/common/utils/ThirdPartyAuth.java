package com.ruoyi.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/8
 */
@Component
public class ThirdPartyAuth {
    @Value("${lanya.base-url:112.98.110.101:8091/gateway-service}")
    public String baseUrl;
    @Value("${lanya.username:admin}")
    private String username;
    @Value("${lanya.password:xrkc168168}")
    private String password;

    public String token = null;

    private ObjectMapper mapper = new ObjectMapper();

    public RestTemplate restTemplate = new RestTemplate();

    private LinkedHashMap<String, String> userInfo = new LinkedHashMap<>();

    public Map callThirdPartyLogin() throws JsonProcessingException {

        String url = "http://" + baseUrl + "/auth/login";

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
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        String respBody = resp.getBody();
        Map respMap = mapper.readValue(respBody, HashMap.class);
        if (respMap.containsKey("data")) {
            Map dataMap = (Map) respMap.get("data");
            if (dataMap.containsKey("token")) {
                token = (String) dataMap.get("token");
                userInfo = (LinkedHashMap) dataMap.get("userInfo");
            }
            return dataMap;
        }
        return null;
    }

    public Object callThirdParty(String path, HttpMethod method, Map<String, Object> body) throws JsonProcessingException {
        String url = "http://" + baseUrl + path;
        // 4. 发 POST
        Object object = exchange(url, method, entity(body, MediaType.APPLICATION_JSON));

        if (object instanceof Map) {
            Map map = (Map) object;
            if (map.containsKey("code") && !"200".equals(map.get("code") + "")) {
                if ("令牌不能为空".equals(map.get("msg"))) {
                    callThirdPartyLogin();
                    object = exchange(url, method, entity(body, MediaType.APPLICATION_JSON));
                } else {
                    return object;
                }
            }
        }
        return object;
    }

    public Object callThirdParty(String path, HttpMethod method, HttpEntity<MultiValueMap<String, Object>> entity) throws JsonProcessingException {
        String url = "http://" + baseUrl + path;
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(entity.getHeaders());
        headers.set("Authorization", token);
        HttpEntity<MultiValueMap<String, Object>> newEntity = new HttpEntity<>(entity.getBody(), headers);

        // 4. 发 POST
        Object object = exchange(url, method, newEntity);

        if (object instanceof Map) {
            Map map = (Map) object;
            if (map.containsKey("code") && !"200".equals(map.get("code") + "")) {
                if ("令牌不能为空".equals(map.get("msg"))) {
                    callThirdPartyLogin();
                    headers = new HttpHeaders();
                    headers.putAll(entity.getHeaders());
                    headers.set("Authorization", token);
                    newEntity = new HttpEntity<>(entity.getBody(), headers);
                    object = exchange(url, method, newEntity);
                } else {
                    return null;
                }
            }
        }

        return object;
    }

    public Object callThirdParty(String path, HttpMethod method, MediaType imageJpeg) throws JsonProcessingException {
        String url = "http://" + baseUrl + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(imageJpeg);
        headers.set("Authorization", token);

        // 3. 组装请求
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        Object object = exchange(url, method, entity);

        if (object instanceof Map) {
            Map map = (Map) object;
            if (map.containsKey("code") && !"200".equals(map.get("code") + "")) {
                if ("令牌不能为空".equals(map.get("msg"))) {
                    callThirdPartyLogin();
                    headers = new HttpHeaders();
                    headers.putAll(entity.getHeaders());
                    headers.set("Authorization", token);
                    entity = new HttpEntity<>(entity.getBody(), headers);
                    object = exchange(url, method, entity);
                } else {
                    return null;
                }
            }
        }

        return object;
    }

    private HttpEntity<Object> entity(Map<String, Object> body, MediaType mediaType) throws JsonProcessingException {
        // 1. JSON 体
        String sendBody = mapper.writeValueAsString(body);

        // 2. 头
        HttpHeaders headers = new HttpHeaders();
        if (mediaType != null) {
            headers.setContentType(mediaType);
        } else {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.set("Authorization", token);

        // 3. 组装请求
        HttpEntity<Object> entity = new HttpEntity<>(sendBody, headers);
        return entity;
    }

    private Object exchange(String url, HttpMethod method, HttpEntity entity) throws JsonProcessingException {
        ResponseEntity<String> resp = restTemplate.exchange(url, method, entity, String.class);
        String respBody = resp.getBody();
        Object result = null;
        try {
            result = mapper.readValue(respBody, HashMap.class);
        } catch (Exception e) {
            result = respBody;
        }

        return result;
    }


    public LinkedHashMap<String, String> getUserInfo() throws JsonProcessingException {
        if (token == null) {
            callThirdPartyLogin();
        }
        return userInfo;
    }
}
