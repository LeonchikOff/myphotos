package org.example.ejb.service.impls;

import org.example.ejb.model.ProfileUidGenerator;
import org.example.ejb.service.ProfileUidService;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@ProfileUidGenerator(category = ProfileUidGenerator.Category.PRIMARY)
public class TestAdditionalPrimaryProfileUidServiceImpl implements ProfileUidService {
    @Override
    public List<String> generateProfileUidCandidates(String englishFirstName, String englishLastName) {
        return Collections.emptyList();
    }
}
