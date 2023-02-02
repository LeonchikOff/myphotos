package org.example.ejb.jpa.listeners;

//JPA Listener

import org.example.model.exception.InvalidWorkFlowException;
import org.example.model.model.domain.AccessToken;

import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccessTokenLifeCycleManager {

    @Inject
    private Logger logger;

    @PrePersist
    public void setToken(AccessToken accessTokenModel) {
        accessTokenModel.setToken(UUID.randomUUID().toString().replace("-", ""));
        logger.log(Level.FINE, "Generate new uid token {0} for entity {1}",
                new Object[]{accessTokenModel.getToken(), accessTokenModel.getClass()});
    }

    @PreUpdate
    public void rejectUpdate(AccessToken accessTokenModel) {
        throw new InvalidWorkFlowException("AccessToken is not updatable");
    }
}
