package org.example.model.model.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractDomain implements Serializable {

    @Past
    @NotNull
    @Basic(optional = false)
    @Column(name = "date_of_created", nullable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateOfCreated;


    public Date getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(Date dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
