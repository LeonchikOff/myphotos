package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.example.common.cdi.annotation.converter.ConvertAsUrl;

@Setter
@Getter
public class ProfilePhotoRest {

    private Long id;

    private Long countOfViews;

    private Long countOfDownloads;

    @ConvertAsUrl
    private String urlToSmall;
}
