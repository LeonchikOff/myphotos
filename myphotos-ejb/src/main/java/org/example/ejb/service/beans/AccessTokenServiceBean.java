package org.example.ejb.service.beans;

import org.example.ejb.repositories.AccessTokenRepository;
import org.example.model.exception.AccessForbiddenException;
import org.example.model.exception.InvalidAccessTokenException;
import org.example.model.model.domain.AccessToken;
import org.example.model.model.domain.Profile;
import org.example.model.service.AccessTokenService;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@LocalBean
@Local(AccessTokenService.class)
public class AccessTokenServiceBean implements AccessTokenService {

    @Inject
    private Logger logger;

    @Inject
    private AccessTokenRepository accessTokenRepository;

    @Override
    public AccessToken generateAccessToken(Profile profile) {
        AccessToken accessToken = new AccessToken();
        accessToken.setProfile(profile);
        accessTokenRepository.create(accessToken);
        return accessToken;
    }

    @Override
    public Profile findProfileWhereAccessTokenBelongsToProfile(String tokenValue, Long profileId)
            throws InvalidAccessTokenException, AccessForbiddenException {
        Optional<AccessToken> tokenOptional = accessTokenRepository.findByToken(tokenValue);
        if (!tokenOptional.isPresent())
            throw new InvalidAccessTokenException(String.format("Access token %s invalid", tokenValue));
        Profile profile = tokenOptional.get().getProfile();
        if (!profile.getId().equals(profileId))
            throw new AccessForbiddenException(String.format(
                    "Access to the profile with id=%s by token=%s is forbidden because it does not belong to him", profileId, tokenValue));
        return profile;
    }

    @Override
    public void invalidateAccessToken(String tokenValue) throws InvalidAccessTokenException {
        boolean isRemoved = accessTokenRepository.removeAccessToken(tokenValue);
        if (!isRemoved) {
            logger.log(Level.WARNING, "Access token {0} not found", tokenValue);
            throw new InvalidAccessTokenException("Access token not found");
        }
    }
}
