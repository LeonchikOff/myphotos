package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.example.common.cdi.annotation.converter.ConvertAsUrl;

@Setter
@Getter
public class ProfileRest extends SimpleProfileRest {

    private String firstName;

    private String lastName;

    private String jobTitle;

    private String location;

    private int photoCount;

    @ConvertAsUrl
    private String avatarUrl;
}
