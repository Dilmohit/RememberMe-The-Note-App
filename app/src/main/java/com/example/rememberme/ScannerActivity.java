package com.example.rememberme;

import static android.Manifest.permission.CAMERA;
import static com.example.rememberme.MainActivity.ADD_NOTES_REQUEST;
import static com.example.rememberme.MainActivity.EDIT_NOTES_REQUEST;
import static com.example.rememberme.NewNotesActivity.EXTRA_ID;
import static com.example.rememberme.NewNotesActivity.EXTRA_NOTE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class ScannerActivity extends AppCompatActivity {

    private ImageView captureIV;
    private TextView resultTV;
    private FloatingActionButton snapBTN, detectBTN;
    private Bitmap imageBitmap;
    private Button button;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        captureIV = findViewById(R.id.idIVCaptureImage);
        resultTV = findViewById(R.id.idIvDetectedText);
        snapBTN = findViewById(R.id.idFABSnap);
        detectBTN = findViewById(R.id.idFABDetect);
        button = findViewById(R.id.savetextinnotebtn);

        detectBTN.setOnClickListener(v -> detectText());

        snapBTN.setOnClickListener(v -> {

            if (checkPermission()) {
                captureImage();
            } else {
                requestPermission();
            }

        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notesFromImage = resultTV.getText().toString();
                Intent intent = new Intent(getApplicationContext(), NewNotesActivity.class);
                intent.putExtra(EXTRA_NOTE,notesFromImage);
                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    // in below line we are passing our id.
                    intent.putExtra(EXTRA_ID, id);
                }
          //      startActivity(intent);
                startActivityForResult(intent, EDIT_NOTES_REQUEST);

            }
        });

    }

    private boolean checkPermission() {
        int camerpermission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return camerpermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_CODE);
    }

    private void captureImage() {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraPermision = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraPermision) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
                captureImage();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            captureIV.setImageBitmap(imageBitmap);
        }

    }

        private void detectText () {
            InputImage image = InputImage.fromBitmap(imageBitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text text) {
                    StringBuilder result = new StringBuilder();
                    for (Text.TextBlock block : text.getTextBlocks()) {
                        String blockText = block.getText();
                        Point[] blockCornerPoint = block.getCornerPoints();
                        Rect blockFrame = block.getBoundingBox();
                        for (Text.Line line : block.getLines()) {
                            String lineText = line.getText();
                            Point[] lineCornerPoint = line.getCornerPoints();
                            Rect linREct = line.getBoundingBox();
                            for (Text.Element element : line.getElements()) {
                                String elementText = element.getText();
                                result.append(elementText);
                            }

                            resultTV.setText(blockText);
                        }
                    }
                }
            }).addOnFailureListener(e -> Toast.makeText(ScannerActivity.this, "Fail To Detect Text" + e.getMessage(), Toast.LENGTH_SHORT).show());


        }


}