package com.example.jarryd.assignment_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    TextView noteText;
    ImageView noteImage;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_note);
//        Note.load()
//    }
//
//    /* Instantiate widgets*/
//        noteText = (TextView) findViewById(R.id.note_text);
//        noteImage = (ImageView) findViewById(R.id.note_image);
//
//     /* Get Intent Message */
//        Intent editNoteIntent = getIntent();
//        String message = editNoteIntent.getStringExtra(this.getString(R.string.edit_note_intent_message));
//        textDynamic.setText(message);
//
//
//
//    /*  Method to Add photos */
//
//    String photoPath;
//
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
//
//
//
//
//    public void readToVoice(){
//        //
//    }

}
