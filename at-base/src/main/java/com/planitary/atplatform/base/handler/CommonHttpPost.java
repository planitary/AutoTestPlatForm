package com.planitary.atplatform.base.handler;


import com.alibaba.fastjson.JSON;
import com.planitary.atplatform.base.exception.ATPlatformException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Component
public class CommonHttpPost {
    String resBody = null;

    /**
     * 通用请求——表单
     *
     * @param body    请求体
     * @param headers 请求头
     * @param url     url
     * @return
     */

    public String doCommonHttpPostForm(Map<String, String> body, Map<String, String> headers, String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Authorization", headers.get("Authorization"));
        httpPost.addHeader("Content-Type", headers.get("Content-Type"));
        List<NameValuePair> params = new ArrayList<>();
        log.info("请求体:{},请求头,{},url:{}", body, headers, url);
        for (Map.Entry<String, String> entry : body.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params);
            httpPost.setEntity(urlEncodedFormEntity);
            HttpResponse response = httpClient.execute(httpPost);
            // 处理响应
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            if (statusCode == 200) {
                resBody = responseBody;
            } else {
                log.debug("请求结果:{}", responseBody);
            }
        } catch (IOException e) {
            log.error("捕获异常{}", e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("捕获异常:{}", e.getMessage());
            }
        }
        return resBody;
    }

    /**
     * 通用请求-json
     * @param body      请求体
     * @param headers   请求头
     * @param url       url
     * @return
     */
    public String doCommonHttpPostJson(Map<String,String> body,Map<String,String> headers,String url) {
        String responseJSON = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Authorization", headers.get("Authorization"));
        httpPost.addHeader("Content-Type", headers.get("Content-Type"));
        try {
            log.info("json解析源文件:{}", body);
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(body));
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String resBody = EntityUtils.toString(httpResponse.getEntity());
            if (statusCode == 200) {
                responseJSON = resBody;
            } else {
                log.debug("请求结果:{}", resBody);
            }
        } catch (IOException e) {
            log.error("捕获异常{}", e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("捕获异常:{}", e.getMessage());
            }
        }
        return responseJSON;
    }
}
