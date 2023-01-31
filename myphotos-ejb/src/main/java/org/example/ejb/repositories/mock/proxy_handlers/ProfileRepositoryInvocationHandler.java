package org.example.ejb.repositories.mock.proxy_handlers;

import org.example.ejb.repositories.mock.InMemoryDataBase;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

@ApplicationScoped
public class ProfileRepositoryInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("findByUid")) {
            String uidParamOfMethod = String.valueOf(args[0]);
            if (uidParamOfMethod.equals("richard-hendricks")) {
                return Optional.of(InMemoryDataBase.PROFILE);
            } else {
                return Optional.empty();
            }
        }
        throw new UnsupportedOperationException(String.format("Method %s not implemented yet", method));
    }
}
