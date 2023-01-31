package org.example.common.cdi.producer;

import org.example.common.cdi.annotation.Property;
import org.example.model.exception.ConfigException;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@Dependent
public class PropertyProducer {

    @Inject
    private ApplicationPropertiesStorage applicationPropertiesStorage;

    @Produces
    @Property
    public String resolveStringPropertyValue(InjectionPoint injectionPoint) {
        return resolvePropertyValue(injectionPoint);
    }

    @Produces
    @Property
    public int resolveIntPropertyValue(InjectionPoint injectionPoint) {
        return Integer.parseInt(resolvePropertyValue(injectionPoint));
    }

    @Produces
    @Property
    public boolean resolveBooleanPropertyValue(InjectionPoint injectionPoint) {
        return Boolean.parseBoolean(resolvePropertyValue(injectionPoint));
    }

    private String resolvePropertyValue(InjectionPoint injectionPoint) {
        String injectedFieldName  = injectionPoint.getMember().getName();
        String className = injectionPoint.getMember().getDeclaringClass().getName();
        Property propertyAnnotation = injectionPoint.getAnnotated().getAnnotation(Property.class);
        return resolvePropertyValue(className, injectedFieldName , propertyAnnotation);
    }

    private String resolvePropertyValue(String className, String injectedFieldName, Property propertyAnnotation) {
        String nameOfPropertyByAnnotation = propertyAnnotation.nameOfProperty();
        String propertyName = nameOfPropertyByAnnotation.equals("") ? String.format("%s.%s", className, injectedFieldName) : nameOfPropertyByAnnotation;
        String propertyValue = applicationPropertiesStorage.getApplicationProperties().getProperty(propertyName);
        if (propertyValue == null) {
            throw new ConfigException(
                    String.format("Can't resolve property: '%s' for injection to %s.%s", propertyName, className, injectedFieldName));
        } else {
            return propertyValue;
        }
    }
}
