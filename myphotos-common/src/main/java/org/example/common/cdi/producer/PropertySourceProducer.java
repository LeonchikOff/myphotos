package org.example.common.cdi.producer;


import org.example.common.cdi.annotation.PropertiesSource;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.Properties;

@Dependent
public class PropertySourceProducer extends AbstractPropertiesLoader {

    @Produces
    @PropertiesSource(resourceFileName = "")
    private Properties resolveProperties(InjectionPoint injectionPoint) {
        Properties properties = new Properties();
        PropertiesSource propertiesSourceAnnotation
                = injectionPoint.getAnnotated().getAnnotation(PropertiesSource.class);
        this.fillProperties(properties, propertiesSourceAnnotation.resourceFileName());
        return properties;
    }
}
