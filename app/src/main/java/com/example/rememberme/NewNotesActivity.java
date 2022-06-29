package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewNotesActivity extends AppCompatActivity {

    // creating a variables for our button and edittext.
    private EditText notesEdttxt;

    // creating a constant string variable for our notes
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_NOTE = "EXTRA_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);

        // initializing our variables for each view.
        notesEdttxt = findViewById(R.id.idEdtNoteName);
        TextView notesBtn = findViewById(R.id.idBtnSaveNote);
        ImageView imgback = findViewById(R.id.IVbackfromNewActivity);

        // below line is to get intent as we
        // are getting data via an intent.
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // if we get id for our data then we are
            // setting values to our edit text fields.
            notesEdttxt.setText(intent.getStringExtra(EXTRA_NOTE));
        }

        // adding on click listener for our save button.
        notesBtn.setOnClickListener(v -> {
            // getting text value from edittext and validating if
            // the text fields are empty or not.
            String notesName = notesEdttxt.getText().toString();
            if (notesName.isEmpty() ) {
                Toast.makeText(NewNotesActivity.this, "Please enter something to save", Toast.LENGTH_SHORT).show();
            }
            // calling a method to save our note.
            saveNotes(notesName);
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void saveNotes(String notesName) {
        // inside this method we are passing
        // all the data via an intent.
        Intent data = new Intent();

        // in below line we are passing all our notes detail.
        data.putExtra(EXTRA_NOTE, notesName);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            // in below line we are passing our id.
            data.putExtra(EXTRA_ID, id);
        }

        // at last we are setting result as data.
        setResult(RESULT_OK, data);

        // displaying a toast message after adding the data
        Toast.makeText(this, "Note has been saved ", Toast.LENGTH_SHORT).show();
    }
}
