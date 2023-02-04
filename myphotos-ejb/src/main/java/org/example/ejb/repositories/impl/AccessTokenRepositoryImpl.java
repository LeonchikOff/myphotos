package org.example.ejb.repositories.impl;

import org.example.ejb.repositories.AccessTokenRepository;
import org.example.ejb.repositories.impl.StaticJPAQueryInitializer.JPAQuery;
import org.example.model.model.domain.AccessToken;

import javax.enterprise.context.Dependent;
import javax.persistence.NoResultException;
import java.util.Optional;

@SuppressWarnings("JpaQueryApiInspection")
@Dependent
public class AccessTokenRepositoryImpl extends AbstractJPARepository<AccessToken, String> implements AccessTokenRepository {

    @Override
    protected Class<AccessToken> getEntityClass() {
        return AccessToken.class;
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "SELECT at FROM AccessToken at JOIN FETCH at.profile WHERE at.token=:token")
    public Optional<AccessToken> findByToken(String token) {
        try {
            return Optional.of(entityManager
                    .createNamedQuery("AccessToken.findByToken", AccessToken.class)
                    .setParameter("token", token)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    @Override
    @JPAQuery(parameterizedQuery =
            "DELETE FROM AccessToken at WHERE at.token=:token")
    public boolean removeAccessToken(String token) {
        return entityManager
                .createNamedQuery("AccessToken.removeAccessToken")
                .setParameter("token", token)
                .executeUpdate() == 1;
    }
}
