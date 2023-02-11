package org.example.web.controllers.loggedIn;

import org.example.common.cdi.annotation.groups.ProfileUpdateGroup;
import org.example.model.model.domain.Profile;
import org.example.model.service.ProfileService;
import org.example.web.security.SecurityUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/save", loadOnStartup = 1)
public class SaveProfileController extends AbstractSaveProfileController{

    @EJB
    private ProfileService profileService;

    @Override
    protected Class<?>[] getValidationGroups() {
        return new Class<?>[]{ProfileUpdateGroup.class};
    }

    @Override
    protected Profile getCurrentProfile() {
        return SecurityUtils.getCurrentProfile();
    }

    @Override
    protected void saveProfile(Profile currentProfile) {
        profileService.update(currentProfile);
    }

    @Override
    protected String getBackToEditView() {
        return "edit";
    }
}
