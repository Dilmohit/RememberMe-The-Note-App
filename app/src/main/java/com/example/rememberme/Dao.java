package com.example.rememberme;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Adding annotation
// to our Dao class
@androidx.room.Dao
public interface Dao {

    // below method is use to
    // add data to database.
    @Insert
    void insert(NotesModal model);

    // below method is use to update
    // the data in our database.
    @Update
    void update(NotesModal model);

    // below line is use to delete a
    // specific note in our database.
    @Delete
    void delete(NotesModal model);

    // on below line we are making query to
    // delete all Notes from our database.
    @Query("DELETE FROM notes_table")
    void deleteAllNotes();

    // below line is to read all the Notes from our database.
    // in this we are ordering our Notes in ascending order
    // with our Notes name.
    @Query("SELECT * FROM notes_table ORDER BY addNotes ASC")
    LiveData<List<NotesModal>> getAllNotes();
}


