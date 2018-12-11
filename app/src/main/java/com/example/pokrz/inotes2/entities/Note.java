package com.example.pokrz.inotes2.entities;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    int id;
    private String title;
    private String description;
    private Date dateCreated;
    private Date dateLastUpdated;
    private Location location;
    private String categoryTitle;
    private String optionalImagePath;

    public Note(String title, String description, Date dateCreated, Date dateLastUpdated, Location location, String categoryTitle, String optionalImagePath) {
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastUpdated = dateLastUpdated;
        this.location = location;
        this.categoryTitle = categoryTitle;
        this.optionalImagePath = optionalImagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getOptionalImagePath() {
        return optionalImagePath;
    }

    public void setOptionalImagePath(String optionalImagePath) {
        this.optionalImagePath = optionalImagePath;
    }
}