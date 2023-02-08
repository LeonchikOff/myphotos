package org.example.ejb.service.manegers;

import org.example.ejb.model.ProfileUidGenerator;
import org.example.ejb.service.ProfileUidService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class ProfileUidServiceManager {

    @Inject
    private Logger logger;

    //    Iteration field Instance<?>, injected(@Inject) from the CDI container ,
//    which includes all(@Any) implementations of the ProfileUidService interface
    @Inject
    @Any
    private Instance<ProfileUidService> profileUidServices;

    public List<String> getProfileUidCandidates(String englishFirstName, String englishLastName) {
        List<String> profileUidCandidates = new ArrayList<>();
        addUidCandidates(new PrimaryProfileUidGenerator(), profileUidCandidates, englishFirstName, englishLastName);
        addUidCandidates(new SecondaryProfileUidGenerator(), profileUidCandidates, englishFirstName, englishLastName);
        return Collections.unmodifiableList(profileUidCandidates);
    }

    private void addUidCandidates(AnnotationLiteral<ProfileUidGenerator> annotationLiteralSelector, List<String> profileUidCandidates,
                                  String englishFirstName, String englishLastName) {
        Instance<ProfileUidService> profileUidServices = this.profileUidServices.select(annotationLiteralSelector);
        profileUidServices.forEach(profileUidService ->
                profileUidCandidates.addAll(profileUidService.generateProfileUidCandidates(englishFirstName, englishLastName)));
    }


    private static class PrimaryProfileUidGenerator
            extends AnnotationLiteral<ProfileUidGenerator>
            implements ProfileUidGenerator {
        @Override
        public ProfileUidGenerator.Category category() {
            return Category.PRIMARY;
        }
    }

    private static class SecondaryProfileUidGenerator
            extends AnnotationLiteral<ProfileUidGenerator>
            implements ProfileUidGenerator {
        @Override
        public ProfileUidGenerator.Category category() {
            return Category.SECONDARY;
        }
    }

    public String getDefaultUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @PostConstruct
    private void postConstruct() {
        StringBuilder logStringBuilder = new StringBuilder();
        profileUidServices.forEach(profileUidService ->
                logStringBuilder.append(String.format("%s\n", profileUidService.getClass().getName())));
        logger.info(logStringBuilder.toString());
    }


}
