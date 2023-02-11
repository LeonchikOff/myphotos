package org.example.web.controllers.loggedIn;

import org.example.model.model.domain.Profile;
import org.example.web.component.ConstraintViolationConverter;
import org.example.web.component.FormReader;
import org.example.web.forms.ProfileForm;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

public abstract class AbstractSaveProfileController extends HttpServlet {

    @Resource(lookup = "java:comp/Validator")
    private Validator validator;

    @Inject
    private ConstraintViolationConverter constraintViolationConverter;

    @Inject
    private FormReader formReader;

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    protected abstract Class<?>[] getValidationGroups();

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileForm profileForm = formReader.readForm(req, ProfileForm.class);
        Set<ConstraintViolation<ProfileForm>> constraintViolations = validator.validate(profileForm, getValidationGroups());
        if (constraintViolations.isEmpty()) {
            saveChanges(profileForm, resp);
        } else {
            backToEditForm(req, profileForm, constraintViolations, resp);
        }
    }


    protected abstract Profile getCurrentProfile();

    protected abstract void saveProfile(Profile currentProfile);

    private void saveChanges(ProfileForm profileForm, HttpServletResponse resp) throws IOException {
        Profile currentProfile = this.getCurrentProfile();
        profileForm.copyToProfile(currentProfile);
        saveProfile(currentProfile);
        resp.sendRedirect("/" + currentProfile.getUid());
    }

    protected abstract String getBackToEditView();

    private void backToEditForm(HttpServletRequest req, ProfileForm profileForm,
                                Set<ConstraintViolation<ProfileForm>> constraintViolations, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("profile", profileForm);
        req.setAttribute("violations", constraintViolationConverter.convert(constraintViolations));

        req.setAttribute("dynamicPage", String.format("../view/%s.jsp", getBackToEditView()));
        req.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(req, resp);
    }


}












