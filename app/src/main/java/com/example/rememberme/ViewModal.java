package com.example.rememberme;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModal extends AndroidViewModel {

    // creating a new variable for notes repository.
    private NotesRepository repository;

    // below line is to create a variable for live
    // data where all the notes are present.
    private LiveData<List<NotesModal>> allNotes;

    // constructor for our view modal.
    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new NotesRepository(application);
        allNotes = repository.getAllNotes();
    }

    // below method is use to insert the data to our repository.
    public void insert(NotesModal model) {
        repository.insert(model);
    }

    // below line is to update data in our repository.
    public void update(NotesModal model) {
        repository.update(model);
    }

    // below line is to delete the data in our repository.
    public void delete(NotesModal model) {
        repository.delete(model);
    }

    // below method is to delete all the notes in our list.
    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    // below method is to get all the notes in our list.
    public LiveData<List<NotesModal>> getAllNotes() {
        return allNotes;
    }
}
