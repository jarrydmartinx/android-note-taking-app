package com.example.jarryd.assignment_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.SparseBooleanArray;

import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * Created by jarryd on 24/03/16. draws on http://developer.android.com/reference/android/util/JsonReader.html, http://developer.android.com/reference/android/util/JsonWriter.html
 */
public class NoteDAOImplSQLite implements NoteDAO {
    public NoteDBHelper noteDBHelper;

    private static final String TYPE = " TEXT";
    private static final String SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NoteDBContract.NoteEntry.TABLE_NAME + " (" +
                    NoteDBContract.NoteEntry._ID + " INTEGER PRIMARY KEY" + SEP +
                    NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID + TYPE + SEP +
                    NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID + TYPE + SEP +
                    NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE + TYPE + SEP +
                    NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TEXT + TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteDBContract.NoteEntry.TABLE_NAME;

    public NoteDAOImplSQLite(Context context) {
        noteDBHelper = NoteDBHelper.getInstance(context);
    }


    @Override
    public void saveNewNoteData(Note note) {
        SQLiteDatabase noteDB = noteDBHelper.getWritableDatabase();

        //Creates a new Hash Map of values where the column names of noteDB are the keys
        ContentValues values = new ContentValues();
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID, note.getNote_id());
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID, note.getImage_id());
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE, note.note_title);
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TEXT, note.note_text);

        noteDB.insert(NoteDBContract.NoteEntry.TABLE_NAME, null, values);
        System.out.println("#################new note put in DB: " + values + ", #########################");
    }

    @Override
    public Note loadNote(String note_id) {
        Note note = new Note();
        SQLiteDatabase noteDB = noteDBHelper.getWritableDatabase();

        String where = NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID + " LIKE ?";
        String[] whereArgs = {note_id};

        Cursor cursor = noteDB.query(
                NoteDBContract.NoteEntry.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                where,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        cursor.moveToFirst();
        note.note_id = getStringFromCursor(cursor, NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID);
        note.image_id = getStringFromCursor(cursor, NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID);
        note.note_title = getStringFromCursor(cursor, NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE);
        note.note_text = getStringFromCursor(cursor, NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TEXT);
        cursor.close();

        return note;
    }

    @Override
    public void updateNoteData(Note note) {
        SQLiteDatabase noteDB = noteDBHelper.getReadableDatabase();

        // Create a hash map where DB column names are keys and note member fields are the values
        // Updates the stored value of every field of the note object on which updateNote is called
        ContentValues values = new ContentValues();
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID, note.getNote_id());
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID, note.getImage_id());
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE, note.note_title);
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TEXT, note.note_text);

        // Which row to update, based on the ID
        String where = NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID + " LIKE ?";
        String[] whereArgs = {note.getNote_id()};

        noteDB.update(NoteDBContract.NoteEntry.TABLE_NAME, values, where, whereArgs);
        System.out.println("#################note updated in DB: " + values + ", #########################");

    }

    @Override
    public void deleteNoteDataAndImage(Note note) {
        ArrayList<Note> singleNoteArray = new ArrayList<>();
        singleNoteArray.add(note);
        deleteMultiNoteDataAndImage(singleNoteArray);
    }

    @Override
    public void deleteMultiNoteDataAndImage(ArrayList<Note> noteArrayList) {
        SQLiteDatabase noteDB = noteDBHelper.getWritableDatabase();
        String where = NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID;
        // Specify arguments in placeholder order.???????????????????????????????????????????????
        String[] whereArgs = new String[noteArrayList.size()];
        for (int i=0;i<noteArrayList.size();i++) {
            whereArgs[i] = "'" + noteArrayList.get(i).note_id + "'";
        }
        //SQL delete statement
        noteDB.execSQL("DELETE FROM " + NoteDBContract.NoteEntry.TABLE_NAME + " WHERE " + where + " IN " + "("+TextUtils.join(", ", whereArgs)+")");
    }

    @Override
    public ArrayList<Note> getAllSavedNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        SQLiteDatabase noteDB = noteDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database (CURRENTLY USING ALL COLUMNS BUT REALLY SHOULD ONLY USE NOTE TITLES WHEN LOADING FROM STORAGE
        // you will actually use after this query.
        String[] proj = {
                NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID,
                NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID,
                NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE
        };

        Cursor cursor = noteDB.query(
                NoteDBContract.NoteEntry.TABLE_NAME,  // The table to query
                proj,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Note loadedNote = new Note(
                            getStringFromCursor(cursor, NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID),
                            getStringFromCursor(cursor, NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID),
                            getStringFromCursor(cursor, NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE));
            noteList.add(loadedNote);
            System.out.println(loadedNote.note_id);
            cursor.moveToNext();
        }
        cursor.close();

        return noteList;
    }

    private String getStringFromCursor(Cursor cursor, String column_name){
        String attr = cursor.getString(cursor.getColumnIndexOrThrow(column_name));
        return attr;
    }


}
