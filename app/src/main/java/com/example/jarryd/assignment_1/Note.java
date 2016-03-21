package com.example.jarryd.assignment_1;

import android.media.Image;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jarryd on 20/03/16.
 */
public class Note implements Serializable {

        /*Declare Note class attributes*/
        public String note_title;
        public Date dateModified;
        public String image_name;

        public Note(){
            super();
        }

        /* Constructor for note without image*/
        public Note(String title, Date date) {
            super();
            this.note_title = title;
            this.dateModified = date;
            this.image_name = "!NO_IMAGE!";
        }

        /* Constructor for note with image */
        public Note(String title, Date date, String image_name) {
        super();
        this.note_title = title;
        this.dateModified = date;
        this.image_name = image_name;
    }

        /* Note load method */
        static public Note load(String filename) {
            Note loaded_note = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

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

        public void save(String filename) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));

                oos.writeObject(this);
                oos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

}
