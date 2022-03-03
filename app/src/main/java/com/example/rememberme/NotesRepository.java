package com.example.rememberme;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotesRepository {

    // below line is the create a variable
    // for dao and list for all notes.
    private Dao dao;
    private LiveData<List<NotesModal>> allNotes;

    // creating a constructor for our variables
    // and passing the variables to it.
    public NotesRepository(Application application) {
        NotesDatabase database = NotesDatabase.getInstance(application);
        dao = database.Dao();
        allNotes = dao.getAllNotes();
    }

    // creating a method to insert the data to our database.
    public void insert(NotesModal model) {
        new InsertNotesAsyncTask(dao).execute(model);
    }

    // creating a method to update data in database.
    public void update(NotesModal model) {
        new UpdateNotesAsyncTask(dao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(NotesModal model) {
        new DeleteNotesAsyncTask(dao).execute(model);
    }

    // below is the method to delete all the notes.
    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(dao).execute();
    }

    // below method is to read all the notes.
    public LiveData<List<NotesModal>> getAllNotes() {
        return allNotes;
    }

    // we are creating a async task method to insert new notes.
    private static class InsertNotesAsyncTask extends AsyncTask<NotesModal, Void, Void> {
        private Dao dao;

        private InsertNotesAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NotesModal... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our notes.
    private static class UpdateNotesAsyncTask extends AsyncTask<NotesModal, Void, Void> {
        private Dao dao;

        private UpdateNotesAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NotesModal... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete notes.
    private static class DeleteNotesAsyncTask extends AsyncTask<NotesModal, Void, Void> {
        private Dao dao;

        private DeleteNotesAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NotesModal... models) {
            // below line is use to delete
            // our notes modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all notes.
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllNotesAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all notes.
            dao.deleteAllNotes();
            return null;
        }
    }
}

