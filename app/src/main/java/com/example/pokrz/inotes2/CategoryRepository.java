package com.example.pokrz.inotes2;

import android.app.Application;

import com.example.pokrz.inotes2.daos.CategoryDao;
import com.example.pokrz.inotes2.entities.Category;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private Executor executor;

    public CategoryRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(Category category) {
        executor.execute(() -> categoryDao.insert(category));
    }

    public void update(Category category) {
        executor.execute(() -> categoryDao.update(category));
    }

    public void delete(Category category) {
        executor.execute(() -> categoryDao.delete(category));
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
}
