package org.example.service;

import org.example.exception.ObjectNotFoundException;
import org.example.model.*;
import org.example.model.domain.Photo;
import org.example.model.domain.Profile;

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
