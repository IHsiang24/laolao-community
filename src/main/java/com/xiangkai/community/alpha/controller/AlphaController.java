package com.xiangkai.community.alpha.controller;

import com.xiangkai.community.util.CommunityUtil;
import com.xiangkai.community.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AlphaController {

    @ResponseBody
    @RequestMapping(path = "/alpha/setCookie", method = RequestMethod.GET)
    public String setCookie() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        Cookie cookie1 = new Cookie("code1", CommunityUtil.generateUUID());
        Cookie cookie2 = new Cookie("code2", CommunityUtil.generateUUID());
        cookie1.setMaxAge(60 * 10);
        cookie1.setPath("/community/alpha");

        cookie2.setMaxAge(60 * 10);
        cookie2.setPath("/community/alpha");

        response.addCookie(cookie1);
        response.addCookie(cookie2);
        return "set cookie";
    }

    @ResponseBody
    @RequestMapping(path = "/alpha/getCookie", method = RequestMethod.GET)
    public String getCookie() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();
        System.out.println(cookies[0]);
        System.out.println(cookies[1]);
        return "get cookie";
    }

    @ResponseBody
    @RequestMapping(path = "/alpha/setSession", method = RequestMethod.GET)
    public String setSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("code", CommunityUtil.generateUUID());
        return "set session";
    }

    @ResponseBody
    @RequestMapping(path = "/alpha/getSession", method = RequestMethod.GET)
    public String getSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("code"));
        return "get session";
    }

    @ResponseBody
    @RequestMapping(path = "/alpha/invalidCookie", method = RequestMethod.GET)
    public String invalidCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.invalidCookie(request, response, "code1");
        return "invalid cookie";
    }

}
