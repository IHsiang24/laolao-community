package com.xiangkai.community.config.filter;

import com.xiangkai.community.model.bo.CustomizedCookie;
import com.xiangkai.community.model.entity.LoginTicket;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.UserService;
import com.xiangkai.community.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class FirstVisitFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        CustomizedCookie cookie = CookieUtil.getCookieByName(request, "ticket");

        if (cookie != null) {
            LoginTicket loginTicket = userService.findByTicket(cookie.getValue());
            if (loginTicket!=null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());

                // 写入登录信息
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getGrantedAuthorities(user.getId()));

                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
        chain.doFilter(request, response);
    }
}
