package org.example.common.cdi.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.logging.Logger;

public class ClassExtensionForPrintAllDiscoveredBeans implements Extension {
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    <T> void processAnnotationType(@Observes ProcessAnnotatedType<T> processAnnotatedType, BeanManager beanManager) {
        String className = processAnnotatedType.getAnnotatedType().getJavaClass().getName();
        if (className.startsWith("org.example")) {
            Set<Annotation> annotationsSet = processAnnotatedType.getAnnotatedType().getAnnotations();
            logger.info(String.format("Discovered bean of %s with annotations: %s", className, annotationsSet));
        }

    }
}
