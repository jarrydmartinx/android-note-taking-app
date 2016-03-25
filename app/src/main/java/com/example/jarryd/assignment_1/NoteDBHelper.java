package com.example.jarryd.assignment_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jarryd on 24/03/16.
 */
public class NoteDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Notes.db";
    public static final int DB_VERSION = 1;

        public NoteDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase noteDB) {
            noteDB.execSQL(NoteDAOImplSQLite.SQL_CREATE_ENTRIES);
        }
        @Override
        public void onUpgrade(SQLiteDatabase noteDB, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            noteDB.execSQL(NoteDAOImplSQLite.SQL_DELETE_ENTRIES);
            onCreate(noteDB);
        }

        @Override
        public void onDowngrade(SQLiteDatabase noteDB, int oldVersion, int newVersion) {
            onUpgrade(noteDB, oldVersion, newVersion);
        }
    }
