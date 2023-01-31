package org.example.common.cdi.resource;


import org.example.model.exception.ConfigException;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class ClassPathResourceLoader implements ResourceLoader {
    @Override
    public boolean isSupport(String resourceName) {
        return resourceName.startsWith("classpath:");
    }

    @Override
    public InputStream getInputStream(String resourceName) throws IOException {
        String classPathResourceName = resourceName.replace("classpath:", "");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            InputStream resourceAsStream = classLoader.getResourceAsStream(classPathResourceName);
            if (resourceAsStream != null) {
                return resourceAsStream;
            }
        }

        throw new ConfigException("Classpath resource not found: " + classPathResourceName);
    }
}
