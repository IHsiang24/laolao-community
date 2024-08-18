package com.xiangkai.community.config;

import com.xiangkai.community.config.filter.FirstVisitFilter;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Autowired
    private FirstVisitFilter firstVisitFilter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略静态资源的访问
        web.ignoring().antMatchers("/resources/**");
    }

    /**
     * 授权
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 授权配置 -> 没有权限就抛出403异常
        http.authorizeRequests()
                // 正则：需要特定权限才能访问的路径
                .antMatchers(
                        "/comment/add/**",
                        "/follow",
                        "/unFollow",
                        "/like",
                        "/post/publish",
                        "/user/profile/**",
                        "/user/uploadHeader",
                        "/message/**",
                        "/notice/**",
                        "/index"
                )
                // antMatchers匹配的路径需要什么权限才能访问
                .hasAnyAuthority(
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR,
                        AUTHORITY_USER
                )
                // 除了antMatchers匹配的路径需要指定的权限外，其他的请求路径不需要权限就能访问
                .anyRequest()
                .permitAll()
                .and()
                // 关闭csrf验证，否则前端每一个form表单提交都会生成一个_csrftoken
                .csrf()
                .disable();

        // 处理没有权限时的异常情况：403异常 (考虑一般请求和异步请求两种类型：不能靠accessDeniedPage返回单一的错误页面)
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    // 没有登录时的异常处理
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        // 读取header的x-requested-with，如果是XMLHttpRequest，则是异步请求；否则是同步请求
                        String xrw = request.getHeader("x-requested-with");

                        // 异步请求：返回json格式的错误码；同步请求，直接重定向至登陆页面
                        if (!StringUtils.isBlank(xrw) && "XMLHttpRequest".equals(xrw)) {

                            response.setContentType("application/plain;charset=utf-8");

                            try (PrintWriter writer = response.getWriter();) {
                                writer.write(new Result<>(ErrorCode.USER_UNLOGIN).toJson());
                                writer.flush();
                            }
                        } else {
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 权限不足时的异常处理
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        // 读取header的x-requested-with，如果是XMLHttpRequest，则是异步请求；否则是同步请求
                        String xrw = request.getHeader("x-requested-with");

                        // 异步请求：返回json格式的错误码；同步请求，直接重定向至登陆页面
                        if (!StringUtils.isBlank(xrw) && "XMLHttpRequest".equals(xrw)) {

                            response.setContentType("application/plain;charset=utf-8");

                            try (PrintWriter writer = response.getWriter();) {
                                writer.write(new Result<>(ErrorCode.USER_NO_AUTHORITY).toJson());
                                writer.flush();
                            }
                        } else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // 自定义filter，处理remember-me
        http.addFilterBefore(firstVisitFilter, BasicAuthenticationFilter.class);

        // security底层默认会拦截/logout请求，覆盖他默认的逻辑才能执行我们自己的代码, (写一个假的登出路径欺骗security)
        http.logout().logoutUrl("/fakeLogout");
    }
}
