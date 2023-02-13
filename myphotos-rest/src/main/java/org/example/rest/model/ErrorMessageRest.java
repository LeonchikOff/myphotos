package org.example.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("ErrorMessage")
public class ErrorMessageRest {

    @ApiModelProperty(required = true,
            value = "Error message. " +
                    "This message should be displayed to user if userError = true, otherwise it is message for developer.")
    private String message;
    @ApiModelProperty(required = true,
            value = "Flag field for category of error message. " +
                    "If userError = true, message should be displayed to user, otherwise it is message for developer")
    private boolean userError;

    public ErrorMessageRest() {
    }

    public ErrorMessageRest(String message) {
        this(message, false);
    }

    public ErrorMessageRest(String message, boolean userError) {
        this.message = message;
        this.userError = userError;
    }
}
