package com.example.jarryd.assignment_1;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * EditNoteActivity is the UI screen in the app that allows the user to Edit their Note,
 * add/change a photo, record a voice note and share notes
 */
public class EditNoteActivity extends AppCompatActivity {
    /**
     * The ShareActionProvider determines how data can be shared by the user with other apps
     */
    ShareActionProvider shareActionProvider;
    /**
     * The default Display of the hardware device being used
     */
    Display display;
    /**
     * Holds size data about the default Display
     */
    DisplayMetrics metrics = new DisplayMetrics();
    //Declare Widget Objects
    EditText noteEditText;
    MyImageView noteImageView;
    EditText titleEditText;
    FloatingActionButton fab;
    /**
     * The Context of EditNoteActivity (currently instantiated as the ApplicationContext,
     * (needed for certain methods)
     */
    private Context context; // the Context of this activity
    /**
     * The local resources associated with EditNoteActivity
     */
    private Resources res; // Its associated resources
    /**
     * The Note that is being edited in this instance of EditNoteActivity
     */
    private Note note;
    /**
     * The Data Access Object that implements interface methods for accessing stored Note Data
     */
    private NoteDAO noteDAO;
    /**
     * The Data Access Object that implements methods for accessing stored Images
     */
    private ImageDAO imageDAO;

