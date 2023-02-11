package org.example.web.component;

import org.example.model.exception.InvalidWorkFlowException;
import org.example.model.exception.ObjectNotFoundException;
import org.example.model.model.domain.Profile;
import org.example.model.service.ProfileSignUpService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@SessionScoped
public class ProfileSignUpServiceProxy implements ProfileSignUpService, Serializable {

    @SuppressWarnings("CdiUnproxyableBeanTypesInspection")
    @Inject
    private transient Logger logger;

    @EJB
    private ProfileSignUpService profileSignUpService;


    @PostConstruct
    private void postConstruct() {
        logger.log(Level.FINE, "Created {0} instance: {1}",
                new Object[]{this.getClass().getSimpleName(), System.identityHashCode(this)});
    }

    @PreDestroy
    private void preDestroy() {
        logger.log(Level.FINE, "Destroyed {0} instance: {1}",
                new Object[]{this.getClass().getSimpleName(), System.identityHashCode(this)});
//        Remove stateful bean if current session destroyed
        if (profileSignUpService != null) {
//            remove
            profileSignUpService.cancel();
            profileSignUpService = null;
        }
    }


    private void validate() {
        if (profileSignUpService == null) {
            throw new InvalidWorkFlowException(
                    "Can't use ProfileSignUpService after invocation of method completeSignUpProfile() or cancel()");
        }
    }

    @Override
    public void createSignUpProfile(Profile profile) {
        validate();
        profileSignUpService.createSignUpProfile(profile);
    }

    @Override
    public Profile getCurrentProfile() throws ObjectNotFoundException {
        validate();
        return profileSignUpService.getCurrentProfile();
    }

    @Override
    public void completeSignUpProfile() {
        validate();
        System.out.println("!!!!!!!!!!!!!!BEFORE!!!!!completeSignUpProfile!!!!!!!!!!!!!!!!!!!");
        profileSignUpService.completeSignUpProfile();
        System.out.println("!!!!!!!!!!!!!!AFTER!!!!!completeSignUpProfile!!!!!!!!!!!!!!!!!!!");
        profileSignUpService = null;
    }

    @Override
    public void cancel() {
        profileSignUpService.cancel();
        profileSignUpService = null;
    }
}
