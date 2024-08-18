package com.xiangkai.community.constant;

public interface CommunityConstant {

    /**
     * 激活成功
     */
    Integer ACTIVATION_SUCCESS = 0;

    /**
     * 激活失败
     */
    Integer ACTIVATION_FAIL = 1;

    /**
     * 重复激活
     */
    Integer ACTIVATION_REPEAT = 2;

    /**
     * 登陆时长：十小时
     */
    Integer NO_REMEMBER_SECONDS = 3600 * 10;

    /**
     * 登陆时长：二十天
     */
    Integer REMEMBER_SECONDS = 3600 * 24 * 20;

    /**
     * 图片后缀正则表达式
     */
    String IMG_SUFFIX_PATTERN = "^(png|jpg|jpeg|svg)$";

    /**
     * entityTYPE:0 -> 帖子
     */
    Integer ENTITY_TYPE_POST = 0;

    /**
     * entityTYPE:1 -> 帖子的评论
     */
    Integer ENTITY_TYPE_COMMENT = 1;

    /**
     * entityTYPE:2 -> 回复的评论
     */
    Integer ENTITY_TYPE_REPLY = 2;

    /**
     * entityTYPE:3 -> 用户
     */
    Integer ENTITY_TYPE_USER = 3;

    /**
     * 忘记密码验证码有效时长
     */
    Integer VERIFICATION_CODE_TIMEOUT = 5 * 60;

    /**
     * 忘记密码验证码过期
     */
    Integer VERIFICATION_CODE_INVALID = 1;

    /**
     * 主题：评论
     */
    String TOPIC_COMMENT = "COMMENT";

    /**
     * 主题：点赞
     */
    String TOPIC_LIKE = "LIKE";

    /**
     * 主题：关注
     */
    String TOPIC_FOLLOW = "FOLLOW";

    /**
     * 主题：发布帖子
     */
    String TOPIC_PUBLISH = "PUBLISH";

    /**
     * 事件ID：评论
     */
    Integer EVENT_TYPE_ID_COMMENT = 1;

    /**
     * 事件ID：点赞
     */
    Integer EVENT_TYPE_ID_LIKE = 2;

    /**
     * 事件ID：关注
     */
    Integer EVENT_TYPE_ID_FOLLOW = 3;

    /**
     * 事件ID：发布帖子
     */
    Integer EVENT_TYPE_ID_PUBLISH = 4;

    /**
     * 系统用户ID
     */
    Integer SYSTEM_USER_ID = 1;

    /**
     * conversationId: like
     */
    String CONVERSATION_ID_LIKE = "like";

    /**
     * conversationId: comment
     */
    String CONVERSATION_ID_COMMENT = "comment";

    /**
     * conversationId: follow
     */
    String CONVERSATION_ID_FOLLOW = "follow";

    /**
     * 权限：普通用户
     */
    String AUTHORITY_USER = "USER";

    /**
     * 权限：超级管理员
     */
    String AUTHORITY_ADMIN = "ADMIN";

    /**
     * 权限：版主
     */
    String AUTHORITY_MODERATOR = "MODERATOR";
}
