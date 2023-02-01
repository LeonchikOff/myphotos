package org.example.ejb.repositories.mock;

import org.example.ejb.repositories.AccessTokenRepository;
import org.example.ejb.repositories.PhotoRepository;
import org.example.ejb.repositories.ProfileRepository;
import org.example.ejb.repositories.mock.proxy_handlers.PhotoRepositoryInvocationHandler;
import org.example.ejb.repositories.mock.proxy_handlers.ProfileRepositoryInvocationHandler;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.lang.reflect.Proxy;

@Dependent
public class InMemoryRepositoryFactory {

    @Inject
    private ProfileRepositoryInvocationHandler profileRepositoryInvocationHandler;

    @Inject
    private PhotoRepositoryInvocationHandler photoRepositoryInvocationHandler;

    @Produces
    public ProfileRepository getProfileRepository() {
        return (ProfileRepository) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{ProfileRepository.class},
                profileRepositoryInvocationHandler);
    }

    @Produces
    public PhotoRepository getPhotoRepository() {
        return (PhotoRepository) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{PhotoRepository.class},
                photoRepositoryInvocationHandler);
    }


    @Produces
    public AccessTokenRepository getAccessTokenRepository() {
        return (AccessTokenRepository) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{AccessTokenRepository.class},
                (proxy, method, args) -> {
                    throw new UnsupportedOperationException("Not implemented yet");
                }
        );
    }
}
