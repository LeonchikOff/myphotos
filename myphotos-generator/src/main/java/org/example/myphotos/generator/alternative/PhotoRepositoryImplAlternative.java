package org.example.myphotos.generator.alternative;

import org.example.ejb.repositories.impl.PhotoRepositoryImpl;

import javax.enterprise.context.Dependent;

@Dependent
@TestDataGeneratorEnvironment
public class PhotoRepositoryImplAlternative extends PhotoRepositoryImpl {
    @Override
    public int countOfProfilePhotos(Long profileId) {
        Object countObj = entityManager
                .createQuery("SELECT COUNT(ph) FROM Photo ph WHERE ph.profile.id=:profileId")
                .setParameter("profileId", profileId)
                .getSingleResult();
        return ((Number) countObj).intValue();
    }
}
