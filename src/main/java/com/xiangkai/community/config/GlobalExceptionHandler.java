package com.xiangkai.community.config;

import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public void globalExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {

        String requestURI = request.getRequestURI();

        LOGGER.error(String.format("捕获全局异常：请求路径：%s，异常详情：\n%s",
                requestURI, ExceptionUtils.getStackTrace(e)));

        try {
            // 读取header的x-requested-with，如果是XMLHttpRequest，则是异步请求；否则是同步请求
            String xrw = request.getHeader("x-requested-with");

            // 异步请求：返回json格式的错误码；同步请求，直接重定向至登陆页面
            if (!StringUtils.isBlank(xrw) && "XMLHttpRequest".equals(xrw)) {

                response.setContentType("application/plain;charset=utf-8");

                try (PrintWriter writer = response.getWriter();) {
                    writer.write(new Result<>(ErrorCode.SYSTEM_INTERNAL_ERROR).toJson());
                    writer.flush();
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/denied");
            }
        } catch (IOException ioException) {
            LOGGER.error("无法处理异常：" + ioException);
        }
    }
}
