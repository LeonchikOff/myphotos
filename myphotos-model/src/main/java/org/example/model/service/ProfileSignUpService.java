package org.example.model.service;

import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.domain.Profile;

public interface ProfileSignUpService {

    void createSignUpProfile(Profile profile);

    Profile getCurrentProfile()
            throws ObjectNotFoundException;

    void completeSignUpProfile();

    void cancel();
}
