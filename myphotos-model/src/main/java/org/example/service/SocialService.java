package org.example.service;

import org.example.exception.FailedRetrievalSocialDataException;
import org.example.model.domain.Profile;

public interface SocialService {

    Profile fetchProfile(String code)
            throws FailedRetrievalSocialDataException;

    String getClientId();

    default String getAuthorizeUrl() {
        throw new UnsupportedOperationException();
    }
}
