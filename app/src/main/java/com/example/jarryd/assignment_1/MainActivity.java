package com.example.jarryd.assignment_1;

//http://developer.android.com/guide/topics/ui/menus.html#FloatingContextMenu

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * The main Activity screen shown when the app is first launched
 * Shows a layout with previews of all the existing notes,
 * allows selection for editing & single/multiple deletion
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The context (MainActivity itself)
     */
    private Context context = this;
    /**
     * The Data Access Object that implements interface methods for accessing stored Note Data
     */
    private NoteDAO noteDAO;
    /**
     * The data model, an ArrayList of all the existing notes as Note objects,
     * which backs the NoteGridAdapter
     */
    private ArrayList<Note> noteArray;
    /**
     * The main UI element, a grid of note previews
     */
    private GridView noteGridView;
    /**
     * The custom adapter for loading note preview Views into the GridView
     */
    private NoteGridAdapter noteGridAdapter;

    /**
     * Called whenever MainActivity is created
     *
     * @param savedInstanceState: the saved state of the activity before creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets activity layout and initializes toolbar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Gets the display dimensions of the device used
        Display display = getWindowManager().getDefaultDisplay();

        //Initializes the floating action button (to create new note)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * Defines callback method called when user clicks on Floating Action Button
             * @param view : the FAB
             */
            @Override
            public void onClick(View view) {
                dispatchLaunchEditNewNoteIntent();
            }
        });

        //Initializes the DAO that accesses the note data in SQLite database
        noteDAO = new NoteDAOImplSQLite(context);

        // Loads all Notes in DB into an ArrayList to back the NoteAdapter
        this.noteArray = noteDAO.getAllSavedNotes();

        // Listener for responding to user clicks on an individual note preview, launches EditNoteActivity */
        AdapterView.OnItemClickListener notePreviewClickedListener = new AdapterView.OnItemClickListener() {
            /**
             * Defines callback method called when user clicks on one the note previews in the grid
             * @param parent the parent view of the adapter
             * @param view the clicked View
             * @param position the position of the clicked View in the Adapter
             * @param id the id of the clicked View
             */
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                dispatchLaunchEditNoteIntent(position);
            }
        };

        /* Initializes GridView that displays note previews*/
        noteGridView = (GridView) findViewById(R.id.noteGridView);
        noteGridView.invalidateViews();
        /* Instantiate NoteAdapter that connects the Note ArrayList with the GridView */
        noteGridAdapter = new NoteGridAdapter(context, R.layout.note_preview, noteArray, display);
        noteGridView.setAdapter(noteGridAdapter);

        //Set Listeners for OnClick and OnLongClick Item selection (MultiChoice Mode begins on long click)
        noteGridView.setOnItemClickListener(notePreviewClickedListener);
        noteGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        noteGridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            /**
             * Called when the user long-clicks on an item in the Grid, inflates context menu
             * with multi-choice delete option
             * @param mode action mode
             * @param menu the context menu to be inflated
             * @return
             */
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_main_context, menu);
                return true;
            }

            /**
             * Called when a user checks/unchecks an item in multi-choice mode
             * updates a list of checked notes stored in the Adapter and highlights checked notes
             * @param mode action mode
             * @param position item position in the adapter
             * @param id id of checked/unchecked item
             * @param checked true if checked
             */
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                //Updates the list of checked notes maintained by the Adapter
                noteGridAdapter.updateCheckedNoteList(noteGridView.getCheckedItemPositions());
                //Regenerates note Previews so that checked items are highlighted
                noteGridView.invalidateViews();
            }

            @Override
            /**
             * Called when user clicks on a menu item in the action bar
             * @param mode action mode
             * @param item the clicked menu item
             * @return true if action consumed, false if not
             */
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int item_id = item.getItemId();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setMessage(R.string.delete_confirm_message)
                        .setTitle(R.string.delete_confirm_title)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            /**
                             * Defines callback method called when user clicks on the button
                             * @param dialogInterface the dialog box
                             * @param item_id the button pressed
                             */
                            public void onClick(DialogInterface dialogInterface, int item_id) {
                                noteDAO.deleteMultiNoteDataAndImage(noteGridAdapter.checkedNotes);
                                noteArray.removeAll(noteGridAdapter.checkedNotes);
                                noteGridAdapter.notifyDataSetChanged();
                                noteGridView.invalidateViews();
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

                if (item_id == R.id.delete) {
                    noteGridAdapter.updateCheckedNoteList(noteGridView.getCheckedItemPositions());
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                    mode.finish();
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
                return false;
            }
        });
    }

    /**
     * Inflates the Options menu, adds action menu items to action bar
     *
     * @param menu the menu to be inflated
     * @return boolean true upon completion
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Inflates the Context menu, adds action menu items to action bar
     *
     * @param menu the menu to be inflated
     * @return boolean true upon completion
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return;
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
        if (item_id == R.id.display_note_count) {
            int noteCount = noteArray.size();
            Snackbar noteCountSnackbar = Snackbar.make(noteGridView, "You have " + noteCount + " saved notes!", Snackbar.LENGTH_LONG);
            noteCountSnackbar.show();
            return true;
        }
        return false;
    }


    /**
     * Dispatches Intent that launches EditActivity for editing an existing Note
     *
     * @param position the position in the Adapter of the note to be edited
     */
    private void dispatchLaunchEditNoteIntent(int position) {
        Intent launchEditNoteIntent = new Intent(MainActivity.this, EditNoteActivity.class);
        launchEditNoteIntent.putExtra(
                context.getString(R.string.selected_note_id),
                noteArray.get(position).getNote_id()
        );
        startActivity(launchEditNoteIntent);
    }

    /**
     * Dispatches Intent that launches EditActivity for editing a new Note
     */
    private void dispatchLaunchEditNewNoteIntent() {
        Note newNote = new Note();
        noteDAO.saveNewNoteData(newNote);
        Intent launchEditNoteIntent = new Intent(MainActivity.this, EditNoteActivity.class);
        launchEditNoteIntent.putExtra(context.getString(R.string.selected_note_id), newNote.getNote_id());
        startActivity(launchEditNoteIntent);
    }

    /**
     * Dispatches an Intent when the user shares the note via other apps
     *
     * @param note the note to be shared
     */
    private void dispatchShareIntent(Note note) {
        startActivity(Intent.createChooser(note.createShareNoteIntent(), "Share your note"));
    }


}
