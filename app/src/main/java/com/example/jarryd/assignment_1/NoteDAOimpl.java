package com.example.jarryd.assignment_1;

import android.content.Context;
import android.util.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by jarryd on 24/03/16. draws on http://developer.android.com/reference/android/util/JsonReader.html
 */
public class NoteDAOimpl implements NoteDAOInterface {
    ArrayList<Note> noteList;

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

}



