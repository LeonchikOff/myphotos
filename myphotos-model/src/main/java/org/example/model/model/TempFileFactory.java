package org.example.model.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TempFileFactory {

    private static class CantCreateTempFileException extends IllegalStateException {
        public CantCreateTempFileException(Path tempFilePath, Throwable exception) {
            super("Can't create temp file: " + tempFilePath, exception);
        }
    }

    public static Path createTempFile(String fileExtension) {
        String uniqueTempFileName = String.format("%s.%s", UUID.randomUUID(), fileExtension);
        String tempDirectoryPathProperty = System.getProperty("java.io.tmpdir");
        Path tempFilePath = Paths.get(tempDirectoryPathProperty, uniqueTempFileName);
        try {
            return Files.createFile(tempFilePath);
        } catch (IOException ioException) {
            throw new CantCreateTempFileException(tempFilePath, ioException);
        }
    }

    public static void deleteTempFile(Path path) {
//        TODO Невозможно удлить временный фаил (эксепшн при сохранении редактированного нового зарегистрированного пользователя)
        try {
            Files.deleteIfExists(path);
        } catch (IOException | RuntimeException exception) {
            Logger.getLogger("TempFileEraser").log(Level.WARNING, "Can't delete temp file: " + path, exception);
        }
    }
}
