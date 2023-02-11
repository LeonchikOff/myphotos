package org.example.web.controllers.social;


import org.example.common.cdi.annotation.qualifier.Facebook;
import org.example.model.service.SocialService;
import org.example.web.security.SecurityUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/sign-up/facebook", loadOnStartup = 1)
public class SignUpViaFacebookController extends HttpServlet {

    @Inject
    @Facebook
    private SocialService socialService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SecurityUtils.isAuthenticated()) {
            if (SecurityUtils.isTempAuthenticated()) {
                resp.sendRedirect("/sign-up");
            } else {
                resp.sendRedirect("/" + SecurityUtils.getCurrentProfile().getUid());
            }
        } else {
            resp.sendRedirect(socialService.getAuthorizeUrl());
        }
    }
}
