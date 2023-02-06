package org.example.web.controllers;


import org.example.model.service.PhotoService;
import org.example.web.utils.UrlExtractorUtil;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/preview/*", loadOnStartup = 1)
public class PreviewPhotoController extends HttpServlet {

    @EJB
    private PhotoService photoService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long photoId = Long.parseLong(UrlExtractorUtil.getPathVariableValue(req.getRequestURI(), "/preview/", ".jpg"));
        String urlLargePhoto = photoService.getUrlLargePhotoAndIncrementCountOfViews(photoId);
        resp.sendRedirect(urlLargePhoto);
    }
}
