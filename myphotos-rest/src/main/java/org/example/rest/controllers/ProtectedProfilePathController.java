package org.example.rest.controllers;

import io.swagger.annotations.Api;
import org.example.common.converter.UrlConverter;
import org.example.model.model.AsyncOperation;
import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;
import org.example.model.service.AccessTokenService;
import org.example.model.service.PhotoService;
import org.example.model.service.ProfileService;
import org.example.rest.Constants;
import org.example.rest.converters.ConstraintValidationConverter;
import org.example.rest.model.*;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.example.common.config.Constants.DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS;

@Api("profile")
@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ProtectedProfilePathController {

    @EJB
    private AccessTokenService accessTokenService;

    @EJB
    private ProfileService profileService;

    @EJB
    private PhotoService photoService;

    @Resource(lookup = "java:comp/Validator")
    private Validator validator;

    @Inject
    private UrlConverter urlConverter;

    @Inject
    private ConstraintValidationConverter constraintValidationConverter;


    @PUT
    @Path("/{profileId}")
    public Response updateProfile(
            @PathParam("profileId") Long profileId,
            @HeaderParam(Constants.ACCESS_TOKEN_HEADER) String accessToken,
            UpdateProfileRest profileUpdate) {
        Profile profileWithAccessToken = accessTokenService.findProfileWhereAccessTokenBelongsToProfile(accessToken, profileId);
        Set<ConstraintViolation<UpdateProfileRest>> violations = validator.validate(profileUpdate, Default.class);
        if (violations.isEmpty()) {
            profileUpdate.copyToProfile(profileWithAccessToken);
            profileService.update(profileWithAccessToken);
            return Response.ok().build();
        } else {
            ValidationResultRest validationResultRest = constraintValidationConverter.convert(violations);
            return Response.status(Response.Status.BAD_REQUEST).entity(validationResultRest).build();
        }
    }

    @POST
    @Path("/{profileId}/avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void updateAvatar(
            @Suspended AsyncResponse asyncResponse,
            @PathParam("profileId") Long profileId,
            @HeaderParam(Constants.ACCESS_TOKEN_HEADER) String accessToken,
            UploadImageRest uploadImageRest) {
        Profile profileWithAccessToken =
                accessTokenService.findProfileWhereAccessTokenBelongsToProfile(accessToken, profileId);
        asyncResponse.setTimeout(DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS);
        profileService.uploadNewAvatar(profileWithAccessToken, uploadImageRest.getImageResource(), new AsyncOperation<Profile>() {
            @Override
            public long getTimeOutOfOperationInMillis() {
                return DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS;
            }

            @Override
            public void onSuccessOperation(Profile result) {
                String absoluteAvatarUrl = urlConverter.convertRelativeUrlToAbsolute(result.getAvatarUrl());
                asyncResponse.resume(new ImageLinkRest(absoluteAvatarUrl));
            }

            @Override
            public void onFailedOperation(Throwable throwable) {
                asyncResponse.resume(throwable);
            }
        });
    }

    @POST
    @Path("/{profileId}/photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadPhoto(
            @Suspended AsyncResponse asyncResponse,
            @PathParam("profileId") Long profileId,
            @HeaderParam(Constants.ACCESS_TOKEN_HEADER) String accessToken,
            UploadImageRest uploadImageRest) {
        Profile profileWithAccessToken =
                accessTokenService.findProfileWhereAccessTokenBelongsToProfile(accessToken, profileId);
        asyncResponse.setTimeout(DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS);
        photoService.uploadNewPhoto(profileWithAccessToken, uploadImageRest.getImageResource(), new AsyncOperation<Photo>() {
            @Override
            public long getTimeOutOfOperationInMillis() {
                return DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS;
            }

            @Override
            public void onSuccessOperation(Photo result) {
                String absoluteSmallPhotoUrl = urlConverter.convertRelativeUrlToAbsolute(result.getUrlToSmall());
                asyncResponse.resume(new UploadPhotoResultRest(result.getId(), absoluteSmallPhotoUrl));
            }

            @Override
            public void onFailedOperation(Throwable throwable) {
                asyncResponse.resume(throwable);
            }
        });
    }


}
