package com.example.pokrz.inotes2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokrz.inotes2.R;
import com.example.pokrz.inotes2.entities.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryDrawerAdapter extends RecyclerView.Adapter<CategoryDrawerAdapter.CategoryHolder> {

    private List<Category> categories = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;

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

    public Category getCategoryAt(int position) {
        return categories.get(position);
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

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(categories.get(position));
                }
            });

            itemView.setOnLongClickListener(view -> {
                int position = getAdapterPosition();
                if (onLongClickListener != null && position != RecyclerView.NO_POSITION) {
                    onLongClickListener.onLongClick(categories.get(position));
                }
                return true;
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);

    }

    public interface OnLongClickListener {
        void onLongClick(Category category);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
