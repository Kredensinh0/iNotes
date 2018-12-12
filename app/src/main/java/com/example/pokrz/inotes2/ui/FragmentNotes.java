package com.example.pokrz.inotes2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pokrz.inotes2.ui.adapters.NoteAdapter;
import com.example.pokrz.inotes2.NoteViewModel;
import com.example.pokrz.inotes2.R;
import com.example.pokrz.inotes2.entities.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class FragmentNotes extends Fragment implements SearchView.OnQueryTextListener {

    public static final int ADD_NOTE_REQUEST = 0;
    public static final int EDIT_NOTE_REQUEST = 1;

    private NoteViewModel noteViewModel;
    private FloatingActionButton floatingActionButton;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.hasFixedSize();
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(getActivity(), AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_DATE_CREATED, note.getDateCreated());
            intent.putExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH, note.getOptionalImagePath());
            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        });
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            noteAdapter.submitList(notes);
            noteAdapter.setNotes(notes);
        });

        floatingActionButton = getActivity().findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(onClickListener);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Snackbar.make(getActivity().findViewById(R.id.fragment_container), R.string.note_deleted, Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && floatingActionButton.isShown())
                    floatingActionButton.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        SearchView searchView = toolbar.findViewById(R.id.search_view_toolbar);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Date dateCreated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_CREATED);
            Date dateUpdated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_UPDATED);
            String bitmapPath = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH);

            Note note = new Note(title, description, dateCreated, dateUpdated, MainActivity.location, null, bitmapPath);

            noteViewModel.insert(note);
            Toast.makeText(getActivity(), R.string.note_saved, Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Date dateCreated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_CREATED);
            Date dateUpdated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_UPDATED);
            String bitmapPath = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH);

            Note note = new Note(title, description, dateCreated, dateUpdated, MainActivity.location, null, bitmapPath);

            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), R.string.note_not_updated, Toast.LENGTH_SHORT).show();
                return;
            }

            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(getActivity(), R.string.note_updated, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), R.string.note_cancelled, Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener onClickListener = view -> {
        Intent intent = new Intent(getActivity(), AddEditNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE_REQUEST);
    };


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        noteAdapter.getFilter().filter(s);
        return true;
    }
}
