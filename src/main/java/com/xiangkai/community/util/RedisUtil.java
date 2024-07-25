package com.xiangkai.community.util;

public class RedisUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_LIKE_ENTITY = "like:entity";
    private static final String PREFIX_LIKE_USER = "like:user";

    // 用集合存点赞的用户id
    // like:entity:entityType:entityId -> set(userId1, userId2,..., userIdx)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_LIKE_ENTITY + SPLIT + entityType + SPLIT + entityId;
    }

    // 用整数存用户收到的点赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_LIKE_USER + SPLIT + userId;
    }
}
