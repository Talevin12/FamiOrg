package com.example.famiorg.logic;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ImagePost implements Serializable {
    private String id = UUID.randomUUID().toString();
    private String email;
    @Exclude private String userName;
    @Exclude private int icon;
    private Date publishDate;
    private String imageLink;
    private String description;

    public String getId() {
        return id;
    }

    public ImagePost setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ImagePost setEmail(String email) {
        this.email = email;
        return this;
    }

    @Exclude
    public String getUserName() {
        return userName;
    }

    @Exclude
    public ImagePost setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Exclude
    public int getIcon() {
        return icon;
    }

    @Exclude
    public ImagePost setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public ImagePost setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public ImagePost setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ImagePost setDescription(String description) {
        this.description = description;
        return this;
    }
}
