package org.example.ejb.service;

import org.example.common.config.ImageCategory;

import java.nio.file.Path;

public interface ImageResizerService {

    void resize(Path sourcePath, Path destinationPath, ImageCategory imageCategory);
}
