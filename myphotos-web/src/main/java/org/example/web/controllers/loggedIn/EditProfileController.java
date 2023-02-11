package org.example.web.controllers.loggedIn;

import org.example.model.model.domain.Profile;
import org.example.web.forms.ProfileForm;
import org.example.web.security.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/edit", loadOnStartup = 1)
public class EditProfileController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile currentProfile = SecurityUtils.getCurrentProfile();
        req.setAttribute("profile", new ProfileForm(currentProfile));

        req.setAttribute("dynamicPage", "../view/edit.jsp");
        req.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(req,resp);
    }
}
