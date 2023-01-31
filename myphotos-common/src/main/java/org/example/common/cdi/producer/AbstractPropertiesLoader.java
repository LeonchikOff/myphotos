package org.example.common.cdi.producer;


import org.example.common.cdi.resource.ResourceLoaderManager;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


abstract class AbstractPropertiesLoader {

    @Inject
    protected Logger logger;

    @Inject
    protected ResourceLoaderManager resourceLoaderManager;

    protected void fillProperties(Properties properties, String resourceName) {
        try {
            try (InputStream inputStreamResource
                         = resourceLoaderManager.getResourceInputStream(resourceName)) {
                properties.load(inputStreamResource);
            }
            logger.log(Level.INFO, "Successful loaded properties from {0}", resourceName);
        } catch (IOException | RuntimeException exception) {
            logger.log(Level.WARNING, "Can't load properties  from: " +  resourceName, exception);
        }
    }
}
