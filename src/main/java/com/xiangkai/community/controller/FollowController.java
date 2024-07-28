package com.xiangkai.community.controller;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.model.dto.FollowDTO;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @ResponseBody
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    public Result<Object> follow(@RequestBody FollowDTO dto) {
        User user = hostHolder.get();
        followService.follow(user.getId(), dto.getEntityType(), dto.getEntityId());
        return new Result<>(ErrorCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(path = "/unFollow", method = RequestMethod.POST)
    public Result<Object> unFollow(@RequestBody FollowDTO dto) {
        User user = hostHolder.get();
        followService.unFollow(user.getId(), dto.getEntityType(), dto.getEntityId());
        return new Result<>(ErrorCode.SUCCESS);
    }

}
