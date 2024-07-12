package com.xiangkai.community.model.dto;

import lombok.Data;

@Data
public class UserLoginInfo {
    private String username;
    private String password;
    private String verifyCode;
    private Boolean rememberMe;
    private Integer expiredSeconds;
}
