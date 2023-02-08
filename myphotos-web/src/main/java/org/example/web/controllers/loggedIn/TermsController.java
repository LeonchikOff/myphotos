package org.example.web.controllers.loggedIn;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/terms")
public class TermsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("dynamicPage", "../view/terms.jsp");
        req.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(req,resp);
    }
}
