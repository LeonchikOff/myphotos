package org.example.model.service;

import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.*;
import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;

import java.util.List;


public interface PhotoService {

    List<Photo> findProfilePhotos(Long profileId, Pageable pageable);
    List<Photo> findPopularPhotos(SortMode sortMode, Pageable pageable);
    long countAllPhotos();

    void uploadNewPhoto(Profile currentProfile, ImageResource imageResource, AsyncOperation<Photo> photoAsyncOperation);


    OriginalImage getDownloadableOriginalPhotoAndIncrementCountOfDownloads(Long photoId)
            throws ObjectNotFoundException;
    String getUrlLargePhotoAndIncrementCountOfViews(Long photoId)
            throws ObjectNotFoundException;
}
