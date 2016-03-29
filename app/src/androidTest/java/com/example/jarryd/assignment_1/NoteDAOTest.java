package com.example.jarryd.assignment_1;

import junit.framework.TestCase;

/**
 * Created by jarryd on 29/03/16.
 */
public class NoteDAOTest extends TestCase {
        protected String noteId, imageId, noteTitle, noteText, newNoteText;
        protected Note aNote;
        MainActivity mainActivity = new MainActivity();
        protected NoteDAO noteDAO = new NoteDAOImplSQLite(mainActivity);
        // assigning the values
        protected void setUp(){
            noteId="noteid";
            imageId="imageid";
            noteTitle="this is the title";
            noteText="noteText this is the note text body";
            newNoteText="this note text has changed";
        }

        // test method to add two values
        public void testDAO(){
            Note aNote = new Note(noteId, imageId,noteTitle,noteText);
            noteDAO.saveNewNoteData(aNote);
            Note loadedNote = noteDAO.loadNote(aNote.getNote_id());

            loadedNote.setNote_text(newNoteText);
            noteDAO.updateNoteData(loadedNote);

            assertTrue(loadedNote.getNote_id().equals(noteId));
            assertTrue(loadedNote.getImage_id().equals(imageId));
            assertTrue(loadedNote.getNote_title().equals(noteTitle));
            assertTrue(loadedNote.getNote_text().equals(noteText));

            Note reloadedNote = noteDAO.loadNote(loadedNote.note_id);
            assertTrue(reloadedNote.getNote_id().equals(noteId));
            assertTrue(reloadedNote.getImage_id().equals(imageId));
            assertTrue(reloadedNote.getNote_title().equals(noteTitle));
            assertTrue(reloadedNote.getNote_text().equals(noteText));


        }
    }

