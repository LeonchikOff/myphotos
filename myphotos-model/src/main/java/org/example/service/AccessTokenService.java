package org.example.service;

import org.example.exception.AccessForbiddenException;
import org.example.exception.InvalidAccessTokenException;
import org.example.model.domain.AccessToken;
import org.example.model.domain.Profile;

public interface AccessTokenService {

    AccessToken generateAccessToken(Profile profile);
    Profile findProfileWhereAccessTokenBelongsToProfile(String tokenValue, Long profileId)
            throws InvalidAccessTokenException, AccessForbiddenException;
    void invalidateAccessToken(String tokenValue)
            throws InvalidAccessTokenException;
}
