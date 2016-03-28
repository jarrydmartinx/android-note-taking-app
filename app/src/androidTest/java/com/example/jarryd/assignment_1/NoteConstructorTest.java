package com.example.jarryd.assignment_1;

/**
 * Created by jarryd on 29/03/16.
 */
import junit.framework.*;

public class NoteConstructorTest extends TestCase {
    protected String noteId, imageId, noteTitle, noteText;
    protected Note aNote;

    // assigning the values
    protected void setUp(){
        noteId="noteid";
        imageId="imageid";
        noteTitle="this is the title";
        noteText="noteText this is the note text body";
    }

    // test method to add two values
    public void testConstructorAndGetters(){
        Note newNote = new Note(noteId, imageId,noteTitle,noteText);
        assertTrue(newNote.getNote_id().equals(noteId));
        assertTrue(newNote.getImage_id().equals(imageId));
        assertTrue(newNote.getNote_title().equals(noteTitle));
        assertTrue(newNote.getNote_text().equals(noteText));
    }
}