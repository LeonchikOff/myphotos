package org.example.web.controllers.loggedIn;

import org.example.common.config.Constants;
import org.example.model.model.AsyncOperation;
import org.example.model.model.domain.Profile;
import org.example.model.service.ProfileService;
import org.example.web.models.PartImageResource;

import javax.ejb.EJB;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@WebServlet(urlPatterns = "/upload-avatar",asyncSupported = true, loadOnStartup = 1)
@MultipartConfig(maxFileSize = Constants.MAX_UPLOADED_PHOTO_SIZE_IN_BYTES)
public class UploadAvatarController extends AbstractUploadController<Profile>{

    @EJB
    private ProfileService profileService;

    @Override
    protected void uploadImage(Profile profile, PartImageResource partImageResource, AsyncOperation<Profile> asyncOperation) {
        profileService.uploadNewAvatar(profile, partImageResource, asyncOperation);
    }

    @Override
    protected Map<String, String> getResultMap(Profile result, HttpServletRequest request) {
        return Collections.singletonMap("thumbnailUrl", result.getAvatarUrl());
    }
}
