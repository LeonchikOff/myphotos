package org.example.common.converter.impl;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.example.common.cdi.annotation.converter.ConvertAsUrl;
import org.example.common.converter.ModelConverter;
import org.example.common.converter.UrlConverter;
import org.example.model.exception.ConfigException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class DefaultModelConverterImpl implements ModelConverter {

    @Inject
    private UrlConverter urlConverter;

    @Override
    public <SourceType, DestinationType> DestinationType convert
            (SourceType source, Class<DestinationType> destinationTypeClass) {
        try {
            DestinationType destinationTypeInstance = destinationTypeClass.newInstance();
            copyProperties(Objects.requireNonNull(source, "Source can't be null"), destinationTypeInstance);
            return destinationTypeInstance;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConfigException(String.format
                    ("Can't convert object from %s to %s: %s", source.getClass(), destinationTypeClass, e.getMessage()));
        }
    }


    private <SourceType, DestinationType> void copyProperties(SourceType sourceObj, DestinationType destinationObj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor[] propertyDescriptors = propertyUtils.getPropertyDescriptors(destinationObj);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            if (!propertyName.equals("class")
                    && propertyUtils.isReadable(sourceObj, propertyName)
                    && propertyUtils.isWriteable(destinationObj, propertyName)) {
                Object propertySourceValue = propertyUtils.getProperty(sourceObj, propertyName);
                if (propertySourceValue != null) {
                    Class<?> propertyDestinationType = propertyUtils.getPropertyType(destinationObj, propertyName);

                    Object propertyDestinationValue;
                    if (propertyDestinationType.isPrimitive() || propertySourceValue.getClass().isPrimitive()) {
                        propertyDestinationValue = propertySourceValue;
                    } else {
                        if (hasAnnotationConvertAsUrl(destinationObj, propertyName)) {
                            propertyDestinationValue = urlConverter.convertRelativeUrlToAbsolute(String.valueOf(propertySourceValue));
                        } else if (propertySourceValue.getClass() != propertyDestinationType) {
                            propertyDestinationValue = convert(propertySourceValue, propertyDestinationType);
                        } else {
                            propertyDestinationValue = propertySourceValue;
                        }
                    }
                    propertyUtils.setProperty(destinationObj, propertyName, propertyDestinationValue);
                }
            }
        }
    }

    private <DestinationType> boolean hasAnnotationConvertAsUrl(DestinationType destinationObj, String propertyName) {
        Field field = null;
        Class<?> destinationClass = destinationObj.getClass();
        while (destinationClass != null) {
            try {
                field = destinationClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException ignored) {
            }
            destinationClass = destinationClass.getSuperclass();
        }
        return field != null && field.isAnnotationPresent(ConvertAsUrl.class);
    }


    @Override
    public <SourceType, DestinationType> List<DestinationType> convertList(List<SourceType> sourceList, Class<DestinationType> destinationTypeClass) {
        return sourceList
                .stream()
                .map(sourceTypeObj -> DefaultModelConverterImpl.this.convert(sourceTypeObj, destinationTypeClass))
                .collect(Collectors.toCollection(() -> new ArrayList<>(sourceList.size())));
    }
}
