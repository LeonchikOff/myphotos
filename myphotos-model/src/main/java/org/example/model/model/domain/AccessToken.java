package org.example.model.model.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "access_token", schema = "public", catalog = "myphotos")
public class AccessToken extends AbstractDomain {

    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @NotNull
    @JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Profile profile;


    public AccessToken() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
