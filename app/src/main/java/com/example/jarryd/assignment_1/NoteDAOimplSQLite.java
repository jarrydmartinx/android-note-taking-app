package com.example.jarryd.assignment_1;

import android.content.ContentValues;
import android.content.Context;
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
    private NoteDBHelper = new NoteDBHelper(context);


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

    public NoteDAOimplSQLite(Context context){
        this.context = context;
    }



    // Instantiate the SQLite Database in write mode
    SQLiteDatabase noteDB = noteDBHelper.getWritableDatabase();

    // Create a new table of values, column names are the keys
    ContentValues values = new ContentValues();

    public void createNote();
    public void saveNote(Note note);
    public void deleteNote(Note note);

    public Note getNotebyId(String note_id);
    public String getImageId(String note_id);


    public createNote
    values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_ID, note_id);
    values.put(NoteDBContract.NoteEntry.COLUMN_NAME_IMAGE_ID, image_id);
    values.put(NoteDBContract.NoteEntry.COLUMN_NAME_NOTE_TEXT, note_text);

    // Insert the new row, returning the primary key value of the new row
    long newRowId;
    newRowId = db.insert(
    NoteDBContract.NoteEntry.TABLE_NAME,
    NoteDBContract.NoteEntry.COLUMN_NAME_NULLABLE,
    values);















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