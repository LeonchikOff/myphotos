package org.example.common.cdi.producer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

@ApplicationScoped
public class ApplicationPropertiesStorage extends AbstractPropertiesLoader {

    private final Properties applicationProperties = new Properties();

    Properties getApplicationProperties() {
        return applicationProperties;
    }

    @PostConstruct
    private void postConstruct() {
        this.fillProperties(applicationProperties, "classpath:application.properties");
        overrideApplicationProperties(applicationProperties, System.getenv(), "System environment");
        overrideApplicationProperties(applicationProperties, System.getProperties(), "System properties");
        logger.log(Level.INFO, "Application properties loaded successful");
    }

    private static final String APPLICATION_CONFIG_PROPERTY_PREFIX = "myphotos.";
    private static final String APPLICATION_CONFIG_FILE = "MYPHOTOS_CONFIG_FILE";

    private void overrideApplicationProperties(Properties applicationProperties, Map<?, ?> map, String description) {
        String configFilePath = null;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            if (key.startsWith(APPLICATION_CONFIG_PROPERTY_PREFIX)) {
                applicationProperties.setProperty(key, value);
                logger.log(Level.INFO, "Overridden application property {0}, defined in the {1}", new String[]{key, description});
            } else if (key.equals(APPLICATION_CONFIG_FILE)) {
                configFilePath = value;
            }
        }
        if (configFilePath != null && Files.exists(Paths.get(configFilePath))) {
            fillProperties(applicationProperties, configFilePath);
            logger.log(Level.INFO, "Overridden application properties from file {0}, defined in the {1}", new String[]{configFilePath, description});
        }
    }
}
