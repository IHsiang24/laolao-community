package com.xiangkai.community.util;

public class RedisUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_LIKE_ENTITY = "like:entity";

    // 用集合存点赞的用户id
    // like:entity:entityType:entityId -> set(userId1, userId2,..., userIdx)
    public static String getEntityKey(int entityType, int entityId) {
        return PREFIX_LIKE_ENTITY + SPLIT + entityType + SPLIT + entityId;
    }
}
