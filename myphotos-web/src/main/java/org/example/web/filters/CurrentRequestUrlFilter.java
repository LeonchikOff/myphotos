package org.example.web.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CurrentRequestUrlFilter", asyncSupported = true)
public class CurrentRequestUrlFilter implements Filter {

    public static final String CURRENT_REQUEST_URL = "currentRequestUrl";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        System.out.println(CURRENT_REQUEST_URL + ": " + requestURI);
        request.setAttribute(CURRENT_REQUEST_URL, requestURI);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
