package org.example.web.controllers.social;

import org.example.model.model.domain.Profile;
import org.example.model.service.ProfileService;
import org.example.model.service.SocialService;
import org.example.web.component.ProfileSignUpServiceProxy;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public abstract class SignUpAbstractController extends HttpServlet {

    @EJB
    protected ProfileService profileService;


    @Inject
    protected ProfileSignUpServiceProxy profileSignUpService;

    protected SocialService socialService;

    protected abstract void setSocialService(SocialService socialService);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> codeParam = Optional.ofNullable(req.getParameter("code"));
        if (codeParam.isPresent())
            processSignUp(codeParam.get(), req, resp);
        else
            resp.sendRedirect("/");
    }

    private void processSignUp(String code, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Profile fetchProfile = socialService.fetchProfile(code);
        Optional<Profile> optionalProfileFromDB = profileService.findByEmail(fetchProfile.getEmail());
        if (optionalProfileFromDB.isPresent()) {
            Profile profileFromDB = optionalProfileFromDB.get();
            //TODO authenticate
            resp.sendRedirect("/" + profileFromDB.getUid());
        } else {
            //TODO authenticate
            profileSignUpService.createSignUpProfile(fetchProfile);
            resp.sendRedirect("/sign-up");
        }

    }
}
