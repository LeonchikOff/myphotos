package org.example.ejb;

import org.example.common.cdi.annotation.PropertiesSource;
import org.example.common.cdi.annotation.Property;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Properties;

@Singleton
@Startup
public class Test {

    @Inject
    @PropertiesSource(resourceFileName = "classpath:application.properties")
    private Properties properties;

    @Inject
    @Property(nameOfProperty = "myphotos.host")
    private String host;

    @Inject
    @Property(nameOfProperty = "myphotos.social.facebook.client-id")
    private int clientId;

    @Inject
    @Property(nameOfProperty = "myphotos.text.boolean")
    private boolean testBoolean;


    @PostConstruct
    private void postConstruct() {
        System.out.println("postConstruct()!!!");
        System.out.println("Inject Field-----------------host=" + host);
        System.out.println("Inject Field-----------------clientId=" + clientId);
        System.out.println("Inject Field-----------------testBoolean=" + testBoolean);
        System.out.println("Inject Properties-----------------properties=" + properties);
    }
}
