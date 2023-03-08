package com.example.kotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnClick: Button = findViewById(R.id.btnClick)
        val tvHello: TextView = findViewById(R.id.tvHello)
        val myCounter: Counter = Counter(5)

        btnClick.setOnClickListener {
            tvHello.text = "Hello!"
            Log.i("MAINACTIVITY", "Here we are! " + myCounter.value)
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show();
        }
    }
}