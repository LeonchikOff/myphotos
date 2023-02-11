package org.example.common.converter.impl;

import org.example.common.cdi.annotation.Property;
import org.example.common.converter.UrlConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

@ApplicationScoped
public class DefaultUrlConverterImpl implements UrlConverter {

    @Inject
    @Property(nameOfProperty = "myphotos.host")
    private String host;


    @Override
    public String convertRelativeUrlToAbsolute(String url) {
        try {
            if (new URI(url).isAbsolute())
                return url;
        } catch (URISyntaxException e) {
            //do nothing, because url is relative;
        }
        return host + url;
    }
}
