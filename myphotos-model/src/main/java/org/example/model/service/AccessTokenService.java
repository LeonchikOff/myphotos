package org.example.model.service;

import org.example.model.exception.AccessForbiddenException;
import org.example.model.exception.InvalidAccessTokenException;
import org.example.model.model.domain.AccessToken;
import org.example.model.model.domain.Profile;

public interface AccessTokenService {

    AccessToken generateAccessToken(Profile profile);
    Profile findProfileWhereAccessTokenBelongsToProfile(String tokenValue, Long profileId)
            throws InvalidAccessTokenException, AccessForbiddenException;
    void invalidateAccessToken(String tokenValue)
            throws InvalidAccessTokenException;
}
