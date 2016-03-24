package com.example.jarryd.assignment_1;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jarryd on 24/03/16.
 */
public interface NoteDAOInterface {

        public ArrayList<Note> getAllSavedNotes(Context context);

        public void createNote();
        public void saveNote(Note note);
        public void deleteNote(Note note);

        public Note getNotebyId(String note_id);
        public String getImageId(String note_id);

}


