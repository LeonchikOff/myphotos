package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageLinkRest {

    private String url;

    public ImageLinkRest() {
    }

    public ImageLinkRest(String url) {
        this.url = url;
    }
}
