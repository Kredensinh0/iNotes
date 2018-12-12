package com.example.pokrz.inotes2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokrz.inotes2.R;
import com.example.pokrz.inotes2.entities.Category;
import com.example.pokrz.inotes2.entities.Note;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private List<Category> categories = new ArrayList<>();

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_item_layout, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.textViewCategoryTitle.setText(currentCategory.getTitle());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        private TextView textViewCategoryTitle;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryTitle = itemView.findViewById(R.id.text_view_category_title_drawer_item);
        }


    }
}
