package com.xiangkai.community.controller;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.model.dto.DiscussPostDTO;
import com.xiangkai.community.model.entity.Comment;
import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.model.entity.Page;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.CommentService;
import com.xiangkai.community.service.DiscussPostService;
import com.xiangkai.community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/post")
public class DiscussPostController implements CommunityConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscussPostController.class);

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(path = "/publish", method = RequestMethod.POST)
    public Result<Object> publish(@RequestBody DiscussPostDTO dto) {
        return discussPostService.publishDiscussPost(dto);
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String detail(Model model,
                         Page page,
                         @PathVariable("discussPostId") Integer discussPostId) {

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

        page.setLimit(5);
        page.setPath("/post/detail" + "/" + discussPostId);
        page.setRows(commentService.findCommentRows(ENTITY_TYPE_COMMENT, discussPostId));

        // 查找该帖子名下所有评论
        List<Comment> comments = commentService.findComments(ENTITY_TYPE_COMMENT, discussPostId,
                page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentsVO = new ArrayList<>();

        for (Comment comment : comments) {
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("comment", comment);
            commentMap.put("commentUser", userService.findUserById(comment.getUserId()));

            // 查找该评论名下所有回复
            List<Comment> replies = commentService.findComments(ENTITY_TYPE_REPLY, comment.getId(),
                    0, Integer.MAX_VALUE);
            List<Map<String, Object>> repliesVO = new ArrayList<>();

            for (Comment reply : replies) {
                Map<String, Object> replyMap = new HashMap<>();
                replyMap.put("reply", reply);
                replyMap.put("replyUser", userService.findUserById(reply.getUserId()));
                if (reply.getTargetId() != null) {
                    User targetUser = userService.findUserById(reply.getTargetId());
                    replyMap.put("targetUser", targetUser);
                }
                repliesVO.add(replyMap);
            }

            commentMap.put("repliesVO", repliesVO);

            Integer replyCount = commentService.findCommentRows(ENTITY_TYPE_REPLY, comment.getId());
            commentMap.put("replyCount", replyCount);

            commentsVO.add(commentMap);
        }

        model.addAttribute("commentsVO", commentsVO);
        return "/site/discuss-detail";
    }
}
