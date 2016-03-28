package com.example.jarryd.assignment_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jarryd on 26/03/16.
 * This implementation draws heavily on the Android Developer Guide provided at
 * http://developer.android.com/training/basics/data-storage/databases.html
 * <p/>
 * Helper class for accessing instances of the SQLite database
 */
public class NoteDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 1;
    private static NoteDBHelper noteDBInstance;

    //Constructor is private to prevent direct instantiations of the database
    private NoteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized NoteDBHelper getInstance(Context context) {
        if (noteDBInstance == null) {
            noteDBInstance = new NoteDBHelper(context.getApplicationContext());
        }
        return noteDBInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase noteDB) {
        noteDB.execSQL(NoteDAOImplSQLite.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase noteDB, int oldVersion, int newVersion) {
        noteDB.execSQL(NoteDAOImplSQLite.SQL_DELETE_ENTRIES);
        onCreate(noteDB);
    }

    @Override
    public void onDowngrade(SQLiteDatabase noteDB, int oldVersion, int newVersion) {
        onUpgrade(noteDB, oldVersion, newVersion);
    }

}



