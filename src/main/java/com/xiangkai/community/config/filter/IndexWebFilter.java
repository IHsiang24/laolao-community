package com.xiangkai.community.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndexWebFilter implements Filter {

    private static final String ROOT_PATH = "/";

    private static final String CONTEXT_PATH = "/community";

    private static final String CONTEXT_PATH_ = "/community/";

    private static final String INDEX_PATH = CONTEXT_PATH + "/index";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String requestURI = httpServletRequest.getRequestURI();

        if (ROOT_PATH.equals(requestURI) || CONTEXT_PATH.equals(requestURI) || CONTEXT_PATH_.equals(requestURI)) {
            request.getRequestDispatcher(INDEX_PATH).forward(request, response);
        }

        chain.doFilter(request, response);
    }
}
