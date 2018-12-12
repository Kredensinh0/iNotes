package com.example.pokrz.inotes2.typeconverters;

import com.example.pokrz.inotes2.entities.Category;
import com.google.gson.Gson;

import androidx.room.TypeConverter;

public class CategoryTypeConverter {

    @TypeConverter
    public static String toString(Category category) {
        return new Gson().toJson(category);
    }

    @TypeConverter
    public static Category toCategory(String value) {
        return new Gson().fromJson(value, Category.class);
    }

}
