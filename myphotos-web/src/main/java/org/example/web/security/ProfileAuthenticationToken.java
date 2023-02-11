package org.example.web.security;

import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.example.model.model.domain.Profile;

public class ProfileAuthenticationToken implements RememberMeAuthenticationToken {

    private final Profile profile;

    public ProfileAuthenticationToken(Profile profile) {
        this.profile = profile;
    }

    @Override
    public Object getPrincipal() {
        return profile;
    }

    @Override
    public Object getCredentials() {
        return SecurityUtils.TEMP_PASS;
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }
}
