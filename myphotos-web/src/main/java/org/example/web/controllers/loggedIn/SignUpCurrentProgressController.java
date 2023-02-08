package org.example.web.controllers.loggedIn;

import org.example.model.model.domain.Profile;
import org.example.web.component.ProfileSignUpServiceProxy;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/sign-up", loadOnStartup = 1)
public class SignUpCurrentProgressController extends HttpServlet {

    @Inject
    private ProfileSignUpServiceProxy profileSignUpService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile currentProfile = profileSignUpService.getCurrentProfile();
        System.out.println("CURRENT PROFILE: " + currentProfile.toString());
        req.setAttribute("profile", currentProfile);
//        RoutingUtil.forwardToPage("home",req, resp);
        req.setAttribute("dynamicPage", "../view/sign-up.jsp");
        req.getRequestDispatcher("WEB-INF/template/page-template.jsp").forward(req, resp);
    }
}
