package org.example.ejb.service.impl;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.example.common.config.ImageCategory;
import org.example.ejb.service.ImageResizerService;
import org.example.model.exception.ApplicationException;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@ApplicationScoped
public class ThumbnailatorImageResizerServiceImpl implements ImageResizerService {
    @Override
    public void resize(Path sourcePath, Path destinationPath, ImageCategory imageCategory) {
        try {
            Thumbnails.Builder<File> builder = Thumbnails.of(sourcePath.toFile());
            if (imageCategory.isCrop()) {
                builder.crop(Positions.CENTER);
            }
            builder
                    .size(imageCategory.getWidth(), imageCategory.getHeight())
                    .outputFormat(imageCategory.getOutputExtensionFormat())
                    .outputQuality(imageCategory.getQuality())
                    .allowOverwrite(true)
                    .toFile(destinationPath.toFile());
        } catch (IOException ioException) {
            throw new ApplicationException("Can't resize image: " + ioException.getMessage(), ioException);
        }
    }
}
