package org.example.ws.services.service_impl_beans;

import org.example.common.converter.ModelConverter;
import org.example.model.model.Pageable;
import org.example.model.model.domain.Photo;
import org.example.model.service.PhotoService;
import org.example.model.service.ProfileService;
import org.example.ws.error.ExceptionMapperInterceptor;
import org.example.ws.model.ProfilePhotosSOAP;
import org.example.ws.model.ProfilePhotoSOAP;
import org.example.ws.model.ProfileSOAP;
import org.example.ws.services.ProfileWebService;

import javax.inject.Singleton;
import javax.inject.Inject;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import java.util.List;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@WebService(
        portName = "ProfileServicePort",
        serviceName = "ProfileService",
        targetNamespace = "http://soap.myphoros.com/ws/ProfileService?wsdl",
        endpointInterface = "org.example.ws.services.ProfileWebService")
@Interceptors(ExceptionMapperInterceptor.class)
public class ProfileWebServiceBean implements ProfileWebService {
    @EJB
    private ProfileService profileService;

    @EJB
    private PhotoService photoService;

    @Inject
    ModelConverter modelConverter;

    @Override
    public ProfileSOAP findProfileById(Long profileId, boolean withProfilePhotos, int limit) {
        ProfileSOAP profileSOAP = modelConverter.convert(profileService.findById(profileId), ProfileSOAP.class);
        if (withProfilePhotos) {
            List<Photo> profilePhotos = photoService.findProfilePhotos(profileSOAP.getId(), new Pageable(limit));
            List<ProfilePhotoSOAP> profilePhotosSOAPS = modelConverter.convertList(profilePhotos, ProfilePhotoSOAP.class);
            profileSOAP.setProfilePhotos(profilePhotosSOAPS);
        }
        return profileSOAP;
    }

    @Override
    public ProfilePhotosSOAP findProfilePhotos(Long profileId, int numberOfPage, int limit) {
        ProfilePhotosSOAP profilePhotosSOAP = new ProfilePhotosSOAP();
        List<Photo> profilePhotos = photoService.findProfilePhotos(profileId, new Pageable(numberOfPage, limit));
        List<ProfilePhotoSOAP> profilePhotosSOAPList = modelConverter.convertList(profilePhotos, ProfilePhotoSOAP.class);
        profilePhotosSOAP.setPhotos(profilePhotosSOAPList);
        return profilePhotosSOAP;
    }
}