    /**
     * Called whenever EditNoteActivity is created
     *
     * @param savedInstanceState: the saved state of the activity before creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        context = getApplicationContext();
        display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        res = getResources();
        noteDAO = new NoteDAOImplSQLite(context);
        imageDAO = new ImageDAOImpl(context);

        //Gets Intent Message
        Intent editNoteIntent = getIntent();
        String received_note_id = editNoteIntent.getStringExtra(context.getString(R.string.selected_note_id));
        //Loads note from Database
        note = noteDAO.loadNote(received_note_id);

        //Instantiates the widgets
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        noteEditText = (EditText) findViewById(R.id.noteEditText);
        noteImageView = (MyImageView) findViewById(R.id.noteImageView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Sets the content of the Widgets
        titleEditText.setText(note.note_title, TextView.BufferType.EDITABLE);
        noteEditText.setText(note.note_text, TextView.BufferType.EDITABLE);
        if (note.getImage_id() != null) {
            noteImageView.setImageResource(android.R.drawable.dialog_holo_light_frame);
            noteImageView.setBitmapViaBackgroundTask(context, note.image_id, metrics.widthPixels, res.getInteger(R.integer.IM_SCALE_FACTOR_EDIT));
        }

        //Sets up the onClickListener for the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * Defines callback method called when user clicks on Floating Action Button
             * @param view : the FAB
             */
            @Override
            public void onClick(View view) {
                dispatchImageCaptureIntent(note);
            }
        });
        //Sets up an input method manager and listeners to control showing/hiding of keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        TextViewListenerHelper textViewListenerHelper = new TextViewListenerHelperImpl(inputMethodManager);
        textViewListenerHelper.setListenersForEditText(noteEditText);
        textViewListenerHelper.setListenersForEditText(titleEditText);
    }


    /**
     * Inflates the Options menu, adds action menu items to action bar
     *
     * @param menu the menu to be inflated
     * @return boolean true upon completion
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        MenuItem item = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    /**
     * Sets the behaviour when user clicks on one of the menu items in the action bar
     *
     * @param item the menu item clicked
     * @return boolean true if action consumed, false if not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.delete) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage(R.string.delete_confirm_message)
                    .setTitle(R.string.delete_confirm_title)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        /**
                         * Defines callback method called when user clicks on the button
                         * @param dialogInterface the dialog box
                         * @param item_id the button pressed
                         */
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            noteDAO.deleteNoteDataAndImage(note);
                            Intent launchMainActivityIntent = new Intent(EditNoteActivity.this, MainActivity.class);
                            startActivity(launchMainActivityIntent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        /**
                         * Defines callback method called when user clicks on the button
                         * @param dialogInterface the dialog box
                         * @param item_id the button pressed
                         */
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            //Delete action cancelled by user
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
                        /**
                         * Defines callback method called when user clicks on the button
                         * @param dialogInterface the dialog box
                         * @param item_id the button pressed
                         */
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            note.note_text = noteEditText.getText().toString();
                            note.note_title = titleEditText.getText().toString();
                            noteDAO.updateNoteData(note);
                            dispatchNoteSavedIntent(note.getNote_id());
                        }
                    })
                    .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                        /**
                         * Defines callback method called when user clicks on the button
                         * @param dialogInterface the dialog box
                         * @param item_id the button pressed
                         */
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

    /**
     * Defines callback method invoked when a user keyEvent with this keyCode occurs
     *
     * @param keyCode  the code of the user initiated keyEvent
     * @param keyEvent the user initiated event when key is pressed
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage(R.string.save_or_discard_message)
                    .setPositiveButton(R.string.save_button_title, new DialogInterface.OnClickListener() {
                        /**
                         * Defines callback method called when user clicks on the button
                         * @param dialogInterface the dialog box
                         * @param item_id the button pressed
                         */
                        public void onClick(DialogInterface dialogInterface, int item_id) {
                            note.note_text = noteEditText.getText().toString();
                            note.note_title = titleEditText.getText().toString();
                            noteDAO.updateNoteData(note);
                            dispatchNoteSavedIntent(note.getNote_id());
                        }
                    })
                    .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                        /**
                         * Defines callback method called when user clicks on the button
                         * @param dialogInterface the dialog box
                         * @param item_id the button pressed
                         */
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
     * Dispatches Intent that a note has been saved from EditNoteActivity
     * to MainActivity
     *
     * @param note_id the id of the saved note
     */
    private void dispatchNoteSavedIntent(String note_id) {
        Intent noteSavedIntent = new Intent(EditNoteActivity.this, MainActivity.class);
        noteSavedIntent.putExtra(
                context.getString(R.string.selected_note_id),
                note_id);
        startActivity(noteSavedIntent);

    }

    /**
     * Dispatches a Recognizer Intent when the user initiates voice input to the Note text field
     */
    private void dispatchSpeechInputIntent() {
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.begin_speech));
        try {
            startActivityForResult(speechRecognizerIntent, res.getInteger(R.integer.REQUEST_VOICE_INPUT));
        } catch (ActivityNotFoundException a) {
            Snackbar speechSnackbar = Snackbar.make(noteEditText, "Speech Input is not available on your device", Snackbar.LENGTH_LONG);
            speechSnackbar.show();
        }
    }

    /**
     * Dispatches an Intent when the user shares the note via other apps
     *
     * @param note the note to be shared
     */
    private void dispatchShareIntent(Note note) {
        startActivity(Intent.createChooser(note.createShareNoteIntent(), "Share your note"));
    }

    /**
     * Dispatches an Intent when the user wants to add a photo to the Note
     *
     * @param note the note being edited
     */
    private void dispatchImageCaptureIntent(Note note) {
        startActivityForResult(imageDAO.createImageCaptureIntent(note), res.getInteger(R.integer.REQUEST_IMAGE_CAPTURE));
    }

    /**
     * Receives the results of various Intent requests (e.g. Voice Input, Image Capture)
     *
     * @param requestCode the request code sent with the original Intent from EditNoteActivity
     * @param resultCode  code returned with the result
     * @param data        the data returned with the result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Receives the transcribed string from the Voice Input
        if (requestCode == res.getInteger(R.integer.REQUEST_VOICE_INPUT) && resultCode == RESULT_OK && data != null) {
            ArrayList<String> activityResult =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            noteEditText.append(activityResult.get(0));
            //Receives the saved photo from the Hardware Camera
        } else if (requestCode == res.getInteger(R.integer.REQUEST_IMAGE_CAPTURE) && resultCode == RESULT_OK) {
            noteImageView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            display.getMetrics(metrics);
            noteImageView.setBitmapViaBackgroundTask(context, note.getImage_id(), metrics.widthPixels, res.getInteger(R.integer.IM_SCALE_FACTOR_EDIT));

        }

    }


}





