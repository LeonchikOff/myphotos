package org.example.common.model;

import org.example.model.exception.ApplicationException;
import org.example.model.exception.ValidationException;
import org.example.model.model.ImageResource;
import org.example.model.model.TempFileFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractMimeTypeImageResourceImpl implements ImageResource {

    private Path tempPath;

    protected abstract void copyContent() throws Exception;

    @Override
    public final Path getTempPath() {
        if (tempPath == null) {
            tempPath = TempFileFactory.createTempFile(this.getExtension());
            try {
                copyContent();
            } catch (Exception e) {
                throw new ApplicationException(
                        String.format("Can't copy content from %s to temp file %s", this.toString(), tempPath), e);
            }
        }
        return tempPath;
    }

    protected abstract String getContentType();

    protected String getExtension() {
        String contentType = getContentType();
        if (contentType.equalsIgnoreCase("image/jpeg"))
            return "jpg";
        else if (contentType.equalsIgnoreCase("image/png"))
            return "png";
        else throw new ValidationException("Only JPEG/JPG and PNG formats supported. Current format is " + contentType);
    }

    protected abstract void deleteTempResource() throws IOException;

    @Override
    public final void close() throws Exception {
        TempFileFactory.deleteTempFile(tempPath);
        try {
            deleteTempResource();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
                    "Can't delete temp resource from " + this.toString(), e);
        }
    }

    @Override
    public abstract String toString();
}
