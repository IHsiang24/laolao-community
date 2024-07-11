package com.xiangkai.community.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateMD5(String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(generateUUID());
    }
}
