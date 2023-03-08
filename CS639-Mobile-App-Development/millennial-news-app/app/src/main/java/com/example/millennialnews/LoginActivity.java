package com.example.millennialnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserEmail;
    private EditText etUserPassword;
    private Button btnLogin;
    private DatabaseReference db;
    private User currentUser;
    private Switch switchMode;
    SaveState saveState;
    String userID;

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

        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // INITIALIZING VARIABLES
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPassword = findViewById(R.id.etUserPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // === DATABASE ===
        db = FirebaseDatabase.getInstance().getReference("users");
        Log.w("LoginActivity - DB", db.toString());

        // TO ADD IN LOGIN ACTIVITY
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userEmail = etUserEmail.getText().toString();
                        String userPassword = etUserPassword.getText().toString();
                        String password;
                        String email;
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        boolean isLoggedIn = false;
                        int counter = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            password = ds.child("password").getValue().toString();
                            email = ds.child("email").getValue().toString();
                            Log.d("LoginActivity - DB", ds.getValue().toString());
                            if (email.equals(userEmail) && password.equals(userPassword)) {
                                Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                isLoggedIn = true;
                                Log.d("LoginActivity - DB", ds.getValue().toString());
                                intent.putExtra("isLoggedIn", isLoggedIn);
                                Log.d("LOGINACTIVITY COUNTER", Integer.toString(counter));
                                userID = Integer.toString(counter);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userFirstName", ds.child("firstName").getValue().toString());
                                break;
                            }
                            counter++;
                        }
                        if (!isLoggedIn) {
                            Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("LoginActivity - DB", "Failed to read value.", error.toException());
                    }
                });
            }
        });
    }
}