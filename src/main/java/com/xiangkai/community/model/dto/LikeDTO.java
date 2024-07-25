package com.xiangkai.community.model.dto;

import lombok.Data;

@Data
public class LikeDTO {

    public Integer entityType;

    public Integer entityId;

    public Integer entityUserId;
}
