package com.bin.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EncodeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType("text/html;charset:UTF-8");
        response.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest,response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
