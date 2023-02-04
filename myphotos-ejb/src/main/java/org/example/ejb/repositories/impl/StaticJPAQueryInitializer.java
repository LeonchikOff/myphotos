package org.example.ejb.repositories.impl;

import org.apache.commons.lang3.reflect.MethodUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class StaticJPAQueryInitializer {

    @Inject
    private JPARepositoryFinder jpaRepositoryFinder;

    @Inject
    private JPAQueryParser jpaQueryParser;

    @Inject
    private JPAQueryCreator jpaQueryCreator;


    @PostConstruct
    private void postConstruct() {
        Set<Class<?>> jpaRepositoryClasses = jpaRepositoryFinder.getJPARepositoryClasses();
        Map<String, String> mapOfNamedQueries = jpaQueryParser.getMapOfNamedQueries(jpaRepositoryClasses);
        jpaQueryCreator.addAllNamedQueriesToEntityManagerFactory(mapOfNamedQueries);
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface JPAQuery {

        String nameQuery() default "";

        String parameterizedQuery();
    }

    @Dependent
    public static class JPARepositoryFinder {

        @Inject
        protected Logger logger;

        @Inject
        protected BeanManager beanManager;

        public Set<Class<?>> getJPARepositoryClasses() {
            Set<Class<?>> setRepositoryClasses = new HashSet<>();
            beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {
            }).forEach(bean -> {
                Class<?> beanClass = bean.getBeanClass();
                if (AbstractJPARepository.class.isAssignableFrom(beanClass)) {
                    setRepositoryClasses.add(beanClass);
                    logger.log(Level.INFO, "Found {0} JPA Repository class", beanClass.getName());
                }
            });
            return setRepositoryClasses;
        }

//        protected Class<?> getRepositoryClass() {
//            return AbstractJPARepository.class;
//        }
//
//        protected boolean isCandidateValid(Bean<?> bean) {
//            return true;
//        }
    }

    @SuppressWarnings("EjbProhibitedPackageUsageInspection")
    @Dependent
    public static class JPAQueryParser {
        public Map<String, String> getMapOfNamedQueries(Set<Class<?>> jpaRepositoryClasses) {
            Map<String, String> mapOfNamedQueries = new HashMap<>();
            for (Class<?> jpaRepositoryClass : jpaRepositoryClasses) {
                Method[] methodsWithAnnotationJPAQuery = MethodUtils.getMethodsWithAnnotation(jpaRepositoryClass, JPAQuery.class);
                for (Method methodJpaRepository : methodsWithAnnotationJPAQuery) {
                    JPAQuery annotationJPAQuery = methodJpaRepository.getAnnotation(JPAQuery.class);
                    String nameQuery = annotationJPAQuery.nameQuery();
                    if (nameQuery.isEmpty()) {
                        nameQuery = String.format("%s.%s", getEntityClass(jpaRepositoryClass), methodJpaRepository.getName());
                    }
                    String parameterizedQuery = annotationJPAQuery.parameterizedQuery();
                    if (mapOfNamedQueries.put(nameQuery, parameterizedQuery) != null) {
                        throw new IllegalStateException("Detected named query duplicates: " + nameQuery);
                    }
                }
            }
            return mapOfNamedQueries;
        }

        protected String getEntityClass(Class<?> jpaRepositoryClass) {
            Type type = jpaRepositoryClass;
            while (type != null) {
                if (type instanceof ParameterizedType)
                    return ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).getSimpleName();
                type = jpaRepositoryClass.getGenericSuperclass();
            }
            throw new IllegalArgumentException("JPA class " + jpaRepositoryClass + " is not generic class");
        }
    }

    @Dependent
    public static class JPAQueryCreator {

        @Inject
        protected Logger logger;

        @PersistenceUnit
        protected EntityManagerFactory entityManagerFactory;

        protected void addAllNamedQueriesToEntityManagerFactory(Map<String, String> mapOfNamedQueries) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                mapOfNamedQueries.forEach((nameQuery, parameterizedQuery) -> {
                    try {
                        entityManagerFactory.addNamedQuery(nameQuery, entityManager.createQuery(parameterizedQuery));
                        logger.log(Level.FINE, "Added named query: {0} -> {1}", new Object[]{nameQuery, parameterizedQuery});
                    } catch (RuntimeException runtimeException) {
                        throw new RuntimeException("Invalid query: " + nameQuery);
                    }
                });
            } finally {
                entityManager.close();
            }
        }
    }
}