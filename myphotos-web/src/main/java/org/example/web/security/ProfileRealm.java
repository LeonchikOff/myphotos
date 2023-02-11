package org.example.web.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.model.model.domain.Profile;

import java.util.Collections;

public class ProfileRealm extends AuthorizingRealm {


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ProfileAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection.getPrimaryPrincipal() instanceof Profile) {
            return new SimpleAuthorizationInfo(Collections.singleton(SecurityUtils.PROFILE_ROLE));
        } else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        ProfileAuthenticationToken profileAuthenticationToken = (ProfileAuthenticationToken) authenticationToken;
        return new SimpleAuthenticationInfo(profileAuthenticationToken.getPrincipal(),
                profileAuthenticationToken.getCredentials(), ProfileRealm.class.getSimpleName());
    }

    @Override
    protected Object getAvailablePrincipal(PrincipalCollection principals) {
        Object availablePrincipal = super.getAvailablePrincipal(principals);
        if (availablePrincipal instanceof Profile) {
            return ((Profile) availablePrincipal).getEmail();
        }
        return availablePrincipal;
    }
}
