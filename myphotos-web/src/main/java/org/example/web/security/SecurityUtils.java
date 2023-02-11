package org.example.web.security;

import org.apache.shiro.subject.Subject;
import org.example.model.model.domain.Profile;

public class SecurityUtils {
    static final String TEMP_PROFILE = "";
    static final String TEMP_PASS = "";

    static final String TEMP_ROLE = "TEMP";
    static final String PROFILE_ROLE = "PROFILE";

    public static void authenticate(Profile profile) {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        currentSubject.login(new ProfileAuthenticationToken(profile));
    }

    public static void authenticate() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        currentSubject.login(new TempAuthenticationToken());
    }

    public static void logout() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        currentSubject.logout();
    }

    public static boolean isAuthenticated() {
        return org.apache.shiro.SecurityUtils.getSubject().isAuthenticated();
    }
    public static boolean isTempAuthenticated() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        return currentSubject.isAuthenticated() && TEMP_PROFILE.equals(currentSubject.getPrincipal());
    }

    public static Profile getCurrentProfile() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        if(currentSubject.isAuthenticated()) {
            return ((Profile) currentSubject.getPrincipal());
        } else {
            throw new IllegalStateException("Current subject is not authenticated");
        }
    }
}
