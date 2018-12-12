package com.example.pokrz.inotes2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;

    public Category(String title) {
        this.title = title;
    }

    //TODO: pre-populate with "uncategorized" category (non-deleteable)

    protected Category(Parcel in) {
        id = in.readInt();
        title = in.readString();
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

    @Override
    public String toString() {
        return title;
    }

    public static Category[] populateData() {
        return new Category[] {
                new Category("All notes"),
                new Category("Uncategorized")
        };
    }
}
