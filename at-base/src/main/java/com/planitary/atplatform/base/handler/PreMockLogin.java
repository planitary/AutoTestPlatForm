package com.planitary.atplatform.base.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 预登录
 */
@Slf4j
public class PreMockLogin {
    private static final String USER_CODE = "2103471201";
    private static final String TEST_LOGIN_URL_PREFIX = "https://test-scs-login.52imile.cn/ucenter/";
    private static final String PASSWORD = "g5dllW8Rk9IGOO6XhqNAuwq5e/R4pfYG3Y/w9+pwio5CpMB727UQg0Ov0WDCfn3MsRt+9CsFKx94zFiNe1PEM2fVnjNqbSKsurYFmh41MoI+qbL6ou+PJgAO+KPDrmTGc346vz+46zKpBuX4Lbmms7NNr5QwzJm8kbCl2PLGOgE=";
    private static final String LOGIN_AUTHORIZED_HEADER = "Basic YXBwOmFwcA==";

    private static String login(){
        String contentType = "application/x-www-form-urlencoded";
        Map<String,String> map = new HashMap<>();
        Map<String,String> headersMap = new HashMap<>();
        String loginUrl = TEST_LOGIN_URL_PREFIX + "imile/login";
        map.put("userCode",USER_CODE);
        map.put("password",PASSWORD);
        map.put("remember","true");
        map.put("lang","zh_CN");
        map.put("timeZone","8");
        headersMap.put("Authorization",LOGIN_AUTHORIZED_HEADER);
        headersMap.put("Content-Type",contentType);
        String loginResponse = CommonHttpPost.doCommonHttpPostForm(map,headersMap,loginUrl);
        log.info("url:{},resbody:{}",loginUrl,loginResponse);
        return loginResponse;
    }

    /**
     * 预登录拿到token
     * @return map
     * keySet:accessToken,tokenType,refreshToken,userStatus;
     */
    private static Map<String,String> getToken(){
        Map<String,String> map = new HashMap<>();
        String loginRes = login();
        //解析JSON，拿到token
        JSONObject jsonObject = JSON.parseObject(loginRes);
        JSONObject resultObject = jsonObject.getJSONObject("resultObject");
        String accessToken = resultObject.getString("access_token");
        String tokenType = resultObject.getString("token_type");
        String refreshToken = resultObject.getString("refresh_token");
        JSONObject userInfoObject = resultObject.getJSONObject("user_info");
        String userStatus = userInfoObject.getString("enabled");
        // tokenType的首位大写
        tokenType = tokenType.substring(0, 1).toUpperCase() + tokenType.substring(1);
        map.put("accessToken", accessToken);
        map.put("tokenType", tokenType);
        map.put("refreshToken", refreshToken);
        map.put("userStatus", userStatus);
        return map;
    }

    public static void main(String[] args) {
        Map<String, String> token = getToken();
        System.out.println(token);
    }
}
