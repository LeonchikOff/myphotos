package org.example.ejb.service.impl.social;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;

import org.example.common.cdi.annotation.Property;
import org.example.common.cdi.annotation.qualifier.Facebook;
import org.example.model.exception.FailedRetrievalSocialDataException;
import org.example.model.model.domain.Profile;
import org.example.model.service.SocialService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@Facebook //@Qualifier
@ApplicationScoped
public class FacebookSocialServiceImpl implements SocialService {

    @Inject
    @Property(nameOfProperty = "myphotos.social.facebook.client-id")
    private String clientId;

    @Inject
    @Property(nameOfProperty = "myphotos.social.facebook.client-secret")
    private String secret;

    private String redirectUrl;


    @Inject
    public void setHost(@Property(nameOfProperty = "myphotos.host") String host) {
        this.redirectUrl = host + "/from/facebook";
    }


    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public Profile fetchProfile(String code) throws FailedRetrievalSocialDataException {
        try {
            User fetchedFacebookUser = this.fetchUser(code);
            return createProfile(fetchedFacebookUser);
        } catch (RuntimeException runtimeException) {
            throw new FailedRetrievalSocialDataException("Can't fetch user from facebook: " + runtimeException.getMessage(), runtimeException);
        }
    }

    private User fetchUser(String code) {
        FacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);
        FacebookClient.AccessToken accessToken = facebookClient.obtainUserAccessToken(clientId, secret, redirectUrl, code);
        facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
        return facebookClient
                .fetchObject(
                        "me",
                        User.class,
                        Parameter.with("fields", "id, email, first_name, last_name"));
    }

    private Profile createProfile(User user) {
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setAvatarUrl(String.format("https://graph.facebook.com/v16.0/%s/picture?type=large", user.getId()));
        return profile;
    }


    @Override
    public String getAuthorizeUrl() {
        ScopeBuilder scopeBuilder = new ScopeBuilder();
        scopeBuilder.addPermission(FacebookPermissions.EMAIL);
        DefaultFacebookClient defaultFacebookClient = new DefaultFacebookClient(Version.LATEST);
        return defaultFacebookClient.getLoginDialogUrl(clientId, redirectUrl, scopeBuilder);
    }
}
