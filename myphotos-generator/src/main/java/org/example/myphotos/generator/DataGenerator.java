package org.example.myphotos.generator;

import org.example.common.cdi.annotation.Property;
import org.example.common.config.ImageCategory;
import org.example.ejb.service.beans.PhotoServiceBean;
import org.example.ejb.service.beans.ProfileServiceBean;
import org.example.ejb.service.beans.UpdateProfileRatingBean;
import org.example.model.model.ImageResourceTempImpl;
import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;
import org.example.myphotos.generator.component.AbstractEnvironmentGenerator;
import org.example.myphotos.generator.component.PhotoGenerator;
import org.example.myphotos.generator.component.ProfileGenerator;
import org.example.myphotos.generator.component.UpdatePhotoService;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator extends AbstractEnvironmentGenerator {

    public static void main(String[] args) throws Exception {
        new DataGenerator().execute();
    }

    @Inject
    private ProfileGenerator profileGenerator;

    @Inject
    private PhotoGenerator photoGenerator;

    @Inject
    private UpdatePhotoService updatePhotoService;

    @EJB
    private ProfileServiceBean profileServiceBean;

    @EJB
    private PhotoServiceBean photoServiceBean;

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
        List<Photo> uploadedPhotos = new ArrayList<>();

        for (Profile profile : generatedProfiles) {
            profileServiceBean.signUpWithDeliveryToDB(profile, false);
            profileServiceBean.uploadNewAvatar(profile, new PathImageResource(profile.getAvatarUrl()));

            List<String> pathsPhotos = photoGenerator.generatePhotos(profile.getPhotoCount());
            for (String pathPhoto : pathsPhotos) {
                Profile dbProfile = profileServiceBean.findById(profile.getId());
                Photo photo = photoServiceBean.uploadNewPhotoAndGet(dbProfile, new PathImageResource(pathPhoto));
                uploadedPhotos.add(photo);
            }
        }

        updatePhotoService.updatePhotos(uploadedPhotos);
        this.updateProfileRating();
        System.out.println("Generated " + generatedProfiles.size() + " profiles");
        System.out.println("Generated " + uploadedPhotos.size() + " photos");
    }

    private void updateProfileRating() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT update_rating()")) {
            connection.setAutoCommit(false);
            preparedStatement.executeQuery();
            connection.commit();
        }
    }

    public static class PathImageResource extends ImageResourceTempImpl {
        public PathImageResource(String path) throws IOException {
            Files.copy(Paths.get(path), this.getTempPath(), StandardCopyOption.REPLACE_EXISTING);
        }
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
            statement.executeQuery("SELECT setval('photo_seq', 123456, false)");
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
