package org.example.service;

import org.example.exception.ObjectNotFoundException;
import org.example.model.domain.Profile;

public interface ProfileSignUpService {

    void createSignUpProfile(Profile profile);

    Profile getCurrentProfile()
            throws ObjectNotFoundException;

    void completeSignUpProfile();

    void cancel();
}
