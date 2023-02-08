package org.example.ejb.service.beans;

import org.example.common.cdi.annotation.Property;
import org.example.common.config.ImageCategory;
import org.example.ejb.repositories.ProfileRepository;
import org.example.ejb.service.ImageStorageService;
import org.example.ejb.service.TranslitConverter;
import org.example.ejb.service.interceptors.AsyncOperationInterceptor;
import org.example.ejb.service.manegers.ProfileUidServiceManager;
import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.AsyncOperation;
import org.example.model.model.ImageResource;
import org.example.model.model.domain.Profile;
import org.example.model.service.ProfileService;

import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
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
    private TranslitConverter translitConverter;

    @Inject
    private ProfileUidServiceManager profileUidServiceManager;

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
        if (profile.getUid() == null)
            this.setProfileUid(profile);
        profileRepository.create(profile);
    }

    private void setProfileUid(Profile profile) {
        List<String> profileUidCandidates = profileUidServiceManager.getProfileUidCandidates(profile.getFirstName(), profile.getLastName());
        List<String> existingUids = profileRepository.checkExistingUids(profileUidCandidates);
        for (String uidCandidate : profileUidCandidates) {
            if (!existingUids.contains(uidCandidate)) {
                profile.setUid(uidCandidate);
                return;
            }
        }
        profile.setUid(profileUidServiceManager.getDefaultUid());
    }

    @Override
    public void translateSocialProfile(Profile profile) {
        profile.setFirstName(profile.getFullName() != null ? translitConverter.translit(profile.getFirstName()) : null);
        profile.setLastName(profile.getLastName() != null ? translitConverter.translit(profile.getLastName()) : null);
        profile.setJobTitle(profile.getJobTitle() != null ? translitConverter.translit(profile.getJobTitle()) : null);
        profile.setLocation(profile.getLocation() != null ? translitConverter.translit(profile.getLocation()) : null);
    }

    @Override
    public void update(Profile profile) {
        profileRepository.update(profile);
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
}
