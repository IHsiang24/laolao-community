package com.xiangkai.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.model.entity.*;
import com.xiangkai.community.model.vo.NoticeVO;
import com.xiangkai.community.service.MessageService;
import com.xiangkai.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeController implements CommunityConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String noticeList(Model model) {
        User loginUser = hostHolder.get();
        if (loginUser == null) {
            throw new IllegalArgumentException("用户未登录！");
        }

        model.addAttribute("commentNotice", handleCommentNotice(loginUser));

        model.addAttribute("likeNotice", handleLikeNotice(loginUser));

        model.addAttribute("followNotice", handleFollowNotice(loginUser));

        // 查询未读数量
        Integer letterUnreadCount = messageService.findUnreadLetterCountByUserId(loginUser.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);

        Integer noticeUnreadCount = messageService.findUnreadNoticeCount(loginUser.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }

    @RequestMapping(path = "/detail/{topic}", method = RequestMethod.GET)
    public String noticeDetail(@PathVariable("topic") String topic,
                               Model model, Page page) {
        User loginUser = hostHolder.get();
        if (loginUser == null) {
            throw new IllegalArgumentException("参数错误：用户为空！");
        }

        page.setPath("/notice/detail/" + topic);
        page.setLimit(5);
        page.setRows(messageService.findTotalNoticeCount(loginUser.getId(), topic));
        List<Message> noticeList = messageService.findNotices(loginUser.getId(), topic,
                page.getOffset(), page.getLimit());

        List<NoticeVO> noticeVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(noticeList)) {
            for (Message message : noticeList) {
                NoticeVO.Builder builder = new NoticeVO.Builder();
                builder.message(message);

                Event event = JSONObject.parseObject(message.getContent(), new TypeReference<Event>(){}.getType(), Feature.SupportNonPublicField);
                builder.user(userService.findUserById(event.getUserId()));
                builder.entityType(event.getEntityType());
                builder.entityId(event.getEntityId());
                builder.postId((Integer) event.getData().get("discussPostId"));
                builder.fromUser(userService.findUserById(message.getFromId()));
                noticeVOS.add(builder.build());
            }
        }

        model.addAttribute("notices", noticeVOS);

        // 设置已读
        List<Integer> ids = getUpdateIds(noticeList);

        if (!CollectionUtils.isEmpty(ids)) {
            messageService.updateStatus(ids, 1);
        }

        return "/site/notice-detail";
    }

    private NoticeVO handleCommentNotice(User loginUser) {
        // 查询评论类通知
        Message commentMessage = messageService.findLatestNotice(loginUser.getId(), CONVERSATION_ID_COMMENT);
        NoticeVO commentNoticeVO = new NoticeVO();
        NoticeVO.Builder commentMessageBuilder = new NoticeVO.Builder();
        if (commentMessage != null) {
            commentMessageBuilder.message(commentMessage);

            Event event = JSONObject.parseObject(commentMessage.getContent(), new TypeReference<Event>(){}.getType(), Feature.SupportNonPublicField);
            commentMessageBuilder.user(userService.findUserById(event.getUserId()));
            commentMessageBuilder.entityType(event.getEntityType());
            commentMessageBuilder.entityId(event.getEntityId());
            commentMessageBuilder.postId((Integer) event.getData().get("discussPostId"));

            int count = messageService.findTotalNoticeCount(loginUser.getId(), CONVERSATION_ID_COMMENT);
            commentMessageBuilder.count(count);

            int unread = messageService.findUnreadNoticeCount(loginUser.getId(), CONVERSATION_ID_COMMENT);
            commentMessageBuilder.unread(unread);

            commentNoticeVO = commentMessageBuilder.build();
        }
        return commentNoticeVO;
    }

    private NoticeVO handleLikeNotice(User loginUser) {
        // 查询点赞类通知
        Message likeMessage = messageService.findLatestNotice(loginUser.getId(), CONVERSATION_ID_LIKE);
        NoticeVO likeNoticeVO = new NoticeVO();
        NoticeVO.Builder likeMessageBuilder = new NoticeVO.Builder();
        if (likeMessage != null) {
            likeMessageBuilder.message(likeMessage);

            Event event = JSONObject.parseObject(likeMessage.getContent(), new TypeReference<Event>(){}.getType(), Feature.SupportNonPublicField);
            likeMessageBuilder.user(userService.findUserById(event.getUserId()));
            likeMessageBuilder.entityType(event.getEntityType());
            likeMessageBuilder.entityId(event.getEntityId());
            likeMessageBuilder.postId((Integer) event.getData().get("discussPostId"));

            int count = messageService.findTotalNoticeCount(loginUser.getId(), CONVERSATION_ID_LIKE);
            likeMessageBuilder.count(count);

            int unread = messageService.findUnreadNoticeCount(loginUser.getId(), CONVERSATION_ID_LIKE);
            likeMessageBuilder.unread(unread);

            likeNoticeVO = likeMessageBuilder.build();
        }
        return likeNoticeVO;
    }

    private NoticeVO handleFollowNotice(User loginUser) {
        // 查询关注类通知
        Message followMessage = messageService.findLatestNotice(loginUser.getId(), CONVERSATION_ID_FOLLOW);
        NoticeVO followNoticeVO = new NoticeVO();
        NoticeVO.Builder followMessageBuilder = new NoticeVO.Builder();
        if (followMessage != null) {
            followMessageBuilder.message(followMessage);

            Event event = JSONObject.parseObject(followMessage.getContent(),  new TypeReference<Event>(){}.getType(), Feature.SupportNonPublicField);
            followMessageBuilder.user(userService.findUserById(event.getUserId()));
            followMessageBuilder.entityType(event.getEntityType());
            followMessageBuilder.entityId(event.getEntityId());

            int count = messageService.findTotalNoticeCount(loginUser.getId(), CONVERSATION_ID_FOLLOW);
            followMessageBuilder.count(count);

            int unread = messageService.findUnreadNoticeCount(loginUser.getId(), CONVERSATION_ID_FOLLOW);
            followMessageBuilder.unread(unread);

            followNoticeVO = followMessageBuilder.build();
        }
        return followNoticeVO;
    }
    private List<Integer> getUpdateIds(List<Message> noticeList) {
        List<Integer> ids = new ArrayList<>();

        for (Message letter : noticeList) {
            if (letter.getToId().equals(hostHolder.get().getId()) && letter.getStatus().equals(0)) {
                ids.add(letter.getId());
            }
        }

        return ids;
    }
    
}
