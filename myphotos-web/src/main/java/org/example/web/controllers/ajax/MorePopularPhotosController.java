package org.example.web.controllers.ajax;


import org.example.model.model.Pageable;
import org.example.model.model.SortMode;
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

@WebServlet(urlPatterns = "/photos/popular/more", loadOnStartup = 1)
public class MorePopularPhotosController extends HttpServlet {

    @EJB
    private PhotoService photoService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SortMode sortModeObtainedFromParam = SortMode.of(req.getParameter("sortMode"));
        int numberOfPageObtainedFromParam = Integer.parseInt(req.getParameter("page"));
        List<Photo> popularPhotos = photoService.findPopularPhotos(
                sortModeObtainedFromParam, new Pageable(numberOfPageObtainedFromParam, Constants.PHOTO_LIMIT));
        req.setAttribute("photos", popularPhotos);
        req.setAttribute("sortMode", sortModeObtainedFromParam.name().toLowerCase());

        req.getRequestDispatcher("/WEB-INF/fragment/more-photos.jsp").forward(req, resp);

    }
}
