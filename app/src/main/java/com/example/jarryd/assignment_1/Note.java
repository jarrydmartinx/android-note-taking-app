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
 * Created by jarryd on 20/03/16. REMEMBER IT DOESN"T NEED TO BE SERIALIZABLE IF YOU USE JSON
 */
public class Note implements Serializable {

    /*Declare Note class attributes*/
    public String note_id;
    public String image_id;
    public String note_text;
    public String note_head;

        /* Constructor for fake notes */
        public Note(){
            super();
            note_id = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss_").format(new Date());
            image_id = null;
            note_text = "Sample Note Text";
            note_head = "Blank Note";
        }

        /* Constructor for new note
        public Note(){
            super();
            note_id = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss_").format(new Date());
            image_id = null;
            note_text = "";
            note_head = "Blank Note";
        }
        */


        /* Constructor for existing note WHAT THE HELL IS THIS SUPPOSED TO BE FOR???*/
        public Note(String note_id, String image_id,String note_text) {
            super();
            this.note_id = note_id;
            this.image_id = image_id;
            this.note_text = note_text;
            setNote_head();
        }

        public String getNote_id(){
            return note_id;
        }

        public String getImage_id(){
            return image_id;
        }

        public void setNote_head() {
            int head_length;
            if (this.image_id == null){
                head_length = 60; //MAGIC NUMBERZZZZZZZZZZZZZZZZZZZZZZZZZ
            } else head_length = 20; //MAGIC NUMBERZZZZZZZZZZZZZZZZZZZZZZZZZ

            this.note_head = note_text.substring(0, Math.min(note_text.length(),head_length))+"...";
        }

        public void setNote_head(String note_head) {
            this.note_head = note_head;
        }

        public void setNote_id(String note_id) {
            this.note_id = note_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public void setNote_text(String note_text) {
            this.note_text = note_text;
        }

        public void setAll(String note_id, String image_id, String note_text, String note_head){
            this.note_id = note_id;
            this.image_id = image_id;
            this.note_text = note_text;
            this.note_head = note_head;
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
