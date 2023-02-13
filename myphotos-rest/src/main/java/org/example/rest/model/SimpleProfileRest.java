package org.example.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("SimpleProfile")
@Setter
@Getter
public class SimpleProfileRest {

    @ApiModelProperty(required = true, value = "Profile Id. Uses as unique identification of profile via rest api")
    private Long id;

    @ApiModelProperty(required = true, value = "Profile uid. " +
            "Can be useful if user wants to open profile via browser from his mobile application. " +
            "Profile unique url will be available at http://myphotos.com/${uid}")
    private String uid;

}
