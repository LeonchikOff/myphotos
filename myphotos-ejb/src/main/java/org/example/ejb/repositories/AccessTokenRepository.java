package org.example.ejb.repositories;

import org.example.model.model.domain.AccessToken;

import java.util.Optional;

public interface AccessTokenRepository extends EntityRepository<AccessToken, String> {

    Optional<AccessToken> findByToken(String token);

    boolean removeAccessToken(String token);
}
