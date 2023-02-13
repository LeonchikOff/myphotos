package org.example.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ValidationItemRest {

    private String field;
    private List<String> message;

    public ValidationItemRest(String field, List<String> message) {
        this.field = field;
        this.message = message;
    }

    public ValidationItemRest() {
    }
}
