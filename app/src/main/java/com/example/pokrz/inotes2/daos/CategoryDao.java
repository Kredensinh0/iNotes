package com.example.pokrz.inotes2.daos;

import com.example.pokrz.inotes2.entities.Category;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category data);

    @Delete
    void delete(Category data);

    @Update
    void update(Category data);

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();

}
