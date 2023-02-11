package org.example.ws.services;

import org.example.ws.model.ImageLinkSOAP;
import org.example.ws.model.PhotosSOAP;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(targetNamespace = "http://soap.myphotos.com/ws/PhotoWebService?wsdl")
public interface PhotoWebService {
    @WebMethod
    @WebResult(name = "photos")
    PhotosSOAP findAllOrderByPhotosPopularity(
            @WebParam(name = "numberOfPage") int numberOfPage,
            @WebParam(name = "limit") int limit,
            @WebParam(name = "withTotalCount") boolean withTotalCount);

    @WebMethod
    @WebResult(name = "photos")
    PhotosSOAP findAllOrderByAuthorPopularity(
            @WebParam(name = "numberOfPage") int numberOfPage,
            @WebParam(name = "limit") int limit,
            @WebParam(name = "withTotalCount") boolean withTotalCount);

    @WebMethod
    @WebResult(name = "imageLink")
    ImageLinkSOAP viewLargePhoto(
            @WebParam(name = "photoId") Long photoId);

    @WebMethod
    @WebResult(name = "originalPhoto")
    DataHandler downloadOriginalPhoto(
            @WebParam(name = "photoId") Long photoId);

}
