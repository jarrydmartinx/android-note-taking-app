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
public class Note {

    /*Declare Note class attributes*/
    public String note_id;
    public String image_id;
    public String note_text;
    public String note_title;

        /* Constructor for new note */
        public Note(){
            super();
            note_id = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss_").format(new Date());
            image_id = null;
            note_text = null;
            note_title = null;
        }

        /* Constructor for existing note read from DB for preview*/
        public Note(String note_id, String image_id, String note_title) {
            super();
            this.note_id = note_id;
            this.image_id = image_id;
            this.note_text = null;
            this.note_title = note_title;
//            }
//            else {
//                setTitleAsHead();
//            }
        }

        public void setTitleAsHead() {
            int head_length;
            if (this.image_id == null){
                head_length = 80; //MAGIC NUMBERZZZZZZZZZZZZZZZZZZZZZZZZZ
            } else head_length = 30; //MAGIC NUMBERZZZZZZZZZZZZZZZZZZZZZZZZZ
            this.note_title = note_text.substring(0, Math.min(note_text.length(),head_length))+"...";
        }

        public String getNote_id(){
            return note_id;
        }

        public String getImage_id(){
            return image_id;
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

        public void setNote_title(String note_title){
            this.note_title = note_title;
        }

        public void setAllNoteAttributes(String note_id, String image_id, String note_text, String note_head){
            this.note_id = note_id;
            this.image_id = image_id;
            this.note_text = note_text;
            this.note_title = note_title;
        }


}
