package org.example.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.example.model.model.domain.Profile;
import org.example.model.model.validation.EnglishLanguage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("SignUpProfile")
public class SignUpProfileRest extends AuthenticationCodeRest {

    private String firstName;

    private String lastName;

    private String jobTitle;

    private String location;

    public SignUpProfileRest() {
    }

    @ApiModelProperty(required = true, value = "Min size = 1, Only latin characters allowed")
    @NotNull(message = "{Profile.firstName.NotNull}")
    @Size(min = 1, message = "{Profile.firstName.Size}")
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, withPunctuation = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ApiModelProperty(required = true, value = "Min size = 1, Only latin characters allowed")
    @NotNull(message = "{Profile.lastName.NotNull}")
    @Size(min = 1, message = "{Profile.lastName.Size}")
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, withPunctuation = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ApiModelProperty(required = true, value = "Min size = 5, Only latin characters allowed")
    @NotNull(message = "{Profile.jobTitle.NotNull}")
    @Size(min = 5, message = "{Profile.jobTitle.Size}")
    @EnglishLanguage(withSpecialSymbols = false)
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @ApiModelProperty(required = true, value = "Min size = 5, Only latin characters allowed")
    @NotNull(message = "{Profile.location.NotNull}")
    @Size(min = 5, message = "{Profile.location.Size}")
    @EnglishLanguage(withSpecialSymbols = false)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void copyToProfile(Profile profile) {
        profile.setFirstName(this.getFirstName());
        profile.setLastName(this.getLastName());
        profile.setJobTitle(this.getJobTitle());
        profile.setLocation(this.getLocation());
    }
}
