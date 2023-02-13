package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadPhotoResultRest extends ImageLinkRest{
    private Long id;

    public UploadPhotoResultRest(Long id, String url) {
        super(url);
        this.id = id;
    }

    public UploadPhotoResultRest() {
    }
}
