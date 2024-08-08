package com.xiangkai.community.config.interceptor;

import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NoticeInterceptor implements HandlerInterceptor {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        User loginUser = hostHolder.get();

        if (loginUser != null && modelAndView != null) {
            Integer unreadLetterCount = messageService.findUnreadLetterCountByUserId(loginUser.getId(), null);

            Integer unreadNoticeCount = messageService.findUnreadNoticeCount(loginUser.getId(), null);
            modelAndView.addObject("unreadCount", unreadLetterCount + unreadNoticeCount);
        }
    }
}
