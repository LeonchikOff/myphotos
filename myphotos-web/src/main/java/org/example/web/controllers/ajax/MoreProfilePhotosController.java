package org.example.web.controllers.ajax;


import org.example.model.model.Pageable;
import org.example.model.model.domain.Photo;
import org.example.model.service.PhotoService;
import org.example.web.Constants;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/photos/profile/more", loadOnStartup = 1)
public class MoreProfilePhotosController extends HttpServlet {

    @EJB
    private PhotoService photoService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long profileId = Long.parseLong(req.getParameter("profileId"));
        int pageNumber = Integer.parseInt(req.getParameter("page"));
        List<Photo> profilePhotos = photoService.findProfilePhotos(profileId, new Pageable(pageNumber, Constants.PHOTO_LIMIT));
        req.setAttribute("photos", profilePhotos);
        req.setAttribute("isProfilePhotos", Boolean.TRUE);
        req.getRequestDispatcher("/WEB-INF/fragment/more-photos.jsp").forward(req, resp);

    }
}
