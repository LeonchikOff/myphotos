package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ValidationResultRest extends ErrorMessageRest{

    private List<ValidationItemRest> items;

    public ValidationResultRest() {
        super("Validation error", true);
    }

    public ValidationResultRest(List<ValidationItemRest> items) {
        this();
        this.items = items;
    }
}
