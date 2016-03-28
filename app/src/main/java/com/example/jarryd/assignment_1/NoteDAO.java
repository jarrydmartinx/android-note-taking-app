package com.example.jarryd.assignment_1;

import java.util.ArrayList;


/**
 * Created by jarryd on 24/03/16.
 * Data Access Object Interface
 * Defines methods for accessing notes in persistent storage
 */

public interface NoteDAO {
    /**
     * Loads all stored notes as Note objects into an ArrayList
     *
     * @return ArrayList of Note objects
     */
    ArrayList<Note> getAllSavedNotes();

    /**
     * Loads an individual note from persistent storage
     *
     * @param note_id the id of the note to be loaded
     * @return the loaded Note object
     */
    Note loadNote(String note_id);

    /**
     * Deletes the passed Note from persistent storage, along with its image (if any)
     *
     * @param note the note to be deleted
     */
    void deleteNoteDataAndImage(Note note);

    /**
     * Deletes all the Notes in the passed List from persistent storage,
     * along with their images (if any)
     *
     * @param noteArrayList the Note objects to be deleted
     */
    void deleteMultiNoteDataAndImage(ArrayList<Note> noteArrayList);

    /**
     * Saves the data for a newly created note to persistent storage
     *
     * @param note
     */
    void saveNewNoteData(Note note);

    /**
     * Updates the data in storage for a particular note on user Save action
     *
     * @param note
     */
    void updateNoteData(Note note);
}


