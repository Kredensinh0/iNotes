package com.example.pokrz.inotes2;

import android.content.Context;

import com.example.pokrz.inotes2.daos.CategoryDao;
import com.example.pokrz.inotes2.daos.NoteDao;
import com.example.pokrz.inotes2.entities.Category;
import com.example.pokrz.inotes2.entities.Note;
import com.example.pokrz.inotes2.typeconverters.CategoryTypeConverter;
import com.example.pokrz.inotes2.typeconverters.DateTypeConverters;
import com.example.pokrz.inotes2.typeconverters.LocationTypeConverter;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class, Category.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverters.class, LocationTypeConverter.class, CategoryTypeConverter.class})
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase INSTANCE;

    public abstract NoteDao noteDao();
    public abstract CategoryDao categoryDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(() ->
                                    getInstance(context).categoryDao().insertAll(Category.populateData()));
                        }})
                    .build();
        }
        return INSTANCE;
    }

}