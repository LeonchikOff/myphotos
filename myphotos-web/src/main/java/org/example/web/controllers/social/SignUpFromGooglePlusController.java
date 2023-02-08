package org.example.web.controllers.social;


import org.example.common.cdi.annotation.qualifier.GooglePlus;
import org.example.model.service.SocialService;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/from/google-plus", loadOnStartup = 1)
public class SignUpFromGooglePlusController extends SignUpAbstractController {

    @Inject
    @Override
    protected void setSocialService(@GooglePlus SocialService socialService) {
        this.socialService = socialService;
    }
}
