package com.example.sqliteexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    StudentDatabaseHelper dbHelper;
    SQLiteDatabase database;
    private EditText insertName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new StudentDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        insertName = findViewById(R.id.insertET);

    }

    public void insertToDB(View view) {
        ContentValues value = new ContentValues();
        String input = insertName.getText().toString();
        if (!input.isEmpty()) {
            value.put("name", input);
            long entry = database.insert("student1", null, value);
            Log.i("ON_INSERT_BTN", "INSERTING " + input);
        }
        Cursor cur;
        String[] idArray = {"id"};
        cur = database.query("student1", idArray, "name='Adam'", null, null, null, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            Log.i("SQLite", cur.getString( 0));
            cur.moveToNext();
        }
        cur.close();
    }
}