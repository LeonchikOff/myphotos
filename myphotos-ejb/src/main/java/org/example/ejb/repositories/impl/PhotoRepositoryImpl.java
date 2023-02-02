package org.example.ejb.repositories.impl;

import org.example.ejb.repositories.PhotoRepository;
import org.example.model.model.domain.Photo;

import javax.enterprise.context.Dependent;
import java.util.List;

@Dependent
public class PhotoRepositoryImpl extends AbstractJPARepository<Photo, Long> implements PhotoRepository {


    @Override
    protected Class<Photo> getEntityClass() {
        return Photo.class;
    }

    @Override
    public List<Photo> findProfilePhotosInOrderLatestFirst(Long profileId, int offset, int limit) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int countOfProfilePhotos(Long profileId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Photo> findAllPhotosOrderByViewsDesc(int offset, int limit) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Photo> findAllPhotosOrderByAuthorRating(int offset, int limit) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public long countAllPhotosToDB() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
