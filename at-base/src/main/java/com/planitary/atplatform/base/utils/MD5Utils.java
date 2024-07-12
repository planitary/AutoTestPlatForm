package com.planitary.atplatform.base.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.utils
 * @name：CalCulateObjectMD5
 * @Date：2023/12/13 9:05 下午
 * @Filename：CalCulateObjectMD5
 * @description：
 */
public class MD5Utils {

    public static String calculateMD5(String s) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = messageDigest.digest(s.getBytes(StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : md5Bytes){
            stringBuilder.append(String.format("%02x",b));
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        String s = "{\"page\": 1, \"size\": 20, \"res\": [12, 23, 33]}";
        System.out.println(MD5Utils.calculateMD5(s));
    }
}
