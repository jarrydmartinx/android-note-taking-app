package com.example.jarryd.assignment_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by jarryd on 24/03/16. draws on http://developer.android.com/reference/android/util/JsonReader.html, http://developer.android.com/reference/android/util/JsonWriter.html
 */
public class NoteDAOimplSQLite implements NoteDAOInterface {
    private Context context;
    private NoteDBHelper noteDBHelper = new NoteDBHelper(context);


    private static final String TYPE = " TEXT";
    private static final String SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NoteDBContract.NoteEntry.TABLE_NAME + " (" +
                    NoteDBContract.NoteEntry._ID + " INTEGER PRIMARY KEY" + SEP +
                    NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID + TYPE + SEP +
                    NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID + TYPE + SEP +
                    NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TEXT + TYPE + SEP + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteDBContract.NoteEntry.TABLE_NAME;

    public NoteDAOimplSQLite(Context context) {
        this.context = context;
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

        long new_integer_row_id;
        new_integer_row_id = noteDB.insert(
                NoteDBContract.NoteEntry.TABLE_NAME, null, values);

        if (note.getImage_id() != null) {
            saveNoteImageToFile(note);
        }
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

        note.note_id = cursor.getString(1);
        note.image_id = cursor.getString(2);
        note.note_title = cursor.getString(3);
        note.note_text = cursor.getString(4);

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
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE, note.note_text);
        values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TEXT, note.note_title);

        // Which row to update, based on the ID
        String where = NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID + " LIKE ?";
        String[] whereArgs = {note.getNote_id()};

        int count = noteDB.update(NoteDBContract.NoteEntry.TABLE_NAME, values, where, whereArgs);

        if (note.image_id != null){
            saveNoteImageToFile(note);
        }
    }

    @Override
    public void deleteNoteData(Note note) {
        SQLiteDatabase noteDB = noteDBHelper.getWritableDatabase();
        String where = NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID + " LIKE ?";
        // Specify arguments in placeholder order.???????????????????????????????????????????????
        String[] whereArgs = { note.getNote_id() };
       //SQL delete statement
        noteDB.delete(NoteDBContract.NoteEntry.TABLE_NAME, where, whereArgs);

        if (note.getImage_id() != null) {
            deleteNoteImageFromFile(note);
        }
    }



    }

    public Note getAllSavedNotes(Context context) {
        Note note;
        SQLiteDatabase noteDB = noteDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database (CURRENTLY USING ALL COLUMNS BUT REALLY SHOULD ONLY USE NOTE TITLES WHEN LOADING FROM STORAGE
        // you will actually use after this query.
        String[] proj = {
                NoteDBContract.NoteEntry._ID,
                NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID,
                NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID,
                NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TITLE
        };

        Cursor cursor = noteDB.query(
                NoteDBContract.NoteEntry.TABLE_NAME,  // The table to query
                proj,                               // The columns to return
                where,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return note;
    }



    private void saveNoteImageToFile(Note note) {
        //must write this helper method
    }

    private void deleteNoteImageFromFile(Note note) {
        //write this helper method

    }
    // Insert the new row, returning the primary key value of the new row
















    @Override
    public ArrayList<Note> getAllSavedNotes(Context context) {
        ArrayList<Note> noteList;
        File file = new File(context.getFilesDir(), context.getResources().getString(R.string.notes_file));
        try {
            InputStream inputStream = new FileInputStream(file);
            noteList = readJsonInputStream(inputStream);
            return noteList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createNote();

    public void updateNote(Note note);

    public void deleteNote(Note note);

    public Note getNotebyId(String note_id);

    @Override
    public void deleteNote(Note note) {
        noteList.remove(note);
    }

    //retrieve list of students from the database


    @Override
    public Note getNote(String note_id) {
        for (int i = 0; i < noteList.size(); i++) {
            if (note_id.equals(noteList.get(i).note_id))
                return noteList.get(i);
        }
        return noteList.get(-1);
    }

    @Override
    public void updateNote(Note note) {
        noteList.get(getNoteIndexInList(note.getNote_id())).setAll(note.note_id, note.image_id, note.note_text, note.note_head);
    }

    public int getNoteIndexInList(String note_id) {
        for (int i = 0; i < noteList.size(); i++) {
            if (note_id.equals(noteList.get(i).note_id))
                return i;
        }
        return -1;
    }


    //JSON PARSER

    //JSON reader
    public ArrayList<Note> readJsonInputStream(InputStream inputStream) throws IOException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        try {
            return readNoteDataArray(jsonReader);
        } finally {
            jsonReader.close();
        }
    }

    public ArrayList<Note> readNoteDataArray(JsonReader jsonReader) throws IOException {
        ArrayList<Note> noteList = new ArrayList<>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            noteList.add(readNoteData(jsonReader));
        }
        jsonReader.endArray();
        return noteList;
    }

    public Note readNoteData(JsonReader jsonReader) throws IOException {
        String note_id = null;
        String image_id = null;
        String note_text = null;

        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            String keyname = jsonReader.nextName();
            if (keyname.equals("note_id")) {
                note_id = jsonReader.nextString();
            } else if (keyname.equals("image_id")) {
                image_id = jsonReader.nextString();
            } else if (keyname.equals("note_text")) {
                note_text = jsonReader.nextString();
            }else jsonReader.skipValue();
        }
        jsonReader.endObject();
        return new Note(note_id,image_id,note_text);
        }

    }

    //JSON writer
    public void saveNoteList(OutputStream outputStream, ArrayList<Note> noteList) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        jsonWriter.setIndent("    ");
        writeNoteArrayList(jsonWriter, noteList);
        jsonWriter.close();
    }

    public void saveNote(OutputStream outputStream, Note note) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        jsonWriter.setIndent("    ");
        writeNote(jsonWriter, note);
        jsonWriter.close();
    }

    private void writeNoteArrayList(JsonWriter jsonWriter, ArrayList<Note> noteList) throws IOException {
       jsonWriter.beginArray();
        for (int i=0;i < noteList.size();i++) {
            jsonWriter.beginObject();
            jsonWriter.name("note_id").value(noteList.get(i).getNote_id());
            jsonWriter.name("image_id").value(noteList.get(i).getNote_id());
            jsonWriter.name("note_text").value(noteList.get(i).getNote_id());
            jsonWriter.endObject();

        }
        jsonWriter.endArray();
    }


    public void writeNote(JsonWriter jsonWriter, Note note) throws IOException {
         jsonWriter.beginObject();
         jsonWriter.name("note_id").value(note.getNote_id();
         jsonWriter.name("image_id").value(note.getNote_id();
         jsonWriter.name("note_text").value(note.getNote_id();
         jsonWriter.endObject();
       }

    public void deleteNoteFromData(Note note) {

    }
}