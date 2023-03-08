package com.example.midtermadamcaraginev3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.midtermadamcaraginev3.extra.REPLY";
    public static final String EXTRA_MESSAGE = "";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.input);
    }

    public void launchSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        double result = 0;
        String msg = input.getText().toString();
        result = 1.13 * (Double.parseDouble(msg));
        msg = String.valueOf(result);
        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }
}