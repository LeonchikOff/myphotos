package org.example.myphotos.generator.component;


import org.example.model.exception.ApplicationException;
import org.example.model.model.domain.Profile;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

//JAXB - Java Architecture for XML Binding
//Платформа для работы с XML
//Позволяет Java разработчикам ставить в соответствие Java классы и XML представления.
// JAXB предоставляет две основные возможности:
// 1.маршаллирование Java объектов в XML
// 2.демаршаллирование обратно из XML в Java объект.


@ApplicationScoped
public class ProfileGenerator {

    private final Random random = new Random();

    public List<Profile> generateProfiles() {
        File file = new File("external/test-data/profiles.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Profiles.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Profiles profiles = (Profiles) jaxbUnmarshaller.unmarshal(file);
            final Date dateOfCreated = new Date();
            return Collections.unmodifiableList(
                    profiles
                            .getProfile()
                            .stream()
                            .peek(profile -> {
                                profile.setUid(String.format("%s-%s", profile.getFirstName().toLowerCase(), profile.getLastName().toLowerCase()));
                                profile.setEmail(profile.getUid() + "@myphotos.com");
                                profile.setPhotoCount(random.nextInt(15) + random.nextInt(5) + 3);
                                profile.setDateOfCreated(dateOfCreated);
                            }).collect(Collectors.toList())
            );
        } catch (JAXBException e) {
            throw new ApplicationException("Can't load test data from xml-file: " + file.getAbsolutePath(), e);
        }
    }

    @XmlRootElement(name = "profiles")
    private static class Profiles {
        private List<Profile> profile;

        public List<Profile> getProfile() {
            return profile;
        }

        public void setProfile(List<Profile> profile) {
            this.profile = profile;
        }
    }


}

