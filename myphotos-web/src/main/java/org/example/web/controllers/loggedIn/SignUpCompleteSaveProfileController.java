package org.example.web.controllers.loggedIn;

import org.example.common.cdi.annotation.groups.SignUpGroup;
import org.example.model.model.domain.Profile;
import org.example.web.component.ProfileSignUpServiceProxy;
import org.example.web.security.SecurityUtils;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/sign-up/complete", loadOnStartup = 1)
public class SignUpCompleteSaveProfileController extends AbstractSaveProfileController {

    @Inject
    private ProfileSignUpServiceProxy profileSignUpService;

    @Override
    protected Class<?>[] getValidationGroups() {
        return new Class<?>[] {SignUpGroup.class};
    }

    @Override
    protected Profile getCurrentProfile() {
        return profileSignUpService.getCurrentProfile();
    }

    @Override
    protected void saveProfile(Profile currentProfile) {
        profileSignUpService.completeSignUpProfile();
        this.reLoginAsUserRole(currentProfile);
    }

    private void reLoginAsUserRole(Profile currentProfile) {
        SecurityUtils.logout();
        SecurityUtils.authenticate(currentProfile);
    }

    @Override
    protected String getBackToEditView() {
        return "sign-up";
    }
}
