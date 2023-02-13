package org.example.rest.converters;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.example.common.config.Constants;
import org.example.model.exception.ApplicationException;
import org.example.model.exception.ValidationException;
import org.example.rest.model.UploadImageRest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;

@Provider
@ApplicationScoped
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class UploadImageMessageBodyReader implements MessageBodyReader<UploadImageRest> {

    private File tempDirectory;

    @PostConstruct
    private void postConstruct() {
        try {
            tempDirectory = Files.createTempDirectory("upload").toFile();
        } catch (IOException ioException) {
            throw new ApplicationException("Can't create temp directory", ioException);
        }
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return UploadImageRest.class.isAssignableFrom(type);
    }

    @Override
    public UploadImageRest readFrom(
            Class<UploadImageRest> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, tempDirectory);
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        servletFileUpload.setFileSizeMax(Constants.MAX_UPLOADED_PHOTO_SIZE_IN_BYTES);
        try {
            List<FileItem> items = servletFileUpload
                    .parseRequest(new JAXRSRequestContext(httpHeaders, entityStream, mediaType.toString()));
            for (FileItem fileItem : items)
                if (fileItem.isFormField())
                    return new UploadImageRest(fileItem);

        } catch (Exception exception) {
            throw new ApplicationException("Can't parse multipart request: " + exception.getMessage(), exception);
        }
        throw new ValidationException("Missing content");
    }
}
