package com.example.pokrz.inotes2.daos;

import com.example.pokrz.inotes2.entities.Note;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao extends BaseDao<Note> {

    @Override
    @Insert
    void insert(Note data);

    @Override
    @Delete
    void delete(Note data);

    @Override
    @Update
    void update(Note data);

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAllNotes();

}
