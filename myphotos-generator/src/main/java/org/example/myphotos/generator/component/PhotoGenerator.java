package org.example.myphotos.generator.component;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class PhotoGenerator {

    private final Random random = new Random();
    private int index = 0;
    private final List<String> pathsToAllPhoto = getPathsToAllTestPhotos();


    private List<String> getPathsToAllTestPhotos() {
        List<String> pathsAllPhoto = new ArrayList<>();
        Path rootDirPhotosPath = Paths.get("external/test-data/photos");
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootDirPhotosPath)) {
            directoryStream.forEach(pathToEachPhoto -> {
                pathsAllPhoto.add(pathToEachPhoto.toAbsolutePath().toString());
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Collections.shuffle(pathsAllPhoto);
        return pathsAllPhoto;
    }

    public List<String> generatePhotos(int photoCount) {
        List<String> photos = new ArrayList<>();
        for (int i = 0; i < photoCount; i++) {
            photos.add(getPhoto());
        }
        return Collections.unmodifiableList(photos);
    }

    private String getPhoto() {
        if (index >= pathsToAllPhoto.size()) index = 0;
        return pathsToAllPhoto.get(index++);
    }

}
