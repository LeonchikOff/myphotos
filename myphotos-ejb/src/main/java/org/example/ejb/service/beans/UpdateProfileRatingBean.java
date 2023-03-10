package org.example.ejb.service.beans;

import org.example.ejb.repositories.ProfileRepository;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Logger;

@Stateless
public class UpdateProfileRatingBean {

    @Inject
    private Logger logger;

    @Inject
    private ProfileRepository profileRepository;

    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    public void updateProfileRating() {
        profileRepository.updateRating();
        logger.info("Successful updated profile rating");
    }
}
