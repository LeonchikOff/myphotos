package org.example.ws.controller;


import org.example.common.cdi.annotation.Property;
import org.example.ws.services.service_impl_beans.PhotoWebServiceBean;
import org.example.ws.services.service_impl_beans.ProfileWebServiceBean;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/index.html")
public class IndexControllerServlet extends HttpServlet {

    @Inject
    @Property(nameOfProperty = "myphotos.host.soap.api")
    private String soapHost;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<WebServiceModel> webServices = new ArrayList<>();
        Arrays.stream(new Class<?>[]{ProfileWebServiceBean.class, PhotoWebServiceBean.class}).forEach(aClass -> {
            webServices.add(new WebServiceModel(aClass.getAnnotation(WebService.class)));
        });

        req.setAttribute("webServices", webServices);
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }

    public class WebServiceModel {

        private final String name;
        private final String port;
        private final String address;

        public WebServiceModel(WebService webServiceAnnotation) {
            this.name = webServiceAnnotation.serviceName();
            this.port = webServiceAnnotation.portName();
            this.address = String.format("%s/%s", soapHost, webServiceAnnotation.serviceName());
        }

        public String getName() {
            return name;
        }

        public String getPort() {
            return port;
        }

        public String getAddress() {
            return address;
        }
    }
}
