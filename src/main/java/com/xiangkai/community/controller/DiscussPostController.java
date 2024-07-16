package com.xiangkai.community.controller;

import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.model.dto.DiscussPostDTO;
import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.DiscussPostService;
import com.xiangkai.community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
public class DiscussPostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscussPostController.class);

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(path = "/publish", method = RequestMethod.POST)
    public Result<Object> publish(@RequestBody DiscussPostDTO dto) {
        return discussPostService.publishDiscussPost(dto);
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String detail(Model model, @PathVariable("discussPostId") Integer discussPostId) {

        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        if (post == null) {
            model.addAttribute("msg", "获取帖子详情失败");
            model.addAttribute("target", "/index");
            LOGGER.error("获取帖子详情失败");
            return "/site/operate-result";
        }

        User user = userService.findUserById(post.getUserId());

        model.addAttribute("post", post);
        model.addAttribute("user", user);
        return "/site/discuss-detail";
    }
}
