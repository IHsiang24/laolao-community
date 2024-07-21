package com.xiangkai.community.controller;

import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.Message;
import com.xiangkai.community.model.entity.Page;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.MessageService;
import com.xiangkai.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String messageList(Model model, Page page) {
        User user = hostHolder.get();
        if (user != null) {
            page.setPath("/message/list");
            page.setLimit(5);
            page.setRows(messageService.findConversationCountByUserId(user.getId()));

            List<Message> conversations = messageService.findConversationsByUserId(
                    user.getId(), page.getOffset(), page.getLimit());
            List<Map<String, Object>> conversationsVO = new ArrayList<>();
            for (Message conversation : conversations) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", conversation);

                Integer conversationLetterCount = messageService.findLetterCountByConversationId(
                        conversation.getConversationId());
                map.put("conversationLetterCount", conversationLetterCount);

                Integer conversationUnreadLetterCount = messageService.findUnreadLetterCountByUserId(user.getId(),
                        conversation.getConversationId());
                map.put("conversationUnreadLetterCount", conversationUnreadLetterCount);

                // targetUser永远是别人："来自xxx的私信" -> 不可能是来自自己的私信吧
                Integer targetId = user.getId().equals(conversation.getFromId()) ?
                        conversation.getToId() : conversation.getFromId();
                User targetUser = userService.findUserById(targetId);
                map.put("targetUser", targetUser);

                conversationsVO.add(map);
            }
            model.addAttribute("conversationsVO", conversationsVO);

            Integer totalUnreadLetterCount = messageService.findUnreadLetterCountByUserId(user.getId(), null);
            model.addAttribute("totalUnreadLetterCount", totalUnreadLetterCount);
        } else {
            throw new IllegalArgumentException("参数错误：用户为空！");
        }
        return "/site/letter";
    }

    @RequestMapping(path = "/detail/{conversationId}", method = RequestMethod.GET)
    public String messageDetail(Model model, Page page, @PathVariable("conversationId") String conversationId) {
        page.setPath("/message/list/" + conversationId);
        page.setLimit(5);
        page.setRows(messageService.findLetterCountByConversationId(conversationId));

        List<Message> letters = messageService.findLetterByConversationId(
                conversationId, page.getOffset(), page.getLimit());

        List<Map<String, Object>> lettersVO = new ArrayList<>();
        for (Message letter : letters) {
            Map<String, Object> map = new HashMap<>();
            map.put("letter", letter);
            User fromUser = userService.findUserById(letter.getFromId());
            map.put("fromUser", fromUser);
            lettersVO.add(map);
        }

        model.addAttribute("lettersVO", lettersVO);
        model.addAttribute("targetUser", getTargetUserId(conversationId));

        return "/site/letter-detail";
    }

    private User getTargetUserId(String conversationId) {
        String[] s = conversationId.split("_");

        Integer id0 = Integer.parseInt(s[0]);
        Integer id1 = Integer.parseInt(s[1]);

        // targetUser永远是别人："来自xxx的私信" -> 不可能是来自自己的私信吧
        if (id0.equals(hostHolder.get().getId())) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }

    }
}
