package org.example.rest.converters;

import org.apache.commons.fileupload.RequestContext;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JAXRSRequestContext implements RequestContext{
    private final MultivaluedMap<String, String> httpHeaders;
    private final InputStream entityStream;
    private final String contentType;

    public JAXRSRequestContext(MultivaluedMap<String, String> httpHeaders, InputStream entityStream, String contentType) {
        this.httpHeaders = httpHeaders;
        this.entityStream = entityStream;
        this.contentType = contentType;
    }

    @Override
    public String getCharacterEncoding() {
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    @Deprecated
    public int getContentLength() {
        return Integer.parseInt(httpHeaders.getFirst("Content-length"));
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return entityStream;
    }
}
