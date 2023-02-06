package org.example.web.controllers;

import org.apache.commons.io.IOUtils;
import org.example.model.model.OriginalImage;
import org.example.model.service.PhotoService;
import org.example.web.utils.UrlExtractorUtil;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(urlPatterns = "/download/*", loadOnStartup = 1)
public class DownloadPhotoController extends HttpServlet {

    @EJB
    private PhotoService photoService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long photoId = Long.parseLong(UrlExtractorUtil.getPathVariableValue(req.getRequestURI(), "/download/", ".jpg"));
        OriginalImage downloadableOriginalPhoto = photoService.getDownloadableOriginalPhotoAndIncrementCountOfDownloads(photoId);

        resp.setHeader("Content-Disposition", "attachment; filename=" + downloadableOriginalPhoto.getName());
        resp.setContentType(this.getServletContext().getMimeType(downloadableOriginalPhoto.getName()));
        resp.setContentLengthLong(downloadableOriginalPhoto.getSize());

        try(InputStream inputStream = downloadableOriginalPhoto.getInputStream();
            OutputStream outputStream = resp.getOutputStream()) {
            IOUtils.copyLarge(inputStream, outputStream);
        }
    }
}
