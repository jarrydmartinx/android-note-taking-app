package com.example.jarryd.assignment_1;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    /* Declare widget objects */
    EditText noteEditText;
    myImageView noteImageView;
    EditText titleEditText;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

    /* Get Intent Message */
        Intent editNoteIntent = getIntent();
        String key = "note_selected";
        note = (Note) editNoteIntent.getExtras().getSerializable(key);
        //or should you load the note from file?

    /* Instantiate widgets*/
        noteEditText = (EditText) findViewById(R.id.noteEditText);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        noteImageView = (myImageView) findViewById(R.id.noteImageView);

        noteEditText.setText(note.note_text);
        //here you're reloading the image afresh, that's fine
        noteImageView.setBitmapViaBackgroundTaskFromResource(R.drawable.john);

        int deviceAPIversion = android.os.Build.VERSION.SDK_INT;
        if (deviceAPIversion >= Build.VERSION_CODES.LOLLIPOP) {
            noteEditText.setShowSoftInputOnFocus(true);
        }
    }
    /*  Method to Add photos */

//    String photoPath;

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String photoFileName = "JPEG_" + timeStamp + "_";
//
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
//        return image;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }


    public void readToVoice(){
        //
    }

}
