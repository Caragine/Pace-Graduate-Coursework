package com.example.firebasedatabase;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    EditText firstNameET;
    EditText lastNameET;
    TextView databaseTV;
    DatabaseReference database;
    int counter = 0;
    int ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference("employees");
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        databaseTV = findViewById(R.id.databaseTV);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method called once with initial value and then again when data at location is updated
                ID = (int) snapshot.getChildrenCount();
                Employee emp;
                counter = 0;
                StringBuilder value = new StringBuilder();
                for (DataSnapshot ds: snapshot.getChildren()) {

                    emp = (Employee)ds.getValue(Employee.class);
                    Log.i("MAINACTIVITY", counter + " - First Name: " + emp.getFirstName() + " Last Name: " + emp.getLastName());
                    value.append(emp.toString(Integer.toString(counter)));
                    counter = counter+1;
                }

                databaseTV.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if failed to read value
                Log.w("MAINACTIVITY", "Failed to read value.", error.toException());
            }
        });
    }

    public void insertToDatabase(View view) {
        String ID = Integer.toString(counter);
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();

        if (!firstName.isEmpty() && !lastName.isEmpty()) {

            Log.i("ADDING TO DATABASE", "ID= " + ID);
            Log.i("ADDING TO DATABASE", "First Name= " + firstName);
            Log.i("ADDING TO DATABASE", "Last Name= " + lastName);

            Employee emp = new Employee(firstName, lastName);
            database.child(String.valueOf(counter)).setValue(emp);
            firstNameET.setText("");
            lastNameET.setText("");
        }

    }
}