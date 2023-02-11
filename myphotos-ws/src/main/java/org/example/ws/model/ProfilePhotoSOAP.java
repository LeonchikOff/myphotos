package org.example.ws.model;

import org.example.common.cdi.annotation.converter.ConvertAsUrl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "profilePhoto")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProfilePhotoSOAP {

    @XmlAttribute(required = true)
    private Long id;
    @ConvertAsUrl
    private String urlToSmall;
    private Long countOfViews;
    private Long countOfDownloads;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlToSmall() {
        return urlToSmall;
    }

    public void setUrlToSmall(String urlToSmall) {
        this.urlToSmall = urlToSmall;
    }

    public Long getCountOfViews() {
        return countOfViews;
    }

    public void setCountOfViews(Long countOfViews) {
        this.countOfViews = countOfViews;
    }

    public Long getCountOfDownloads() {
        return countOfDownloads;
    }

    public void setCountOfDownloads(Long countOfDownloads) {
        this.countOfDownloads = countOfDownloads;
    }
}
