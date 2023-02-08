package org.example.ejb.service.beans;

import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.domain.Profile;
import org.example.model.service.ProfileService;
import org.example.model.service.ProfileSignUpService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateful
@StatefulTimeout(value = 30, unit = TimeUnit.MINUTES)
public class ProfileSignUpServiceBean implements ProfileSignUpService, Serializable {

    @Inject
    private Logger logger;

    @Inject
    private ProfileService profileService;

    private Profile profile;

    @Override
    public void createSignUpProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public Profile getCurrentProfile() throws ObjectNotFoundException {
        if(this.profile == null) {
            throw new ObjectNotFoundException("Profile not found. Please create profile before use");
        }
        return this.profile;
    }

    @Override
    @Remove
    public void completeSignUpProfile() {
        profileService.signUpWithDeliveryToDB(this.profile, false);
    }

    @Override
    @Remove
    public void cancel() {
        this.profile = null;
    }

//после создания
    @PostConstruct
    private void postConstruct() {
        logger.log(Level.FINE, "Created {0} instance: {1}",
                new Object[]{this.getClass().getSimpleName(), System.identityHashCode(this)});
    }
//перед использованием
    @PostActivate
    private void postActivate() {
        logger.log(Level.FINE, "Activated {0} instance: {1}",
                new Object[]{this.getClass().getSimpleName(), System.identityHashCode(this)});
    }
//после использования
    @PrePassivate
    private void prePassivate() {
        logger.log(Level.FINE, "Passivated {0} instance: {1}",
                new Object[]{this.getClass().getSimpleName(), System.identityHashCode(this)});
    }
//перед удалением
    @PreDestroy
    private void preDestroy() {
        logger.log(Level.FINE, "Destroyed {0} instance: {1}",
                new Object[]{this.getClass().getSimpleName(), System.identityHashCode(this)});
    }

}
