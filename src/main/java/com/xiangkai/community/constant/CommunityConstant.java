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
     * 登陆时长：一百天
     */
    Integer REMEMBER_SECONDS = 3600 * 24 * 100;

    String IMG_SUFFIX_PATTERN = "^(png|jpg|jpeg|svg)$";

}
