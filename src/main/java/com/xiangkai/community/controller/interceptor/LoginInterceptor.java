package com.xiangkai.community.controller.interceptor;

import com.xiangkai.community.model.entity.Holder;
import com.xiangkai.community.model.entity.LoginTicket;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.UserService;
import com.xiangkai.community.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    private final Holder<User> holder = new Holder<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ticket = CookieUtil.getCookieValue(request, "ticket");

        if (ticket != null) {
            LoginTicket loginTicket = userService.findByTicket(ticket);
            if (loginTicket!=null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());
                holder.set(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User loginUser = holder.get();
        if (modelAndView != null && loginUser != null ) {
            modelAndView.addObject("loginUser", loginUser);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        holder.destroy();
    }
}
