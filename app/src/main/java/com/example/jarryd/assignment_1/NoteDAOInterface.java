package com.example.jarryd.assignment_1;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jarryd on 24/03/16.
 */
public interface NoteDAOInterface {

        ArrayList<Note> getAllSavedNotes(Context context);

        Note loadNote(String note_id);
        void deleteNoteData(Note note);
        void saveNewNoteData(Note note);
        void updateNoteData(Note note);

       // Note readInNote(String note_id);


}


