package com.example.jarryd.assignment_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

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
            System.out.println("#######SAVE ONE EDITACTIVITY############ note_id: " + note.note_id + ", note_title: " + note.note_title + ", ______________");
            Snackbar saveSnackbar = Snackbar.make(noteEditText, "Note Saved!", Snackbar.LENGTH_LONG);
            saveSnackbar.show();
            return true;
        }

        if (item_id == R.id.share){
            //share function note implemented
        }

        return super.onOptionsItemSelected(item);
    }

    public void readToVoice(){
        //
    }

}
