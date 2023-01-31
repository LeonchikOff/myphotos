package org.example.ejb.repositories.mock.proxy_handlers;

import org.example.ejb.repositories.mock.InMemoryDataBase;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.stream.Collectors;

@ApplicationScoped
public class PhotoRepositoryInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "findProfilePhotosInOrderLatestFirst":
                return findProfilePhotosInOrderLatestFirst(args);
            case "findAllPhotosOrderByViewsDesc":
            case "findAllPhotosOrderByAuthorRating":
                return findAll(args);
            case "countAllPhotosToDB":
                return countAllPhotosToDB(args);
            default:
                throw new UnsupportedOperationException(String.format("Method %s not implemented yet", method));
        }
    }

    private Object findProfilePhotosInOrderLatestFirst(Object[] args) {
        Long profileId = (Long) args[0];
        Integer offset = (Integer) args[1];
        Integer limit = (Integer) args[2];
        if (profileId.equals(InMemoryDataBase.PROFILE.getId())) {
            return InMemoryDataBase.PHOTOS.stream().skip(offset).limit(limit).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private Object findAll(Object[] args) {
        Integer offset = (Integer) args[0];
        Integer limit = (Integer) args[1];
        return InMemoryDataBase.PHOTOS.stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    private Object countAllPhotosToDB(Object[] args) {
        return (long) InMemoryDataBase.PHOTOS.size();
    }

}
