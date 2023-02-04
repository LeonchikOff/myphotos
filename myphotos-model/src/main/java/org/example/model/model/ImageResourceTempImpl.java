package org.example.model.model;

import java.nio.file.Path;

public class ImageResourceTempImpl implements ImageResource {

    private final Path path;

    public ImageResourceTempImpl(String fileExtension) {
        this.path = TempFileFactory.createTempFile(fileExtension);
    }

    public ImageResourceTempImpl() {
        this("jpg");
    }

    @Override
    public Path getTempPath() {
        return this.path;
    }

    @Override
    public void close() {
        TempFileFactory.deleteTempFile(path);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), path);
    }
}
