package org.example.service;

import org.example.exception.ObjectNotFoundException;
import org.example.model.AsyncOperation;
import org.example.model.ImageResource;
import org.example.model.domain.Profile;

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
