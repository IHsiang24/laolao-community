package com.xiangkai.community.util;

public class RedisUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_LIKE_ENTITY = "like:entity";
    private static final String PREFIX_LIKE_USER = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee:";
    private static final String PREFIX_FOLLOWER = "follower:";

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

    // 用户关注的实体
    // followee:userId:entityType -> zset(entityId, now)
    public static  String getFollowTargetKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + userId + SPLIT + entityType;
    }

    // 某一实体的关注者
    // follower:entityType:entityId -> zset(userId, now)
    public static  String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + entityType + SPLIT + entityId;
    }
}
