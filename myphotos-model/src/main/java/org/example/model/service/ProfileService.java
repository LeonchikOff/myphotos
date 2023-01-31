package org.example.model.service;

import org.example.model.model.domain.Profile;
import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.AsyncOperation;
import org.example.model.model.ImageResource;

import java.util.Optional;

public interface ProfileService {

    Profile findById(Long id)
            throws ObjectNotFoundException;
    Profile findByUid(String uid)
            throws ObjectNotFoundException;
    Optional<Profile> findByEmail(String email);

    void signUpWithDeliveryToDB(Profile profile, boolean uploadProfileAvatar);
    void uploadNewAvatar(Profile currentProfile, ImageResource imageResource, AsyncOperation<Profile> profileAsyncOperation);
    void translateSocialProfile(Profile profile);
    void update(Profile profile);

}
