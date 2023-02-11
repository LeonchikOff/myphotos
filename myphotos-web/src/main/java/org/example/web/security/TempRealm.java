package org.example.web.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.Collections;

public class TempRealm extends AuthorizingRealm {

    private static final Account ACCOUNT = new SimpleAccount(
            new SimplePrincipalCollection(SecurityUtils.TEMP_PROFILE, TempRealm.class.getSimpleName()),
            SecurityUtils.TEMP_PASS,
            Collections.singleton(SecurityUtils.TEMP_ROLE)
    );

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof TempAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if(SecurityUtils.TEMP_PROFILE.equals(principalCollection.getPrimaryPrincipal())) {
            return ACCOUNT;
        } else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return ACCOUNT;
    }
}
