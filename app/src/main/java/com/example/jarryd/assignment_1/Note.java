package com.example.jarryd.assignment_1;

import android.content.Intent;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jarryd on 20/03/16.
 * The Note class is the data model for each note. It contains the text, title, and unique ids for
 * itself and its associated image (if any)
 */
public class Note {

    /**
     * Unique note id generated when Note first created by user
     */
    public String note_id;
    /**
     * Unique image id generated when image first created by user
     */
    public String image_id;
    /**
     * The body text of the Note
     */
    public String note_text;
    /**
     * The title of the note shown in Note Previews in MainActivity
     */
    public String note_title;

    /**
     * Constructor for new Note (id is set by date stamp)
     */
    public Note() {
        super();
        note_id = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss_").format(new Date());
        image_id = null;
        note_text = null;
        note_title = null;
    }

    /**
     * Constructor for existing note read from storage
     */
    public Note(String note_id, String image_id, String note_title, String note_text) {
        super();
        this.note_id = note_id;
        this.image_id = image_id;
        this.note_text = note_text;
        this.note_title = note_title;
    }

    /**
     * Gets the head of the note for use as a title if title empty
     *
     * @return
     */
    public String getNoteHead() {
        int head_length;
        if (this.image_id == null) {
            head_length = 80;
        } else head_length = 30;
        return note_text.substring(0, Math.min(note_text.length(), head_length)) + "...";
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    /**
     * Helper method creates an Intent for sharing the instance Note
     *
     * @return a share Intent
     */
    public Intent createShareNoteIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, note_title);

        shareIntent.putExtra(Intent.EXTRA_TEXT, note_text);
        if (image_id != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(image_id));
        }
        return shareIntent;
    }

}
