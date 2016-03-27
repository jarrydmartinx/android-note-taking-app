package com.example.jarryd.assignment_1;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.ShareActionProvider;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {
//    private final Context context = getApplicationContext();

    /* Declare widget objects */
    Context context;
    Note note;
    NoteDAO noteDAO;
    ImageDAO imageDAO;
    EditText noteEditText;
    MyImageView noteImageView;
    EditText titleEditText;
    ShareActionProvider shareActionProvider;
    int layoutWidth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        context = getApplicationContext();
        noteDAO = new NoteDAOImplSQLite(context);
        imageDAO = new ImageDAOImpl(context);

    /* Get Intent Message */
        Intent editNoteIntent = getIntent();
        String received_note_id = editNoteIntent.getStringExtra(context.getString(R.string.selected_note_id));


        //Load note from Database
        note = noteDAO.loadNote(received_note_id);


    /* Instantiate widgets*/
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        noteEditText = (EditText) findViewById(R.id.noteEditText);
        noteImageView = (MyImageView) findViewById(R.id.noteImageView);

        titleEditText.setText(note.note_title, TextView.BufferType.EDITABLE);
        noteEditText.setText(note.note_text, TextView.BufferType.EDITABLE);
        //here you're reloading the image afresh, that's fine
        if (note.getImage_id() != null) {
            noteImageView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            noteImageView.setBitmapViaBackgroundTask(context, note.image_id);
            System.out.println("########ONCREATE EDITNOTEACTIVITY###########  width is :    " +noteImageView.getMeasuredWidth()+
                    "   height is :    " +noteImageView.getMeasuredHeight());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchImageCaptureIntent(note);
            }
        });

        //Set up an input method manager and listeners to control showing/hiding of keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        TextViewListenerHelper textViewListenerHelper = new TextViewListenerHelperImpl(inputMethodManager);
        textViewListenerHelper.setListenersForEditText(noteEditText);
        textViewListenerHelper.setListenersForEditText(titleEditText);

    }


    private void dispatchNoteSavedIntent(String note_id) {
        Intent noteSavedIntent = new Intent(EditNoteActivity.this, MainActivity.class);
        noteSavedIntent.putExtra(
                context.getString(R.string.selected_note_id),
                note_id);
        startActivity(noteSavedIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        MenuItem item = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
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
        } else if (item_id == R.id.save) {
            note.note_text = noteEditText.getText().toString();
            note.note_title = titleEditText.getText().toString();

            noteDAO.updateNoteData(note);
            Snackbar saveSnackbar = Snackbar.make(noteEditText, "Note Saved!", Snackbar.LENGTH_LONG);
            saveSnackbar.show();
            return true;
        } else if (item_id == R.id.voice) {
            dispatchSpeechInputIntent();
        } else if (item_id == R.id.share) {
            dispatchShareIntent(note);
            return true;
        } else if (item_id == android.R.id.home) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage(R.string.save_or_discard_message)
                    .setPositiveButton(R.string.save_button_title, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            note.note_text = noteEditText.getText().toString();
                            note.note_title = titleEditText.getText().toString();
                            noteDAO.updateNoteData(note);
                            dispatchNoteSavedIntent(note.getNote_id());
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage(R.string.save_or_discard_message)
                    .setPositiveButton(R.string.save_button_title, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            note.note_text = noteEditText.getText().toString();
                            note.note_title = titleEditText.getText().toString();
                            noteDAO.updateNoteData(note);
                            dispatchNoteSavedIntent(note.getNote_id());
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

    /**
     * Showing google speech input dialog
     */

    private void dispatchSpeechInputIntent() {
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.begin_speech));
        try {
            startActivityForResult(speechRecognizerIntent, getResources().getInteger(R.integer.REQUEST_VOICE_INPUT));
        } catch (ActivityNotFoundException a) {
            Snackbar speechSnackbar = Snackbar.make(noteEditText, "Speech Input is not available on your device", Snackbar.LENGTH_LONG);
            speechSnackbar.show();
        }
    }

    private void dispatchShareIntent(Note note) {
        startActivity(Intent.createChooser(note.createShareNoteIntent(), "Share your note"));
    }

    private void dispatchImageCaptureIntent(Note note){
        startActivityForResult(imageDAO.createImageCaptureIntent(note), getResources().getInteger(R.integer.REQUEST_IMAGE_CAPTURE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * Receives the transcribed string from the Voice Input
         * */
        if (requestCode == getResources().getInteger(R.integer.REQUEST_VOICE_INPUT) && resultCode == RESULT_OK && data != null) {
            ArrayList<String> activityResult =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            noteEditText.append(activityResult.get(0));
            /**
             * Receiving the saved photo from the Hardware Camera
             * */
        } else if (requestCode == getResources().getInteger(R.integer.REQUEST_IMAGE_CAPTURE) && resultCode == RESULT_OK) {
            System.out.println("imageID is " + note.getImage_id());
            noteImageView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            noteImageView.setBitmapViaBackgroundTask(context, note.getImage_id());
            System.out.println("###########ON RECEIVE FROM CAMERA########  width is :    " + noteImageView.getMeasuredWidth() +
                    "   height is :    " + noteImageView.getMeasuredHeight());
        }

    }



}





