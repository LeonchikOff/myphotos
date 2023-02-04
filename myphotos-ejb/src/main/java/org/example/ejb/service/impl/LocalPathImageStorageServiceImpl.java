package org.example.ejb.service.impl;

import org.example.common.cdi.annotation.Property;
import org.example.common.config.ImageCategory;
import org.example.ejb.service.FileNameGeneratorService;
import org.example.ejb.service.ImageStorageService;
import org.example.model.exception.ApplicationException;
import org.example.model.model.OriginalImage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class LocalPathImageStorageServiceImpl implements ImageStorageService {

    @Inject
    private Logger logger;

    @Inject
    private FileNameGeneratorService fileNameGeneratorService;

    @Inject
    @Property(nameOfProperty = "myphotos.storage.root.dir")
    private String storageImageRoot;

    @Inject
    @Property(nameOfProperty = "myphotos.media.absolute.root")
    private String mediaRoot;


    @Override
    public String saveProtectedOriginalImage(Path sourcePath) {
        String uniqueFileName = fileNameGeneratorService.generateUniqueFileName();
        Path destinationPath = Paths.get(storageImageRoot + uniqueFileName);
        this.saveImage(sourcePath, destinationPath);
        return uniqueFileName;
    }

    @Override
    public String savePublicImage(ImageCategory imageCategory, Path sourcePath) {
        String uniqueFileName = fileNameGeneratorService.generateUniqueFileName();
        Path destinationPath = Paths.get(mediaRoot + imageCategory.getRelativeRoot() + uniqueFileName);
        this.saveImage(sourcePath, destinationPath);
        return "/" + imageCategory.getRelativeRoot() + uniqueFileName;

    }

    private void saveImage(Path sourcePath, Path destinationPath) {
        try {
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | RuntimeException exception) {
            logger.log(Level.WARNING, String.format("Move failed from %s to %s. Try to copy... ", sourcePath, destinationPath), exception);
            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioException) {
                ApplicationException applicationException =
                        new ApplicationException("Can't save image: " + destinationPath, ioException);
                applicationException.addSuppressed(exception);
                throw applicationException;
            }
        }
        logger.log(Level.INFO, "Saved image: {0}", destinationPath);
    }

    @Override
    public void deletePublicImage(String url) {
        Path destinationPath = Paths.get(mediaRoot + url.substring(1));
        try {
            Files.deleteIfExists(destinationPath);
        } catch (IOException | RuntimeException exception) {
            logger.log(Level.SEVERE, "Delete public image failed: " + destinationPath, exception);
        }
    }

    @Override
    public OriginalImage getOriginalImage(String originalImageUrl) {
        Path originalPath = Paths.get(storageImageRoot + originalImageUrl);
        try {
            return new OriginalImage(
                    Files.newInputStream(originalPath),
                    originalPath.getFileName().toString(),
                    Files.size(originalPath));
        } catch (IOException ioException) {
            throw new ApplicationException(String.format("Can't get access to original image by this path: %s,", originalPath), ioException);
        }
    }
}
