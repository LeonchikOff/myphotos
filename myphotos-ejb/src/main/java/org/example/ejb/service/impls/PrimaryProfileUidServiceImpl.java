package org.example.ejb.service.impls;

import org.example.ejb.model.ProfileUidGenerator;
import org.example.ejb.service.ProfileUidService;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@ProfileUidGenerator(category = ProfileUidGenerator.Category.PRIMARY)
public class PrimaryProfileUidServiceImpl implements ProfileUidService {
    @Override
    public List<String> generateProfileUidCandidates(String englishFirstName, String englishLastName) {
        return Collections.unmodifiableList(Arrays.asList(
                String.format("%s-%s", englishFirstName, englishLastName).toLowerCase(),
                String.format("%s.%s", englishFirstName, englishLastName).toLowerCase(),
                String.format("%s%s", englishFirstName, englishLastName).toLowerCase()
        ));
    }
}
