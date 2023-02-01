package org.example.web.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class RoutingUtil {

    public static void forwardToPage(String pageName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("dynamicPage", String.format("../view/%s.jsp", pageName));
        request.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(request, response);
    }

    public static void forwardToFragment(String fragmentName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(String.format("/WEB-INF/fragment/%s.jsp", fragmentName)).forward(request, response);
    }

    public static void redirectToUrl(String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(url);
    }

    public static void sendJSON(JsonObject jsonObject, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        String jsonContent = jsonObject.toString();
        int lengthContent = jsonContent.getBytes(StandardCharsets.UTF_8).length;
        response.setContentLength(lengthContent);
        try (Writer responseWriter = response.getWriter()) {
            responseWriter.write(jsonContent);
            responseWriter.flush();
        }
    }
}
