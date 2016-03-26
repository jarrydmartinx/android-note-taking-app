package com.example.jarryd.assignment_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class EditNoteActivity extends AppCompatActivity {
//    private final Context context = getApplicationContext();

    /* Declare widget objects */
    Context context;
    Note note;
    NoteDAO noteDAO;
    EditText noteEditText;
    MyImageView noteImageView;
    EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        context = getApplicationContext();
        noteDAO = new NoteDAOImplSQLite(context);

    /* Get Intent Message */
        Intent editNoteIntent = getIntent();
        String received_note_id = editNoteIntent.getStringExtra(context.getString(R.string.selected_note_id));


        //Load note from Database
        note = MainActivity.noteDAO.loadNote(received_note_id);


    /* Instantiate widgets*/
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        noteEditText = (EditText) findViewById(R.id.noteEditText);
        noteImageView = (MyImageView) findViewById(R.id.noteImageView);

        titleEditText.setText(note.note_title, TextView.BufferType.EDITABLE);
        noteEditText.setText(note.note_text, TextView.BufferType.EDITABLE);
        //here you're reloading the image afresh, that's fine
        if (note.image_id != null) {
            noteImageView.setBitmapViaBackgroundTask(getApplicationContext(), note.image_id);
        }

        noteEditText.clearFocus();
        int deviceAPIversion = android.os.Build.VERSION.SDK_INT;
        if (deviceAPIversion >= Build.VERSION_CODES.LOLLIPOP) {
            noteEditText.setShowSoftInputOnFocus(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePhotoIntent(note.note_id);
            }
        });



    }

    private void dispatchTakePhotoIntent(String note_id) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            String image_filename = "JPEG_IMAGE_" + note_id + "_";
            File imageFile = new File(getFilesDir(), image_filename);

            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    /*  Method to Add photos */

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchNoteSavedIntent(String note_id) {
        Intent noteSavedIntent = new Intent(EditNoteActivity.this, MainActivity.class);
        noteSavedIntent.putExtra(
                context.getString(R.string.selected_note_id),
                note_id);
        startActivity(noteSavedIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int item_id = item.getItemId();

        //
        if (item_id == R.id.delete) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage(R.string.delete_confirm_message)
                    .setTitle(R.string.delete_confirm_title)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            noteDAO.deleteNoteDataAndImage(note);
                            System.out.println("#######DELETE ONE EDIT############ note_id: " + note.note_id + ", note_title: " + note.note_title + ", ______________");
                            Intent launchMainActivityIntent = new Intent(EditNoteActivity.this, MainActivity.class);
                            startActivity(launchMainActivityIntent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            //Delete Action Cancelled By the User
                        }
                    });

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();

        }
        if (item_id == R.id.save) {
            note.note_text = noteEditText.getText().toString();
            note.note_title = titleEditText.getText().toString();
            noteDAO.updateNoteData(note);
            Snackbar saveSnackbar = Snackbar.make(noteEditText, "Note Saved!", Snackbar.LENGTH_LONG);
            saveSnackbar.show();
            return true;
        }

        if (item_id == R.id.share){
            //share function note implemented
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage(R.string.save_or_discard_message)
                    .setPositiveButton(R.string.save_button_title, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            noteDAO.updateNoteData(note);
                            dispatchNoteSavedIntent(note.getNote_id());
                            System.out.println("DID THIS EVEN HAPPENN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    })
                    .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            Intent launchMainActivityIntent = new Intent(EditNoteActivity.this, MainActivity.class);
                            startActivity(launchMainActivityIntent);
                        }
                    });

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    public void readToVoice(){


    }

}
