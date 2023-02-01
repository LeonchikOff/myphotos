package org.example.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = {"/static/*", "/favicon.ico"}, loadOnStartup = 1)
public class ResourceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rootPath = this.getServletContext().getRealPath("/");
        String requestURI = req.getRequestURI();
        Path path = Paths.get(rootPath + requestURI);
        resp.setContentType(this.getServletContext().getMimeType(path.toString()));
        resp.setContentLengthLong(Files.size(path));
        try(OutputStream outputStream = resp.getOutputStream()) {
            Files.copy(path, outputStream);
        }

    }
}
