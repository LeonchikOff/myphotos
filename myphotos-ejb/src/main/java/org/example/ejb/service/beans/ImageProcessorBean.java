package org.example.ejb.service.beans;


import org.example.common.config.ImageCategory;
import org.example.ejb.service.ImageResizerService;
import org.example.ejb.service.ImageStorageService;
import org.example.ejb.service.interceptors.ImageResourceInterceptor;
import org.example.model.model.ImageResource;
import org.example.model.model.ImageResourceTempImpl;
import org.example.model.model.domain.Photo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ImageProcessorBean {

    @Inject
    private ImageResizerService imageResizerService;

    @Inject
    private ImageStorageService imageStorageService;


    @Interceptors(value = {ImageResourceInterceptor.class})
    public String processProfileAvatarAndGetUrl(ImageResource imageResource) {
        return createResizedImageAndGetUrl(imageResource, ImageCategory.PROFILE_AVATAR);
    }

    @Interceptors(value = {ImageResourceInterceptor.class})
    public Photo processPhotoAndGet(ImageResource imageResource) {
        Photo photo = new Photo();
        photo.setUrlToLarge(this.createResizedImageAndGetUrl(imageResource, ImageCategory.LARGE_PHOTO));
        photo.setUrlToSmall(this.createResizedImageAndGetUrl(imageResource, ImageCategory.SMALL_PHOTO));
        photo.setUrlToOriginal(imageStorageService.saveProtectedOriginalImage(imageResource.getTempPath()));
        return photo;
    }

    private String createResizedImageAndGetUrl(ImageResource imageResource, ImageCategory imageCategory) {
        try(ImageResourceTempImpl tempImageResource = new ImageResourceTempImpl()) {
            imageResizerService.resize(imageResource.getTempPath(), tempImageResource.getTempPath(), imageCategory);
            return imageStorageService.savePublicImage(imageCategory, tempImageResource.getTempPath());
        }
    }
}
