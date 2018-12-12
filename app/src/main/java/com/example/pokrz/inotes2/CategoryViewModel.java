package com.example.pokrz.inotes2;

import android.app.Application;

import com.example.pokrz.inotes2.entities.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }

    public void insert(Category category) {
        categoryRepository.insert(category);
    }

    public void update(Category category) {
        categoryRepository.update(category);
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
}
