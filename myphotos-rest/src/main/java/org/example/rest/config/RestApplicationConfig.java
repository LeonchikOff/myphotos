package org.example.rest.config;

import io.swagger.jaxrs.config.BeanConfig;
import org.example.rest.Constants;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(Constants.CURRENT_VERSION)
public class RestApplicationConfig extends Application {

    public RestApplicationConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setTitle("MyPhotos.com API (Version 1.0)");
        beanConfig.setBasePath("/v1");
        beanConfig.setResourcePackage("org.example.rest");
        beanConfig.setPrettyPrint(true);
        beanConfig.setLicenseUrl("https://www.apache.org/licenses/LICENSE-2.0");
        beanConfig.setScan(true);
    }
}
