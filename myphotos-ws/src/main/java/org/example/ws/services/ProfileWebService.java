package org.example.ws.services;

import org.example.ws.model.ProfilePhotosSOAP;
import org.example.ws.model.ProfileSOAP;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(targetNamespace = "http://soap.myphoros.com/ws/ProfileWebService?wsdl")
public interface ProfileWebService {

    @WebMethod
    @WebResult(name = "profile")
    ProfileSOAP findProfileById(
            @WebParam(name = "id") Long profileId,
            @WebParam(name = "withPhotos") boolean withProfilePhotos,
            @WebParam(name = "limit") int limit);

    @WebMethod
    @WebResult(name = "profilePhotos")
    ProfilePhotosSOAP findProfilePhotos(
            @WebParam(name = "id") Long profileId,
            @WebParam(name = "numberOfPage") int numberOfPage,
            @WebParam(name = "limit") int limit);
}
