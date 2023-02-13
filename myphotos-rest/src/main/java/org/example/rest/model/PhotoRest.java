package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PhotoRest extends ProfilePhotoRest {

    private ProfileRest profile;

}
