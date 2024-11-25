package com.xiangkai.community.model.entity;

import lombok.Data;

@Data
public class EsSyncSucceedDiscussPost {

    /**
     * 帖子ID
     */
    Integer id;

    /**
     * 同步状态
     * <p>
     *     0-未同步；1-同步成功
     * </p>
     */
    Integer status = 0;
}
