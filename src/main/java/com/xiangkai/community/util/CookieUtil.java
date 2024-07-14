package com.xiangkai.community.util;

import com.xiangkai.community.model.bo.CustomizedCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static CustomizedCookie getCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    CustomizedCookie customizedCookie = new CustomizedCookie()
                            .setCookie(cookie)
                            .setName(name)
                            .setValue(cookie.getValue())
                            .build();

                    return customizedCookie;
                }
            }
        }
        return null;
    }

    public static int invalidCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        CustomizedCookie customizedCookie = getCookieByName(request, name);
        if (customizedCookie != null) {
            Cookie cookie = customizedCookie.getCookie();
            Cookie newCookie = new Cookie(cookie.getName(), cookie.getValue());
            // cookie生命周期声明为0等价于删除cookie
            newCookie.setMaxAge(0);
            newCookie.setPath("/community");
            response.addCookie(newCookie);
            return 0;
        }
        return -1;
    }

    public static int invalidCookie(HttpServletResponse response, CustomizedCookie customizedCookie) {
        if (customizedCookie != null) {
            Cookie cookie = customizedCookie.getCookie();
            Cookie newCookie = new Cookie(cookie.getName(), cookie.getValue());
            // cookie生命周期声明为0等价于删除cookie
            newCookie.setMaxAge(0);
            newCookie.setPath("/community");
            response.addCookie(newCookie);
            return 0;
        }
        return -1;
    }
}
