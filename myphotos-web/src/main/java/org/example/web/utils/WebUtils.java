package org.example.web.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
