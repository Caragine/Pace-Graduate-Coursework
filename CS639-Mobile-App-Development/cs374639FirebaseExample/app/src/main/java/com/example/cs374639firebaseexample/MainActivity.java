package com.example.cs374639firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView)findViewById(R.id.tv);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.i("MAINACTIVITY", database.toString());
        DatabaseReference myRef = database.getReference("message");
        Log.i("MAINACTIVITY", myRef.toString());

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("MAINACTIVITY", "Value is: " + value);
                tv.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MAINACTIVITY", "Failed to read value.", error.toException());
            }
        });

    }
}