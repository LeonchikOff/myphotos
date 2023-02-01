package org.example.ejb.service.beans;

import org.example.ejb.repositories.PhotoRepository;
import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.*;
import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;
import org.example.model.service.PhotoService;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
@Local(PhotoService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PhotoServiceBean implements PhotoService {

    @Inject
    private PhotoRepository photoRepository;

    @Resource
    private SessionContext sessionContext;

    @Override
    public List<Photo> findProfilePhotos(Long profileId, Pageable pageable) {
        return photoRepository.findProfilePhotosInOrderLatestFirst(profileId, pageable.getOffset(), pageable.getLimitElementsPerPage());
    }

    @Override
    public List<Photo> findPopularPhotos(SortMode sortMode, Pageable pageable) {
        switch (sortMode) {
            case POPULAR_AUTHOR:
                return photoRepository.findAllPhotosOrderByAuthorRating(pageable.getOffset(), pageable.getLimitElementsPerPage());
            case POPULAR_PHOTO:
                return photoRepository.findAllPhotosOrderByViewsDesc(pageable.getOffset(), pageable.getLimitElementsPerPage());
            default:
                throw new UnsupportedOperationException("Unsupported sort mode: " + sortMode);
        }
    }

    @Override
    public long countAllPhotos() {
        return photoRepository.countAllPhotosToDB();
    }


    @Override
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void uploadNewPhoto(Profile currentProfile, ImageResource imageResource, AsyncOperation<Photo> photoAsyncOperation) {
        try {
            Photo photo = null; //FIXME
            photoAsyncOperation.onSuccessOperation(photo);

        } catch (Throwable throwable) {
            sessionContext.getRollbackOnly();
            photoAsyncOperation.onFailedOperation(throwable);
        }
    }

    @Override
    public OriginalImage getDownloadableOriginalPhotoAndIncrementCountOfDownloads(Long photoId) throws ObjectNotFoundException {
        Photo photo = this.getPhoto(photoId);
        photo.setCountOfDownloads(photo.getCountOfDownloads() + 1);
        photoRepository.update(photo);

        throw new UnsupportedOperationException("Not implemented yet"); //FIXME
    }

    @Override
    public String getUrlLargePhotoAndIncrementCountOfViews(Long photoId) throws ObjectNotFoundException {
        Photo photo = this.getPhoto(photoId);
        photo.setCountOfViews(photo.getCountOfViews() + 1);
        photoRepository.update(photo);
        return photo.getUrlToLarge();
    }

    public Photo getPhoto(Long photoId) throws ObjectNotFoundException {
        Optional<Photo> optionalPhoto = photoRepository.findById(photoId);
        if (!optionalPhoto.isPresent())
            throw new ObjectNotFoundException(String.format("Photo not found by id: %s ", photoId));
        return optionalPhoto.get();
    }

}
