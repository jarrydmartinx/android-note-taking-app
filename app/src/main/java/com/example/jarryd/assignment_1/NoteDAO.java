package com.example.jarryd.assignment_1;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by jarryd on 24/03/16.
 */

public interface NoteDAO {

        ArrayList<Note> getAllSavedNotes();

        Note loadNote(String note_id);
        void deleteNoteDataAndImage(Note note);
        void deleteMultiNoteDataAndImage(ArrayList<Note> noteArrayList);
        void saveNewNoteData(Note note);

        void updateNoteData(Note note);



}


