package org.example.ejb.repositories.mock;

import org.example.model.model.domain.Photo;
import org.example.model.model.domain.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;


public final class InMemoryDataBase {

    public static final Profile PROFILE;
    public static final List<Photo> PHOTOS;

    static {
        PROFILE = createProfile();
        PHOTOS = createPhotos(PROFILE);
    }


    private static Profile createProfile() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUid("richard-hendricks");

        profile.setDateOfCreated(new Date());
        profile.setFirstName("RichardDDD");
        profile.setLastName("Hendricks");
        profile.setJobTitle("CEO of Pied Piper");
        profile.setLocation("Los Angeles, California");
        profile.setAvatarUrl("https://devstudy-net.github.io/myphotos-com-test-images/Richard-Hendricks.jpg");
        return profile;
    }


    private static List<Photo> createPhotos(Profile profile) {
        Random random = new Random();
        List<Photo> photos = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Photo photo = new Photo();
            photo.setProfile(profile);
            profile.setPhotoCount(profile.getPhotoCount() + 1);
            String imageUrl = String.format("https://devstudy-net.github.io/myphotos-com-test-images/%s.jpg", i % 6 + 1);
            photo.setUrlToSmall(imageUrl);
            photo.setUrlToLarge("https://devstudy-net.github.io/myphotos-com-test-images/large.jpg");
            photo.setUrlToOriginal(imageUrl);
            photo.setCountOfViews(random.nextInt(100) * 10 + 1);
            photo.setCountOfDownloads(random.nextInt(20) * 10 + 1);
            photo.setDateOfCreated(new Date());
            photos.add(photo);
        }
        return Collections.unmodifiableList(photos);
    }

    private InMemoryDataBase() {
    }
}
