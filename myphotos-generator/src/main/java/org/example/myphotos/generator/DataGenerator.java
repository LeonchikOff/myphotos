package org.example.myphotos.generator;

import org.example.common.cdi.annotation.Property;
import org.example.common.config.ImageCategory;
import org.example.model.model.domain.Profile;
import org.example.myphotos.generator.component.AbstractEnvironmentGenerator;
import org.example.myphotos.generator.component.PhotoGenerator;
import org.example.myphotos.generator.component.ProfileGenerator;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DataGenerator extends AbstractEnvironmentGenerator {

    public static void main(String[] args) throws Exception {
        new DataGenerator().execute();
    }

    @Inject
    private ProfileGenerator profileGenerator;

    @Inject
    private PhotoGenerator photoGenerator;

    @Resource(mappedName = "MyPhotosDBPoll")
    private DataSource dataSource;

    @Inject
    @Property(nameOfProperty = "myphotos.storage.root.dir")
    private String storageRoot;

    @Inject
    @Property(nameOfProperty = "myphotos.media.absolute.root")
    private String mediaRoot;

    @Override
    protected void generate() throws Exception {
        this.clearExternalResources();
        List<Profile> generatedProfiles = profileGenerator.generateProfiles();
//        TODO create Profiles
        System.out.println("Generated " + generatedProfiles.size() + " profiles");
    }

    private void clearExternalResources() throws SQLException, IOException {
        this.clearDatabase();
        this.clearDirectory(storageRoot);
        this.clearDirectory(mediaRoot + ImageCategory.LARGE_PHOTO.getRelativeRoot());
        this.clearDirectory(mediaRoot + ImageCategory.SMALL_PHOTO.getRelativeRoot());
        this.clearDirectory(mediaRoot + ImageCategory.PROFILE_AVATAR.getRelativeRoot());
    }

    private void clearDatabase() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE photo CASCADE");
            statement.executeUpdate("TRUNCATE access_token CASCADE");
            statement.executeUpdate("TRUNCATE profile CASCADE");
            statement.executeQuery("SELECT setval('profile_seq', 1, false)");
            statement.executeQuery("SELECT setval('photo_seq'), 123456, false");
        }
        System.out.println("Database cleared");
    }

    private void clearDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return super.visitFile(file, attrs);
                }
            });
            System.out.println("Directory " + directoryPath + " were cleared");
        } else {
            Files.createDirectories(path);
        }
    }
}
