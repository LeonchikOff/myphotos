package org.example.ws.model;

import org.example.common.cdi.annotation.converter.ConvertAsUrl;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "profile")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProfileSOAP {

    @XmlAttribute(required = true)
    private Long id;

    private String uid;

    private String firstName;

    private String lastName;

    private String jobTitle;

    private String location;

    private int photoCount;

    @ConvertAsUrl
    private String avatarUrl;

    @XmlElementWrapper(name = "profilePhotos")
    @XmlElement(name = "photo")
    private List<ProfilePhotoSOAP> profilePhotos;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<ProfilePhotoSOAP> getProfilePhotos() {
        return profilePhotos;
    }

    public void setProfilePhotos(List<ProfilePhotoSOAP> profilePhotos) {
        this.profilePhotos = profilePhotos;
    }
}
