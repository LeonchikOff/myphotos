package org.example.myphotos.generator.component;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class AbstractEnvironmentGenerator {

    private final Properties propertiesOutOfEnvironment = new Properties();
    private final Map<String, String> mapOfLocalVariables = new HashMap<>();

    protected final void execute() throws Exception {
        Map<String, Object> mapOfPropertiesForCreatingNewEJBContainer = this.setupProperties();
        try (EJBContainer ejbContainer = EJBContainer.createEJBContainer(mapOfPropertiesForCreatingNewEJBContainer)) {
            Context context = ejbContainer.getContext();
            context.bind("inject", this);
            this.generate();
        }
    }

    protected abstract void generate() throws Exception;


    private Map<String, Object> setupProperties() throws IOException {
        Map<String, Object> mapOfPropertiesForCreatingNewEJBContainer = new HashMap<>();
        loadPropertiesOutOfEnvironmentFile(propertiesOutOfEnvironment, "environment.properties");
        addingPropertiesOutOfEnvironmentInMapOfPropertiesForCreatingNewEJBContainer(mapOfPropertiesForCreatingNewEJBContainer);
        setupVariableLocalMavenRepoPath(mapOfLocalVariables);
        addingPathsToModulesInMapOfPropertiesForCreatingNewEJBContainer(mapOfPropertiesForCreatingNewEJBContainer);
        return mapOfPropertiesForCreatingNewEJBContainer;
    }

    private void addingPathsToModulesInMapOfPropertiesForCreatingNewEJBContainer(Map<String, Object> mapOfPropertiesForCreatingNewEJBContainer) {
        List<File> pathsToModulesList = new ArrayList<>();
        //  EJBContainer.MODULES is string: javax.ejb.embeddable.modules
        String[] modulesEJBValue = propertiesOutOfEnvironment.getProperty(EJBContainer.MODULES).split(",");
        for (String moduleEJBValue : modulesEJBValue) {
            pathsToModulesList.add(new File(getFullPathToModule(moduleEJBValue)));
        }
        pathsToModulesList.add(new File("myphotos-generator/target/classes"));
        mapOfPropertiesForCreatingNewEJBContainer
                .put(EJBContainer.MODULES, pathsToModulesList.toArray(new File[pathsToModulesList.size()]));
    }

    private String getFullPathToModule(String moduleEJB) {
        String fullPathToModuleEJB = moduleEJB;
        for (Map.Entry<String, String> variableEntry : mapOfLocalVariables.entrySet()) {
//                                ${M2_LOCAL} replace to System.getProperty("user.home") + "/.m2/repository"
            fullPathToModuleEJB = fullPathToModuleEJB.replace(variableEntry.getKey(), variableEntry.getValue());
        }
        return fullPathToModuleEJB;
    }

    private void addingPropertiesOutOfEnvironmentInMapOfPropertiesForCreatingNewEJBContainer(Map<String, Object> mapOfPropertiesForCreatingNewEJBContainer) {
        for (Map.Entry<Object, Object> propertyEntry : propertiesOutOfEnvironment.entrySet()) {
            mapOfPropertiesForCreatingNewEJBContainer.put((String) propertyEntry.getKey(), propertyEntry.getValue());
        }
    }

    private void setupVariableLocalMavenRepoPath(Map<String, String> mapOfLocalVariables) {
        String systemPropertyUserHome = System.getProperty("user.home");
        String localMavenRepoPath = systemPropertyUserHome + "/.m2/repository";
        mapOfLocalVariables.put("${M2_LOCAL}", localMavenRepoPath);
    }

    private void loadPropertiesOutOfEnvironmentFile(Properties properties, String nameEnvironmentFile) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(nameEnvironmentFile)) {
            properties.load(inputStream);
        }
    }
}
