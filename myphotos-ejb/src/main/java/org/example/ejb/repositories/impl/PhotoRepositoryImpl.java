package org.example.ejb.repositories.impl;

import org.example.ejb.repositories.PhotoRepository;
import org.example.ejb.repositories.impl.StaticJPAQueryInitializer.JPAQuery;
import org.example.model.model.domain.Photo;

import javax.enterprise.context.Dependent;
import java.util.List;

@SuppressWarnings("JpaQueryApiInspection")
@Dependent
public class PhotoRepositoryImpl extends AbstractJPARepository<Photo, Long> implements PhotoRepository {


    @Override
    protected Class<Photo> getEntityClass() {
        return Photo.class;
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT ph FROM Photo ph WHERE ph.profile.id=:profileId ORDER BY ph.id DESC")
    public List<Photo> findProfilePhotosInOrderLatestFirst(Long profileId, int offset, int limit) {
        return entityManager
                .createNamedQuery("Photo.findProfilePhotosInOrderLatestFirst", Photo.class)
                .setParameter("profileId", profileId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT COUNT(ph) FROM Photo ph WHERE ph.profile.id=:profileId")
    public int countOfProfilePhotos(Long profileId) {
        return entityManager
                .createNamedQuery("Photo.countOfProfilePhotos", Integer.class)
                .setParameter("profileId", profileId)
                .getSingleResult();
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT ph FROM Photo ph JOIN FETCH ph.profile ORDER BY ph.countOfViews DESC")
    public List<Photo> findAllPhotosOrderByViewsDesc(int offset, int limit) {
        return entityManager
                .createNamedQuery("Photo.findAllPhotosOrderByViewsDesc", Photo.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT ph FROM Photo ph JOIN FETCH ph.profile pr ORDER BY pr.rating DESC, ph.countOfViews DESC")
    public List<Photo> findAllPhotosOrderByAuthorRating(int offset, int limit) {
        return entityManager
                .createNamedQuery("Photo.findAllPhotosOrderByAuthorRating",Photo.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT COUNT(ph) FROM Photo ph")
    public long countAllPhotosToDB() {
        return entityManager
                .createNamedQuery("Photo.countAllPhotosToDB", Long.class)
                .getSingleResult();
    }
}

