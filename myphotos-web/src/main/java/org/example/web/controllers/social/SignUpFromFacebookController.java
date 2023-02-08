package org.example.web.controllers.social;

import org.example.common.cdi.annotation.qualifier.Facebook;
import org.example.model.service.SocialService;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/from/facebook", loadOnStartup = 1)
public class SignUpFromFacebookController extends SignUpAbstractController {

    @Inject
    @Override
    protected void setSocialService(@Facebook SocialService socialService) {
        this.socialService = socialService;
    }
}
