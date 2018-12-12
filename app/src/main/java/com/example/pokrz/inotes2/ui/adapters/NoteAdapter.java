package com.example.pokrz.inotes2.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokrz.inotes2.R;
import com.example.pokrz.inotes2.entities.Note;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> implements Filterable {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<Note> notesFull;
    private List<Note> notes = new ArrayList<>();

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getLocation().equals(newItem.getLocation()) &&
                    oldItem.getDateCreated().equals(newItem.getDateCreated()) &&
                    oldItem.getDateLastUpdated().equals(newItem.getDateLastUpdated()) &&
                    oldItem.getCategoryTitle().equals(newItem.getCategoryTitle()) &&
                    oldItem.getOptionalImagePath().equals(newItem.getOptionalImagePath());
        }
    };

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notesFull = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_layout, parent, false);
        context = parent.getContext();
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        if (currentNote.getDateLastUpdated() != null && currentNote.getDateCreated() != null) {
            String dUpdated = df.format(currentNote.getDateLastUpdated());
            String dCreated = df.format(currentNote.getDateCreated());
            String date = dCreated + " / " + dUpdated;
            holder.textViewDate.setText(date);
        }
        if (currentNote.getLocation() != null) {
            holder.textViewLocation.setText(getAddress(currentNote.getLocation()));
        } else {
            holder.textViewLocation.setVisibility(View.INVISIBLE);
            holder.imageViewLocation.setVisibility(View.INVISIBLE);
        }

        if (currentNote.getOptionalImagePath() != null) {
            try {
                Uri uri = Uri.parse(currentNote.getOptionalImagePath());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                holder.imageViewOptionalPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                holder.imageViewOptionalPhoto.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            holder.imageViewOptionalPhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private String getAddress(Location location) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String countryCode = addresses.get(0).getCountryCode();
            if (city == null) {
                return state + ", " + countryCode;
            }
            return city + ", " + countryCode;
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown location";
        }
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    @Override
    public Filter getFilter() {
        return noteFilter;
    }

    private Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Note> filteredNotes = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0 || charSequence == "" || charSequence.equals("cat:All notes")) {
                filteredNotes.addAll(notesFull);
            } else {
                if (charSequence.subSequence(0, 4).equals("cat:")) {
                    String filterPattern = charSequence.subSequence(4, charSequence.length()).toString();
                    for (Note note : notesFull) {
                        if (note.getCategoryTitle().contains(filterPattern)) {
                            filteredNotes.add(note);
                        }
                    }
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (Note note : notesFull) {
                        if (note.getTitle().toLowerCase().contains(filterPattern)
                                || note.getDescription().toLowerCase().contains(filterPattern)) {
                            filteredNotes.add(note);
                        }
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredNotes;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notes.clear();
            if (filterResults.values != null) {
                notes.addAll((List) filterResults.values);
            }
            notifyDataSetChanged();
        }
    };

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewDate;
        private TextView textViewLocation;
        private ImageView imageViewLocation;
        private ImageView imageViewOptionalPhoto;

        NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewLocation = itemView.findViewById(R.id.text_view_location);
            imageViewLocation = itemView.findViewById(R.id.image_view_location);
            imageViewOptionalPhoto = itemView.findViewById(R.id.image_view_optional_photo);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
