package com.xiangkai.community.config;

import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Result<Object> globalExceptionHandler(HttpServletRequest request, Exception e) {

        String requestURI = request.getRequestURI();

        LOGGER.error(String.format("捕获全局异常：请求路径：%s，异常详情：\n%s", requestURI, ExceptionUtils.getStackTrace(e)));

        return new Result<>(ErrorCode.SYSTEM_INTERNAL_ERROR);
    }
}
