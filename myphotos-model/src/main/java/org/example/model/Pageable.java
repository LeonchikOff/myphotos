package org.example.model;

import org.example.exception.ValidationException;

public class Pageable {
    private final int numberOfPage;
    private final int limitElementsPerPage;

    public Pageable(int numberOfPage, int limitElementsPerPage) {
        if(numberOfPage < 1) throw new ValidationException("Invalid page value. Value of page should be greater than or equal to 1.");
        if(limitElementsPerPage < 1) throw new ValidationException("Invalid limit value. Value of limit should be greater than or equal to 1.");
        this.numberOfPage = numberOfPage;
        this.limitElementsPerPage = limitElementsPerPage;
    }

    public Pageable(int limitElementsPerPage) {
        this(1, limitElementsPerPage);
    }

    public int getNumberOfPage() {
        return numberOfPage;
    }

    public int getLimitElementsPerPage() {
        return limitElementsPerPage;
    }

    public int getOffset() {
        return (numberOfPage - 1) * limitElementsPerPage;
    }
}
