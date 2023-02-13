package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProfileWithPhotosRest extends ProfileRest {

    private List<? extends ProfilePhotoRest> photos;

}
