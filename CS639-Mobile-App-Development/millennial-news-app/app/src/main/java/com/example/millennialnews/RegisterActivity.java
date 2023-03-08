package com.example.millennialnews;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private Switch switchMode;
    SaveState saveState;
    private EditText firstName, lastName, password, email;
    private Button signUpBtn;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveState = new SaveState(this);
        if (saveState.getState() == true)
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_settings);
        switchMode = findViewById(R.id.switchMode);
        if (saveState.getState() == true)
            switchMode.setChecked(true);

        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // INITIALIZING VARIABLES
        signUpBtn = findViewById(R.id.reg_button);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // DATABASE
        db = FirebaseDatabase.getInstance().getReference("users");
        Log.d("RegisterActivity - DB", db.toString());

        // REGISTER PROCESSING
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(v.getContext(), LoginActivity.class);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userFirstName = firstName.getText().toString().trim();
                        String userLastName = lastName.getText().toString().trim();
                        String userEmail = email.getText().toString().trim();
                        String userPassword = password.getText().toString().trim();
                        String user;
                        boolean doesUserExist = false;
                        int counter = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d("DS", ds.toString());
                            user = ds.child("email").getValue().toString();
                            if (user != null) {
                                if (user.equals(userEmail)) {
                                    Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                                    doesUserExist = true;
                                }
                            }
                            counter++;
                        }
                        if (!doesUserExist) {
                            if (userFirstName.isEmpty()) {
                                firstName.setError("Please enter the First Name");
                            } else if (userLastName.isEmpty()) {
                                lastName.setError("Please enter the Last Name");
                            } else if (userEmail.isEmpty()) {
                                email.setError("Please enter the Email");
                            } else if (userPassword.isEmpty()) {
                                password.setError("Please enter a Password");
                            } else {
                                // If none of the values are empty,
                                // add user to the Database and show a Toast with confirmation
                                User newUser = new User(
                                        userFirstName,
                                        userLastName,
                                        userPassword,
                                        userEmail
                                );
                                db.child(String.valueOf(counter)).setValue(newUser);
                                firstName.setText("");
                                lastName.setText("");
                                email.setText("");
                                password.setText("");
                                Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_LONG).show();
                                startActivity(loginIntent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("RegisterActivity - DB", "Failed to read value.", error.toException());
                    }
                });
            }
        });
    }

}