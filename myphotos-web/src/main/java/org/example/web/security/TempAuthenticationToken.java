package org.example.web.security;

import org.apache.shiro.authc.AuthenticationToken;

public class TempAuthenticationToken implements AuthenticationToken {
    @Override
    public Object getPrincipal() {
        return SecurityUtils.TEMP_PROFILE;
    }

    @Override
    public Object getCredentials() {
        return SecurityUtils.TEMP_PASS;
    }
}
