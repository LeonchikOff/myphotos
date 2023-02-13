package org.example.rest.controllers;

import io.swagger.annotations.Api;
import org.example.common.converter.ModelConverter;
import org.example.model.model.Pageable;
import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;
import org.example.model.service.PhotoService;
import org.example.model.service.ProfileService;
import org.example.rest.Constants;
import org.example.rest.model.PhotosRest;
import org.example.rest.model.ProfilePhotoRest;
import org.example.rest.model.ProfileWithPhotosRest;

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
import java.util.List;

@Api("profile")
@RequestScoped
@Path(value = "/profile")
@Produces(value = {MediaType.APPLICATION_JSON})
public class PublicProfilePathController {

    @EJB
    private ProfileService profileService;

    @EJB
    private PhotoService photoService;

    @Inject
    private ModelConverter modelConverter;


    @GET
    @Path("/{profileId}")
    public ProfileWithPhotosRest findProfileById(
            @PathParam("profileId")
                    Long profileId,
            @DefaultValue("false")
            @QueryParam("withProfilePhotos")
                    boolean withProfilePhotos,
            @DefaultValue(Constants.PHOTO_LIMIT)
            @QueryParam("limit")
                    int limit) {
        Profile profile = profileService.findById(profileId);
        ProfileWithPhotosRest profileWithPhotosRest = modelConverter.convert(profile, ProfileWithPhotosRest.class);
        if (withProfilePhotos) {
            List<Photo> profilePhotos = photoService.findProfilePhotos(profileWithPhotosRest.getId(), new Pageable(limit));
            List<ProfilePhotoRest> profilePhotosRest = modelConverter.convertList(profilePhotos, ProfilePhotoRest.class);
            profileWithPhotosRest.setPhotos(profilePhotosRest);
        }
        return profileWithPhotosRest;
    }

    @GET
    @Path("/{profileId}/photos")
    public PhotosRest findProfilePhotos(
            @PathParam("profileId")
                    Long profileId,
            @DefaultValue("1")
            @QueryParam("numberOfPage")
                    int numberOfPage,
            @DefaultValue(Constants.PHOTO_LIMIT)
            @QueryParam("limit")
                    int limit) {
        List<Photo> profilePhotos = photoService.findProfilePhotos(profileId, new Pageable(numberOfPage, limit));
        List<ProfilePhotoRest> profilePhotosRest = modelConverter.convertList(profilePhotos, ProfilePhotoRest.class);
        return new PhotosRest(profilePhotosRest);
    }
}
