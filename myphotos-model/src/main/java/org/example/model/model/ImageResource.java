package org.example.model.model;

import java.nio.file.Path;

public interface ImageResource extends AutoCloseable {

    Path getTempPath();

    @Override
    void close() throws Exception;
}
