package com.example.rememberme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTES_REQUEST = 1;
    public static final int EDIT_NOTES_REQUEST = 2;
    private ViewModal viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our variable for our recycler view and fab.
        // creating a variables for our recycler view.
        RecyclerView notesRV = findViewById(R.id.idRVNotes);
        FloatingActionButton fab = findViewById(R.id.idFABAdd);

        // adding on click listener for floating action button.
        fab.setOnClickListener(v -> {
            // starting a new activity for adding a new notes
            // and passing a constant value in it.
            Intent intent = new Intent(MainActivity.this, NewNotesActivity.class);
            startActivityForResult(intent, ADD_NOTES_REQUEST);
        });


        // setting layout manager to our adapter class.
        notesRV.setLayoutManager(new GridLayoutManager(this,2));
        notesRV.setHasFixedSize(true);

        // initializing adapter for recycler view.
        final NotesRVAdapter adapter = new NotesRVAdapter();

        // setting adapter class for recycler view.
        notesRV.setAdapter(adapter);

        // passing a data from view modal.
        viewmodal = ViewModelProviders.of(this).get(ViewModal.class);

        // below line is use to get all the notes from view modal.
        viewmodal.getAllNotes().observe(this, new Observer<List<NotesModal>>() {
            @Override
            public void onChanged(List<NotesModal> models) {
                // when the data is changed in our models we are
                // adding that list to our adapter class.
                adapter.submitList(models);
            }
        });

        // below method is use to add swipe to delete method for item of recycler view.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // on recycler view item swiped then we are deleting the item of our recycler view.
                viewmodal.delete(adapter.getNotesAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).
                // below line is use to attach this to recycler view.
                        attachToRecyclerView(notesRV);
        // below line is use to set item click listener for our item of recycler view.
        adapter.setOnItemClickListener(new NotesRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NotesModal model) {
                // after clicking on item of recycler view
                // we are opening a new activity and passing
                // a data to our activity.
                Intent intent = new Intent(MainActivity.this, NewNotesActivity.class);
                intent.putExtra(NewNotesActivity.EXTRA_ID, model.getId());
                intent.putExtra(NewNotesActivity.EXTRA_NOTE, model.getAddNotes());

                // below line is to start a new activity and
                // adding a edit notes constant.
                startActivityForResult(intent, EDIT_NOTES_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTES_REQUEST && resultCode == RESULT_OK) {
            String notesName = data.getStringExtra(NewNotesActivity.EXTRA_NOTE);
            NotesModal model = new NotesModal(notesName);
            viewmodal.insert(model);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTES_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(NewNotesActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String notesName = data.getStringExtra(NewNotesActivity.EXTRA_NOTE);
            NotesModal model = new NotesModal(notesName);
            model.setId(id);
            viewmodal.update(model);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}