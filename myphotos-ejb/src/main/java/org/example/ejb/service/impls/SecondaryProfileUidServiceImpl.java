package org.example.ejb.service.impls;

import org.example.ejb.model.ProfileUidGenerator;
import org.example.ejb.service.ProfileUidService;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@ProfileUidGenerator(category = ProfileUidGenerator.Category.SECONDARY)
public class SecondaryProfileUidServiceImpl implements ProfileUidService {

    @Override
    public List<String> generateProfileUidCandidates(String englishFirstName, String englishLastName) {
        return Collections.unmodifiableList(Arrays.asList(
                String.format("%s-%s", englishFirstName.charAt(0), englishLastName).toLowerCase(),
                String.format("%s.%s", englishFirstName.charAt(0), englishLastName).toLowerCase(),
                String.format("%s%s", englishFirstName.charAt(0), englishLastName).toLowerCase(),
                englishFirstName.toLowerCase(),
                englishLastName.toLowerCase()
        ));
    }
}
