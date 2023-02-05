package org.example.myphotos.generator.alternative;

import org.example.ejb.repositories.impl.StaticJPAQueryInitializer;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.Bean;

@Dependent
@TestDataGeneratorEnvironment
public class JPARepositoryFinderAlternative extends StaticJPAQueryInitializer.JPARepositoryFinder {

    @Override
    protected boolean isCandidateValid(Bean<?> bean) {
        return false;
    }
}
