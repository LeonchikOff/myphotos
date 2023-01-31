package org.example.model.service;

import org.example.model.exception.FailedRetrievalSocialDataException;
import org.example.model.model.domain.Profile;

public interface SocialService {

    Profile fetchProfile(String code)
            throws FailedRetrievalSocialDataException;

    String getClientId();

    default String getAuthorizeUrl() {
        throw new UnsupportedOperationException();
    }
}
