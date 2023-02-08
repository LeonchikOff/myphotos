package org.example.ejb.repositories.impl;

import org.example.ejb.repositories.ProfileRepository;
import org.example.ejb.repositories.impl.StaticJPAQueryInitializer.JPAQuery;
import org.example.model.model.domain.Profile;

import javax.enterprise.context.Dependent;
import javax.persistence.NoResultException;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("JpaQueryApiInspection")
@Dependent
public class ProfileRepositoryImpl extends AbstractJPARepository<Profile, Long> implements ProfileRepository {

    @Override
    protected Class<Profile> getEntityClass() {
        return Profile.class;
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT p FROM Profile p WHERE p.uid = :uid")
    public Optional<Profile> findByUid(String uid) {
        try {
            Profile profile = (Profile) this.entityManager
                    .createNamedQuery("Profile.findByUid")
                    .setParameter("uid", uid)
                    .getSingleResult();
            return Optional.of(profile);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT p FROM Profile p WHERE p.email=:email")
    public Optional<Profile> findByEmail(String email) {
        try {
            Profile profile = (Profile) this.entityManager
                    .createNamedQuery("Profile.findByEmail")
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(profile);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override

    @JPAQuery(parameterizedQuery =
            "SELECT p.uid FROM Profile p WHERE p.uid IN :uids")
    public List<String> checkExistingUids(List<String> uids) {
        return entityManager
                .createNamedQuery("Profile.checkAvailableUids", String.class)
                .setParameter("uids", uids)
                .getResultList();
    }

    @Override
    public void updateRating() {
        StoredProcedureQuery update_ratingQuery
                = this.entityManager.createStoredProcedureQuery("update_rating");
        update_ratingQuery.execute();
    }
}
