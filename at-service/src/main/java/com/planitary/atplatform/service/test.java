package com.planitary.atplatform.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Objects;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service
 * @name：test
 * @Date：2024/1/17 9:05 下午
 * @Filename：test
 * @description：
 */
public class test {
    public static void main(String[] args) {

        String json = "{\"map\": {}, \"code\": \"0\", \"data\": {\"interfaceId\": \"313801911586816\"}, \"errMsg\": null, \"traceId\": null}";
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Object read = JsonPath.read(document, "$.data.interfaceId");
        System.out.println(read);
    }
}