package org.example.ejb.service.impl.social;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.common.cdi.annotation.Property;
import org.example.common.cdi.annotation.qualifier.GooglePlus;
import org.example.model.exception.FailedRetrievalSocialDataException;
import org.example.model.model.domain.Profile;
import org.example.model.service.SocialService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@GooglePlus
@ApplicationScoped
public class GoogleSocialServiceImpl implements SocialService {


    @Inject
    @Property(nameOfProperty = "myphotos.social.google-plus.client-id")
    private String clientId;

    private List<String> issuers;

    @Inject
    public void setIssuers(@Property(nameOfProperty = "myphotos.social.google-plus.issuers") String issuers) {
        this.issuers = Collections.unmodifiableList(Arrays.asList(issuers.split(",")));
    }


    @Override
    public Profile fetchProfile(String code) throws FailedRetrievalSocialDataException {
        DecodedJWT decodedJWT = JWT.decode(code);
        decodedJWT.getClaims().forEach((key, value) -> System.out.println(key + ": " + value));
        Profile profile = new Profile();
        profile.setEmail(decodedJWT.getClaim("email").asString());
        profile.setFirstName(decodedJWT.getClaim("given_name").asString());
        profile.setLastName(decodedJWT.getClaim("family_name").asString());
        profile.setAvatarUrl(decodedJWT.getClaim("picture").asString());
        return profile;
    }


    @Override
    public String getClientId() {
        System.out.println("!!!!!!!!!!!!!!!!" + this.clientId);
        return this.clientId;
    }

    @Override
    public String getAuthorizeUrl() {
        return null;
    }
}
