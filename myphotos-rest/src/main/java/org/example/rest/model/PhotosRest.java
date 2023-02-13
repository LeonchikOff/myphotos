package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PhotosRest {

    private Long total;
    private List<? extends ProfilePhotoRest> photos;

    public PhotosRest() {
    }

    public PhotosRest(List<? extends ProfilePhotoRest> photos) {
        this.photos = photos;
    }
}
