package com.xiangkai.community.util;

import org.springframework.util.DigestUtils;

import java.util.Random;
import java.util.UUID;

public class CommunityUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateRandom(int flag, int bound) {
        String source = "0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < flag; j++) {
            result.append(source.charAt(random.nextInt(bound)));
        }
        return result.toString();
    }

    public static String generateMD5(String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(generateRandom(6, 10));
    }
}
