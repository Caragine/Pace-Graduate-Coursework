package com.example.sqliteexercise;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class StudentDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "student1.db";
    private static int DATABASEVERSION = 1;

    public StudentDatabaseHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE student1(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Auto-generated method stub
    }

    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}
