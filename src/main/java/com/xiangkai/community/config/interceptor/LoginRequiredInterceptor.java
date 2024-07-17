package com.xiangkai.community.config.interceptor;

import com.xiangkai.community.annotation.LoginRequired;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        LoginRequired loginRequired = method.getMethodAnnotation(LoginRequired.class);
        User user = hostHolder.get();

        // 如果用户未登录，则禁止访问某些页面，直接跳转至登录页
        if (loginRequired != null && user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
