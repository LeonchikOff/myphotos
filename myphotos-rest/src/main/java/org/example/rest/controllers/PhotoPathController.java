package org.example.rest.controllers;

import io.swagger.annotations.Api;
import org.example.common.converter.ModelConverter;
import org.example.common.converter.UrlConverter;
import org.example.model.model.OriginalImage;
import org.example.model.model.Pageable;
import org.example.model.model.SortMode;
import org.example.model.model.domain.Photo;
import org.example.model.service.PhotoService;
import org.example.rest.Constants;
import org.example.rest.model.ImageLinkRest;
import org.example.rest.model.PhotoRest;
import org.example.rest.model.PhotosRest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api("photo")
@RequestScoped
@Path("/photo")
@Produces(MediaType.APPLICATION_JSON)
public class PhotoPathController {

    @EJB
    private PhotoService photoService;

    @Inject
    private UrlConverter urlConverter;

    @Inject
    private ModelConverter modelConverter;


    @GET
    @Path("/all")
    public PhotosRest findAllOrderByPhotosPopularity(
            @DefaultValue("popular_photo") @QueryParam("sortMode") String sortMode,
            @DefaultValue("1") @QueryParam("numberOfPage") int numberOfPage,
            @DefaultValue(Constants.PHOTO_LIMIT) @QueryParam("limit") int limit,
            @DefaultValue("false") @QueryParam("withTotalCount") boolean withTotalCount) {
        PhotosRest photosRest = new PhotosRest();
        List<Photo> popularPhotos = photoService.findPopularPhotos(SortMode.of(sortMode), new Pageable(numberOfPage, limit));
        List<PhotoRest> photoRestList = modelConverter.convertList(popularPhotos, PhotoRest.class);
        photosRest.setPhotos(photoRestList);
        if (withTotalCount) {
            photosRest.setTotal(photoService.countAllPhotos());
        }
        return photosRest;
    }

    @GET
    @Path("/preview/{photoId}")
    public ImageLinkRest viewLargePhoto(
            @PathParam("photoId") Long photoId) {
        String urlLargePhoto = photoService.getUrlLargePhotoAndIncrementCountOfViews(photoId);
        String absoluteUrlLargePhoto = urlConverter.convertRelativeUrlToAbsolute(urlLargePhoto);
        return new ImageLinkRest(absoluteUrlLargePhoto);
    }


    @GET
    @Path("/download/{photoId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadOriginalPhoto(
            @PathParam("photoId") Long photoId) {
        OriginalImage downloadableOriginalPhoto =
                photoService.getDownloadableOriginalPhotoAndIncrementCountOfDownloads(photoId);
        Response.ResponseBuilder responseBuilder =
                Response.ok(downloadableOriginalPhoto.getInputStream(), MediaType.APPLICATION_OCTET_STREAM);
        responseBuilder.header("Content-Disposition", "attachment; filename=" + downloadableOriginalPhoto.getName());
        return responseBuilder.build();
    }

}
