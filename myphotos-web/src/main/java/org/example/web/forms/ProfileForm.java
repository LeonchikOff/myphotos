package org.example.web.forms;

import org.example.common.cdi.annotation.groups.ProfileUpdateGroup;
import org.example.common.cdi.annotation.groups.SignUpGroup;
import org.example.model.model.domain.Profile;
import org.example.model.model.validation.EnglishLanguage;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProfileForm {

    private String uid;
    private String avatarUrl;

    @AssertTrue(message = "{ProfileForm.agreement.AssertTrue}", groups = {SignUpGroup.class})
    private boolean agreement;

    @NotNull(message = "{Profile.firstName.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 1, max = 60, message = "{Profile.firstName.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String firstName;

    @NotNull(message = "{Profile.lastName.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 1, max = 60, message = "{Profile.lastName.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String lastName;

    @NotNull(message = "{Profile.jobTitle.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 5, max = 100, message = "{Profile.jobTitle.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withSpecialSymbols = false , groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String jobTitle;

    @NotNull(message = "{Profile.location.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 5, max = 100, message = "{Profile.location.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String location;

    public ProfileForm() {
    }

    public ProfileForm(Profile profile) {
        setUid(profile.getUid());
        setFirstName(profile.getFirstName());
        setLastName(profile.getLastName());
        setJobTitle(profile.getJobTitle());
        setLocation(profile.getLocation());
        setAvatarUrl(profile.getAvatarUrl());
    }


    public void copyToProfile(Profile currentProfile) {
        currentProfile.setFirstName(getFirstName());
        currentProfile.setLastName(getLastName());
        currentProfile.setJobTitle(getJobTitle());
        currentProfile.setLocation(getLocation());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFullName() {
        return String.format("%s %s", getFirstName(), getLastName());
    }
}
