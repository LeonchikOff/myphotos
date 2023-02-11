package org.example.web.controllers.loggedIn;

import org.example.common.config.Constants;
import org.example.model.model.AsyncOperation;
import org.example.model.model.Pageable;
import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;
import org.example.model.service.PhotoService;
import org.example.web.models.PartImageResource;
import org.example.web.security.SecurityUtils;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/upload-photos", asyncSupported = true, loadOnStartup = 1)
@MultipartConfig(maxFileSize = Constants.MAX_UPLOADED_PHOTO_SIZE_IN_BYTES)
public class UploadPhotosController extends AbstractUploadController<Photo>{

    @EJB
    private PhotoService photoService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile currentProfile = SecurityUtils.getCurrentProfile();
        List<Photo> profilePhotos =
                photoService.findProfilePhotos(currentProfile.getId(),
                        new Pageable(1, org.example.web.Constants.PHOTO_LIMIT - 1));
        req.setAttribute("profile", currentProfile);
        req.setAttribute("photos", profilePhotos);
        req.setAttribute("isProfilePhotos", Boolean.TRUE);

        req.setAttribute("dynamicPage", "../view/upload-photos.jsp");
        req.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(req, resp);

    }

    @Override
    protected void uploadImage(Profile profile, PartImageResource partImageResource, AsyncOperation<Photo> asyncOperation) {
        photoService.uploadNewPhoto(profile, partImageResource, asyncOperation);
    }

    @Override
    protected Map<String, String> getResultMap(Photo photo, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("smallUrl", photo.getUrlToSmall());
        map.put("largeUrl", String.format("/preview/%s.jpg", photo.getId()));
        map.put("originalUrl", String.format("/download/%s.jpg", photo.getId()));
        map.put("views", String.valueOf(photo.getCountOfViews()));
        map.put("downloads", String.valueOf(photo.getCountOfDownloads()));
        map.put("created", DateFormat.getDateInstance(DateFormat.SHORT, request.getLocale()).format(photo.getDateOfCreated()));
        return map;
    }
}
