package com.example.jarryd.assignment_1;

import android.content.Context;
import android.media.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jarryd on 20/03/16.
 */
public class Note implements Serializable {

    /*Declare Note class attributes*/
    public String note_id;
    public String image_id;
    public String note_text;
    public String note_head;

        /* Constructor for new note */
        public Note(){
            super();
            note_id = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss_").format(new Date());;
            image_id = null;
            note_text = "";
            note_head = "Blank Note";
        }

        /* Constructor for existing note */
        public Note(String note_id, String image_id,String note_text) {
            super();
            this.note_id = note_id;
            this.image_id = image_id;
            this.note_text = note_text;
            this.note_head = getNoteHead(50);
        }

        public String getNoteHead(int head_length) {
            String note_head = note_text.substring(0, Math.min(note_text.length(),head_length))+"...";
            return note_head;
        }

        /* Note load method */
        static public Note loadNoteFromFile(File file) {
            Note loaded_note = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                loaded_note = (Note) ois.readObject();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            return loaded_note;
        }


        public void saveNoteToFile() {
            try {
                String noteFileName = "NOTE_" + note_id + "_";
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(noteFileName));

                oos.writeObject(this);
                oos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        public static Note[] loadAllNotesFromDir(File directory) {

            FilenameFilter noteFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("NOTE_");
                }
            };

            File[] fileList = directory.listFiles(noteFilter);
            Note[] noteArray = new Note[fileList.length];
            for (int i = 0; i < fileList.length; i++) {
                noteArray[i] = loadNoteFromFile(fileList[i]);
            }
            return noteArray;
        }
}
