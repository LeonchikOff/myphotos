package org.example.ejb.jpa.listeners;

import org.example.model.model.domain.AbstractDomain;

import javax.persistence.PrePersist;
import java.util.Date;

public class DateOfCreatedNowEntityListener {

    @PrePersist
    public void setDateOfCreatedNow(AbstractDomain abstractDomainModel) {
        abstractDomainModel.setDateOfCreated(new Date(System.currentTimeMillis() - 1));
    }
}
