package com.xiangkai.community.controller;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.model.dto.FollowDTO;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.Page;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.FollowService;
import com.xiangkai.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

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

    @RequestMapping(path = "/followee/{userId}", method = RequestMethod.GET)
    public String followee(Model model, @PathVariable("userId") Integer userId, Page page) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("参数错误：用户为空！");
        }

        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/followee/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> followeeList = followService.findFolloweeList(
                userId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());

        model.addAttribute("followeeList", followeeList);

        return "/site/followee";
    }

    @RequestMapping(path = "/follower/{userId}", method = RequestMethod.GET)
    public String follower(Model model, @PathVariable("userId") Integer userId, Page page) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("参数错误：用户为空！");
        }

        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/follower/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

        List<Map<String, Object>> followerList = followService.findFollowerList(
                userId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());

        model.addAttribute("followerList", followerList);

        return "/site/follower";
    }

}
