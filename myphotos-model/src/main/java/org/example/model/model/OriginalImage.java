package org.example.model.model;

import java.io.InputStream;
import java.util.Objects;

public class OriginalImage {
    private final InputStream inputStream;
    private final String name;
    private final long size;

    public OriginalImage(InputStream inputStream, String name, long size) {
        Objects.requireNonNull(this.inputStream = inputStream);
        Objects.requireNonNull(this.name = name);
        this.size = size;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}
