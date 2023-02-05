package org.example.myphotos.generator.component;

import org.example.ejb.repositories.PhotoRepository;
import org.example.model.model.domain.Photo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class UpdatePhotoService {

    @Inject
    private PhotoRepository photoRepository;

    private final Random random = new Random();

    @Transactional
    public void updatePhotos(List<Photo> photoList) {
        photoList.forEach(photo -> {
            photo.setCountOfDownloads(random.nextInt(100));
            photo.setCountOfViews(random.nextInt(1000) * 5 + 100);
            photoRepository.update(photo);
        });
    }
}
