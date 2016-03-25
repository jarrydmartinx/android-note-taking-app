package com.example.jarryd.assignment_1;

import android.provider.BaseColumns;

/**
 * Created by jarryd on 24/03/16.
 */
public final class NoteDBContract {

    public NoteDBContract() {}

        /* Defines the names and headers for tables and columns */
        /* Make sure you understand exactly why it's good to have a contract */

        public static abstract class NoteEntry implements BaseColumns {
            public static final String TABLE_NAME = "notes";
            public static final String COLUMN_NAME_NOTE_ID = "note_id";
            public static final String COLUMN_NAME_IMAGE_ID= "image_id";
            public static final String COLUMN_NAME_NOTE_TITLE = "note_title";
            public static final String COLUMN_NAME_NOTE_TEXT = "note_text";
        }
}
