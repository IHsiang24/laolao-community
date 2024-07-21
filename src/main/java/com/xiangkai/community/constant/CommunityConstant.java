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
     * entityTYPE:1 -> 帖子的评论
     */
    Integer ENTITY_TYPE_COMMENT = 1;

    /**
     * entityTYPE:2 -> 回复的评论
     */
    Integer ENTITY_TYPE_REPLY = 2;

    /**
     * 忘记密码验证码有效时长
     */
    Integer VERIFICATION_CODE_TIMEOUT = 5 * 60;

    /**
     * 忘记密码验证码过期
     */
    Integer VERIFICATION_CODE_INVALID = 1;

}
