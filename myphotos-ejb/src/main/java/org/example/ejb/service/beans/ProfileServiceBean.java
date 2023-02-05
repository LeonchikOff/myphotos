package org.example.ejb.service.beans;

import org.example.common.cdi.annotation.Property;
import org.example.common.config.ImageCategory;
import org.example.ejb.repositories.ProfileRepository;
import org.example.ejb.service.ImageStorageService;
import org.example.ejb.service.interceptors.AsyncOperationInterceptor;
import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.AsyncOperation;
import org.example.model.model.ImageResource;
import org.example.model.model.domain.Profile;
import org.example.model.service.ProfileService;

import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.Optional;

@Stateless
@LocalBean
@Local(ProfileService.class)
public class ProfileServiceBean implements ProfileService {

    @EJB
    private ImageProcessorBean imageProcessorBean;

    @Inject
    private ImageStorageService imageStorageService;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    @Property(nameOfProperty = "myphotos.profile.avatar.placeholder.url")
    private String avatarPlaceHolderUrl;


    @Override
    public Profile findById(Long id) throws ObjectNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (!optionalProfile.isPresent())
            throw new ObjectNotFoundException(String.format("Profile not found by id: %s", id));
        return optionalProfile.get();
    }

    @Override
    public Profile findByUid(String uid) throws ObjectNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findByUid(uid);
        if (!optionalProfile.isPresent())
            throw new ObjectNotFoundException(String.format("Profile not found by uid: %s", uid));
        return optionalProfile.get();
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    @Override
    public void signUpWithDeliveryToDB(Profile profile, boolean uploadProfileAvatar) {
        profileRepository.create(profile);
    }

    @Override
    @Asynchronous
    @Interceptors(value = {AsyncOperationInterceptor.class})
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void uploadNewAvatar(Profile currentProfile, ImageResource imageResource, AsyncOperation<Profile> profileAsyncOperation) {
        try {
            this.uploadNewAvatar(currentProfile, imageResource);
            profileAsyncOperation.onSuccessOperation(currentProfile);
        } catch (Throwable throwable) {
            this.setAvatarPlaceHolder(currentProfile);
            profileAsyncOperation.onFailedOperation(throwable);
        }
    }

    public void uploadNewAvatar(Profile currentProfile, ImageResource imageResource) {
        String avatarUrl = imageProcessorBean.processProfileAvatarAndGetUrl(imageResource);
        if (ImageCategory.isImageCategoryUrl(currentProfile.getAvatarUrl())) {
            imageStorageService.deletePublicImage(currentProfile.getAvatarUrl());
        }
        currentProfile.setAvatarUrl(avatarUrl);
        profileRepository.update(currentProfile);
    }

    public void setAvatarPlaceHolder(Long profileId) {
        this.setAvatarPlaceHolder(this.findById(profileId));
    }

    public void setAvatarPlaceHolder(Profile currentProfile) {
        if (currentProfile.getAvatarUrl() == null) {
            currentProfile.setAvatarUrl(avatarPlaceHolderUrl);
            profileRepository.update(currentProfile);
        }
    }

    @Override
    public void translateSocialProfile(Profile profile) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void update(Profile profile) {
        profileRepository.update(profile);
    }
}
