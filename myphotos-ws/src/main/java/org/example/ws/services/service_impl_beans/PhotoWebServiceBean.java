package org.example.ws.services.service_impl_beans;

import org.example.common.converter.ModelConverter;
import org.example.common.converter.UrlConverter;
import org.example.model.model.OriginalImage;
import org.example.model.model.Pageable;
import org.example.model.model.SortMode;
import org.example.model.model.domain.Photo;
import org.example.model.service.PhotoService;
import org.example.ws.error.ExceptionMapperInterceptor;
import org.example.ws.model.ImageLinkSOAP;
import org.example.ws.model.PhotosSOAP;
import org.example.ws.model.PhotoSOAP;
import org.example.ws.services.PhotoWebService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

//MTOM -- это механизм оптимизации передачи сообщений W3C , метод эффективной отправки двоичных данных в веб - службы и из них .
//MTOM is the W3C Message Transmission Optimization Mechanism, a method of efficiently sending binary data to and from Web services.
@MTOM
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@WebService(
        portName = "PhotoServicePort",
        serviceName = "PhotoService",
        targetNamespace = "http://soap.myphoros.com/ws/PhotoService?wsdl",
        endpointInterface = "org.example.ws.services.PhotoWebService")
@Interceptors(ExceptionMapperInterceptor.class)
public class PhotoWebServiceBean implements PhotoWebService {

    @EJB
    private PhotoService photoService;

    @Inject
    private UrlConverter urlConverter;

    @Inject
    private ModelConverter modelConverter;


    @Override
    public PhotosSOAP findAllOrderByPhotosPopularity(int numberOfPage, int limit, boolean withTotalCount) {
        return findAll(SortMode.POPULAR_PHOTO, numberOfPage, limit, withTotalCount);
    }

    @Override
    public PhotosSOAP findAllOrderByAuthorPopularity(int numberOfPage, int limit, boolean withTotalCount) {
        return findAll(SortMode.POPULAR_AUTHOR, numberOfPage, limit, withTotalCount);
    }

    private PhotosSOAP findAll(SortMode sortMode, int numberOfPage, int limit, boolean withTotalCount) {
        PhotosSOAP photosSOAP = new PhotosSOAP();
        List<Photo> popularPhotos = photoService.findPopularPhotos(sortMode, new Pageable(numberOfPage, limit));
        List<PhotoSOAP> photosSOAPList = modelConverter.convertList(popularPhotos, PhotoSOAP.class);
        photosSOAP.setPhotos(photosSOAPList);
        if (withTotalCount)
            photosSOAP.setTotal(photoService.countAllPhotos());
        return photosSOAP;
    }


    @Override
    public ImageLinkSOAP viewLargePhoto(Long photoId) {
        String urlLargePhoto = photoService.getUrlLargePhotoAndIncrementCountOfViews(photoId);
        String urlLargePhotoAbsolute = urlConverter.convertRelativeUrlToAbsolute(urlLargePhoto);
        return new ImageLinkSOAP(urlLargePhotoAbsolute);
    }

    @Override
    public DataHandler downloadOriginalPhoto(Long photoId) {
        OriginalImage downloadableOriginalPhoto =
                photoService.getDownloadableOriginalPhotoAndIncrementCountOfDownloads(photoId);
        return new DataHandler(new OriginalImageDataSource(downloadableOriginalPhoto));
    }

    private static class OriginalImageDataSource implements DataSource {

        private final OriginalImage originalImage;

        public OriginalImageDataSource(OriginalImage downloadableOriginalPhoto) {
            this.originalImage = downloadableOriginalPhoto;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return originalImage.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException("Output stream not supported");
        }

        @Override
        public String getContentType() {
            return "image/jpeg";
        }

        @Override
        public String getName() {
            return originalImage.getName();
        }
    }
}
