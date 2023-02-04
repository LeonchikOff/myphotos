package org.example.ejb.service;

import org.example.common.config.ImageCategory;
import org.example.model.model.OriginalImage;

import java.nio.file.Path;

public interface ImageStorageService {

    String saveProtectedOriginalImage(Path path);

    String savePublicImage(ImageCategory imageCategory, Path path);

    void deletePublicImage(String url);

    OriginalImage getOriginalImage(String originalImageUrl);
}
