package org.example.web.listeners;


import org.example.common.cdi.annotation.qualifier.GooglePlus;
import org.example.model.service.SocialService;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Inject
    private Logger logger;

    @Inject
    @GooglePlus
    private SocialService googlePlusSocialService;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("googlePlusClientId", this.googlePlusSocialService.getClientId());
        logger.info("!!!!!!!!!!!!!!!!!!!!!Application \"myphotos\" INITIALIZED");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("!!!!!!!!!!!!!!!!!!Application \"myphotos\" DESTROYED");
    }
}
