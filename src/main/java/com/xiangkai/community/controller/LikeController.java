package com.xiangkai.community.controller;

import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.model.dto.LikeDTO;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.model.vo.LikeVO;
import com.xiangkai.community.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @ResponseBody
    @RequestMapping(path = "like", method = RequestMethod.POST)
    public Result<LikeVO> like(@RequestBody LikeDTO dto) {
        User user = hostHolder.get();
        if (user == null) {
            throw new IllegalArgumentException("参数错误：用户未登录！");
        }
        LikeVO vo = new LikeVO();
        likeService.like(dto.getEntityType(), dto.getEntityId(), user.getId());
        Long likeCount = likeService.findEntityLikeCount(dto.getEntityType(), dto.getEntityId());
        Integer likeStatus = likeService.findEntityLikeStatus(dto.getEntityType(), dto.getEntityId(), user.getId());
        vo.setLikeCount(likeCount);
        vo.setLikeStatus(likeStatus);
        return new Result<>(ErrorCode.SUCCESS, vo);
    }
}
