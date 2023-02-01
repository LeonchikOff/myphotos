package org.example.web.controllers;

import org.example.model.model.Pageable;
import org.example.model.model.SortMode;
import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;
import org.example.model.service.PhotoService;
import org.example.model.service.ProfileService;
import org.example.web.Constants;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class ProfileController extends HttpServlet {

    @EJB
    private ProfileService profileService;
    @EJB
    private PhotoService photoService;

    private final List<String> homeUrl;

    public ProfileController() {
        this.homeUrl = Collections.unmodifiableList(Arrays.asList("/"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        if (homeUrl.contains(requestURI)) {
            Optional<String> sortModePramOptional = Optional.ofNullable(req.getParameter("sort"));
            SortMode sortMode = sortModePramOptional.map(new Function<String, SortMode>() {
                @Override
                public SortMode apply(String nameOfSortMode) {
                    return SortMode.of(nameOfSortMode);
                }
            }).orElse(SortMode.POPULAR_PHOTO);
            List<Photo> popularPhotos = photoService.findPopularPhotos(sortMode, new Pageable(1, Constants.PHOTO_LIMIT));
            long countAllPhotos = photoService.countAllPhotos();
            req.setAttribute("sortMode", sortMode.name().toLowerCase());
            req.setAttribute("photos", popularPhotos);
            req.setAttribute("countAllPhotos", countAllPhotos);

            req.setAttribute("dynamicPage", "../view/home.jsp");
        } else {
            String uidProfile = requestURI.substring(1);
            Profile profile = profileService.findByUid(uidProfile);
            List<Photo> profilePhotos = photoService.findProfilePhotos(profile.getId(), new Pageable(1, Constants.PHOTO_LIMIT));
            req.setAttribute("profile", profile);
            req.setAttribute("isProfilePhotos", Boolean.TRUE);
            req.setAttribute("photos", profilePhotos);
            req.setAttribute("dynamicPage", "../view/profile.jsp");
        }
        req.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(req, resp);
    }

}
