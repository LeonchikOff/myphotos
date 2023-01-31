package org.example.model.model;

import org.example.model.exception.ValidationException;

public enum SortMode {
    POPULAR_PHOTO,
    POPULAR_AUTHOR;

    public static SortMode of(String nameOfSortMode) {
        for (SortMode value : SortMode.values()) {
            if (value.name().equalsIgnoreCase(nameOfSortMode)) {
                return value;
            }
        }
        throw new ValidationException("Undefined sort mode: " + String.valueOf(nameOfSortMode).toUpperCase());
    }
}
