package org.example.model.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "photo", schema = "public", catalog = "myphotos")
public class Photo extends AbstractDomain {

    @Id
    @Basic(optional = false)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    @SequenceGenerator(name = "photo_generator", sequenceName = "photo_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_generator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name = "url_to_original", updatable = false, nullable = false, length = 255)
    private String urlToOriginal;

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name = "url_to_small", updatable = false, nullable = false, length = 255)
    private String urlToSmall;

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name = "url_to_large", updatable = false, nullable = false, length = 255)
    private String urlToLarge;

    @Min(0)
    @Basic(optional = false)
    @Column(name = "count_of_views", nullable = false)
    private long countOfViews;

    @Min(0)
    @Basic(optional = false)
    @Column(name = "count_of_downloads", nullable = false)
    private long countOfDownloads;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", updatable = false, nullable = false)
    private Profile profile;

    public Photo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlToOriginal() {
        return urlToOriginal;
    }

    public void setUrlToOriginal(String urlToOriginal) {
        this.urlToOriginal = urlToOriginal;
    }

    public String getUrlToSmall() {
        return urlToSmall;
    }

    public void setUrlToSmall(String urlToSmall) {
        this.urlToSmall = urlToSmall;
    }

    public String getUrlToLarge() {
        return urlToLarge;
    }

    public void setUrlToLarge(String urlToLarge) {
        this.urlToLarge = urlToLarge;
    }

    public long getCountOfViews() {
        return countOfViews;
    }

    public void setCountOfViews(long countOfViews) {
        this.countOfViews = countOfViews;
    }

    public long getCountOfDownloads() {
        return countOfDownloads;
    }

    public void setCountOfDownloads(long countOfDownloads) {
        this.countOfDownloads = countOfDownloads;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
