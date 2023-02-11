package org.example.web.models;

import org.example.common.model.AbstractMimeTypeImageResourceImpl;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class PartImageResource extends AbstractMimeTypeImageResourceImpl {

    private final Part part;

    public PartImageResource(Part part) {
        Objects.requireNonNull(this.part = part);
    }

    @Override
    protected void copyContent() throws IOException {
        Files.copy(part.getInputStream(), getTempPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    protected String getContentType() {
        return part.getContentType();
    }

    @Override
    protected void deleteTempResource() throws IOException {
        part.delete();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.getClass().getSimpleName(), part);
    }
}
