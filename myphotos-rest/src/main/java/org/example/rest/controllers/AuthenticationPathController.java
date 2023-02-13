package org.example.rest.controllers;

import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.example.common.cdi.annotation.qualifier.Facebook;
import org.example.common.cdi.annotation.qualifier.GooglePlus;
import org.example.common.converter.ModelConverter;
import org.example.model.exception.FailedRetrievalSocialDataException;
import org.example.model.model.domain.AccessToken;
import org.example.model.model.domain.Profile;
import org.example.model.service.AccessTokenService;
import org.example.model.service.ProfileService;
import org.example.model.service.SocialService;
import org.example.rest.Constants;
import org.example.rest.StatusMessage;
import org.example.rest.converters.ConstraintValidationConverter;
import org.example.rest.model.*;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;

@Api("auth")
@ApiResponses(value = {
        @ApiResponse(code = 500, message = StatusMessage.INTERNAL_ERROR, response = ErrorMessageRest.class),
        @ApiResponse(code = 502, message = StatusMessage.SERVICE_UNAVAILABLE),
        @ApiResponse(code = 503, message = StatusMessage.SERVICE_UNAVAILABLE),
        @ApiResponse(code = 504, message = StatusMessage.SERVICE_UNAVAILABLE)
})
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthenticationPathController {

    @Inject
    @GooglePlus
    private SocialService googlePlusSocialService;

    @Inject
    @Facebook
    private SocialService facebookSocialService;

    @EJB
    private ProfileService profileService;

    @EJB
    private AccessTokenService accessTokenService;

    @Inject
    private ModelConverter modelConverter;

    @Inject
    ConstraintValidationConverter constraintValidationConverter;

    @Resource(lookup = "java:comp/Validator")
    private Validator validator;

    @POST
    @Path("/sign-in/facebook")
    @ApiOperation(value = "Signs in via facebook", response = SimpleProfileRest.class,
            notes = "Parameter 'code' - is code retrieved from facebook after successful authentication and authorization")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Invalid authentication code", response = ErrorMessageRest.class),
            @ApiResponse(code = 404, message = "Profile not found. It is necessary to sign up. Response body contains retrieved data from facebook", response = ProfileRest.class)
    })
    public Response facebookSignIn(AuthenticationCodeRest authenticationCodeRest) {
        return auth(authenticationCodeRest, facebookSocialService);
    }

    @POST
    @Path("/sign-up/facebook")
    public Response facebookSignUp(SignUpProfileRest signUpProfileRest) {
        return auth(signUpProfileRest, facebookSocialService);
    }

    @POST
    @Path("/sign-in/google-plus")
    public Response googlePlusSignIn(AuthenticationCodeRest authenticationCodeRest) {
        return auth(authenticationCodeRest, googlePlusSocialService);
    }

    @POST
    @Path("/sign-up/google-plus")
    public Response googlePlusSignUp(SignUpProfileRest signUpProfileRest) {
        return auth(signUpProfileRest, googlePlusSocialService);
    }

    @POST
    @Path("/sign-out")
    @ApiOperation(value = "Invalidates access token on server", notes = "If response status = 401, in means that sign out success")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = StatusMessage.INVALID_ACCESS_TOKEN, response = ErrorMessageRest.class)
    })
    public Response signOut(
            @ApiParam(value = "Access token", required = true)
            @HeaderParam(Constants.ACCESS_TOKEN_HEADER)
                    String token) {
        accessTokenService.invalidateAccessToken(token);
        return Response.ok().build();
    }


    protected Response auth(AuthenticationCodeRest model, SocialService socialService) {
        if (StringUtils.isBlank(model.getCode())) {
            throw new FailedRetrievalSocialDataException("Code is required");
        }
        Profile fetchedProfile = socialService.fetchProfile(model.getCode());
        Optional<Profile> profileOptionalByEmail = profileService.findByEmail(fetchedProfile.getEmail());
        if (profileOptionalByEmail.isPresent()) {
            Profile signedInProfile = profileOptionalByEmail.get();
            AccessToken accessToken = accessTokenService.generateAccessToken(signedInProfile);
            return buildResponse(Response.Status.OK, signedInProfile, Optional.of(accessToken.getToken()), SimpleProfileRest.class);
        } else if (model instanceof SignUpProfileRest) {
            SignUpProfileRest signUpProfile = (SignUpProfileRest) model;
            Set<ConstraintViolation<SignUpProfileRest>> constraintViolations =
                    validator.validate(signUpProfile, Default.class);
            if (constraintViolations.isEmpty()) {
                Profile profile = new Profile();
                signUpProfile.copyToProfile(profile);
                profileService.signUpWithDeliveryToDB(profile, true);
                AccessToken accessToken = accessTokenService.generateAccessToken(profile);
                return buildResponse(Response.Status.OK, profile, Optional.of(accessToken.getToken()), SimpleProfileRest.class);
            } else {
                ValidationResultRest validationResultRest = constraintValidationConverter.convert(constraintViolations);
                return Response.status(Response.Status.BAD_REQUEST).entity(validationResultRest).build();
            }
        } else {
            profileService.translateSocialProfile(fetchedProfile);
            return buildResponse(Response.Status.NOT_FOUND, fetchedProfile, Optional.empty(), ProfileRest.class);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected Response buildResponse(
            Response.Status status, Profile profile, Optional<String> accessToken, Class<?> resultClass) {
        Response.ResponseBuilder responseBuilder = Response.status(status);
        responseBuilder.entity(modelConverter.convert(profile, resultClass));
        accessToken.ifPresent(token
                -> responseBuilder.header(Constants.ACCESS_TOKEN_HEADER, token));
        return responseBuilder.build();
    }

}
