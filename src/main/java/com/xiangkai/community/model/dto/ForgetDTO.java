package com.xiangkai.community.model.dto;

import lombok.Data;

@Data
public class ForgetDTO {
    private String email;

    private String code;

    private String newPassword;
}
