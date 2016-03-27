package com.example.jarryd.assignment_1;

//Remember to remove tags okay! the second tag (in keys.xml and in NoteAdapter
//http://developer.android.com/guide/topics/ui/menus.html#FloatingContextMenu

import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public Context context = this;
    public ArrayList<Note> noteArray;
    public static NoteDAO noteDAO;
    private GridView noteGridView;
    private NoteGridAdapter noteGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets activity layout and initializes toolbar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializes the floating action button (to create new note)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchLaunchEditNewNoteIntent();
            }
        });

        //Initializes the DAO that accesses the note data in SQLite database
        noteDAO = new NoteDAOImplSQLite(context);

        /* Loads all Notes in DB into an ArrayList to back the NoteAdapter */
        this.noteArray = noteDAO.getAllSavedNotes();
        for (Note aNote : noteArray) {
            System.out.println(aNote.note_title);
        }

        /* Listener for responding to user clicks on an individual note preview, launches EditNoteActivity */
        AdapterView.OnItemClickListener notePreviewClickedListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                dispatchLaunchEditNoteIntent(position);
            }
        };

        /* Initializes GridView that displays note previews*/
        noteGridView = (GridView) findViewById(R.id.noteGridView);
        noteGridView.invalidateViews();
        /* Instantiate NoteAdapter that connects the Note ArrayList with the GridView */
        noteGridAdapter = new NoteGridAdapter(this, R.layout.note_preview, noteArray);
        noteGridView.setAdapter(noteGridAdapter);

        //Set Listeners for OnClick and OnLongClick Item selection (MultiChoice Mode begins on long click)
        noteGridView.setOnItemClickListener(notePreviewClickedListener);
        noteGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        noteGridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_main_context, menu);
                return true;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                //Updates the list of checked notes maintained by the Adapter
                noteGridAdapter.updateCheckedNoteList(noteGridView.getCheckedItemPositions());
                //Regenerates note Previews so that checked items are highlighted
                noteGridView.invalidateViews();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                int item_id = item.getItemId();

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setMessage(R.string.delete_confirm_message)
                        .setTitle(R.string.delete_confirm_title)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int item_id) {
                                noteDAO.deleteMultiNoteDataAndImage(noteGridAdapter.checkedNotes);
                                noteArray.removeAll(noteGridAdapter.checkedNotes);
                                noteGridAdapter.notifyDataSetChanged();
                                noteGridView.invalidateViews();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int item_id) {
                                //Delete Action Cancelled By the User
                            }
                        });
                if (item_id == R.id.delete) {
                    noteGridAdapter.updateCheckedNoteList(noteGridView.getCheckedItemPositions());
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                } else if (item_id == R.id.share) {
                    if (noteGridView.getCheckedItemCount() == 1) {
                        dispatchShareIntent(noteGridAdapter.checkedNotes.get(0));
                    } else {
                        Snackbar cantShareMultipleNotes = Snackbar.make(noteGridView, R.string.cant_share_multiple, Snackbar.LENGTH_LONG);
                        cantShareMultipleNotes.show();
                    }
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int item_id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private void dispatchLaunchEditNoteIntent(int position) {
        Intent launchEditNoteIntent = new Intent(MainActivity.this, EditNoteActivity.class);
        launchEditNoteIntent.putExtra(
                context.getString(R.string.selected_note_id),
                noteArray.get(position).getNote_id()
        );
        startActivity(launchEditNoteIntent);
    }

    private void dispatchLaunchEditNewNoteIntent() {
        Note newNote = new Note();
        noteDAO.saveNewNoteData(newNote);
        Intent launchEditNoteIntent = new Intent(MainActivity.this, EditNoteActivity.class);
        launchEditNoteIntent.putExtra(context.getString(R.string.selected_note_id),newNote.getNote_id());
        startActivity(launchEditNoteIntent);
    }

    private void dispatchShareIntent(Note note){
        startActivity(Intent.createChooser(note.createShareNoteIntent(), "Share your note"));
    }


}
