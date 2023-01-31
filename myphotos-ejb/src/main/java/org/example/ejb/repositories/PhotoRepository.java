package org.example.ejb.repositories;

import org.example.model.model.domain.Photo;

import java.util.List;

public interface PhotoRepository extends EntityRepository<Photo, Long> {

//    Profile
    List<Photo> findProfilePhotosInOrderLatestFirst(Long profileId, int offset, int limit);

    int countOfProfilePhotos(Long profileId);

//    General
    List<Photo> findAllPhotosOrderByViewsDesc(int offset, int limit);

    List<Photo> findAllPhotosOrderByAuthorRating(int offset, int limit);

    long countAllPhotosToDB();
}
