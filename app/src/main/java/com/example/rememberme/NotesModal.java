package com.example.rememberme;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// below line is for setting table name.
@Entity(tableName = "notes_table")
public class NotesModal {

    // below line is to auto increment
    // id for each note.
    @PrimaryKey(autoGenerate = true)

    // variable for our id.
    private int id;

    // below line is a variable
    // for note name.
    private String addNotes;

    // below line we are creating constructor class.
    // inside constructor class we are not passing
    // our id because it is incrementing automatically
    public NotesModal(String notesName) {
        this.addNotes = notesName;
    }

    public NotesModal() {
    }

    // on below line we are creating
    // getter and setter methods.
    public String getAddNotes() {
        return addNotes;
    }

    public void setAddNotes(String addNotes) {
        this.addNotes = addNotes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


