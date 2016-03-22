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
        public String note_title;
        public String timeStamp;
        public String image_name;


        public Note(){
            super();
        }

        /* Constructor for note without image*/
        public Note(String title, Date date) {
            super();
            this.note_title = title;
            this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            this.image_name = "!NO_IMAGE!";
        }




        /* Constructor for note with image */
        public Note(String title, Date date, String image_name) {
        super();
        this.note_title = title;
        this.dateModified = date;
        this.image_name = image_name;
    }

        public static void setNoteTitle() {
            String upToNCharacters = s.substring(0, Math.min(s.length(), n));


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
                String noteFileName = "NOTE_" + timeStamp + "_";
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
