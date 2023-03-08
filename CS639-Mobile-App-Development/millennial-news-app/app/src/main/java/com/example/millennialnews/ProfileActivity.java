package com.example.millennialnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName;
    private RecyclerView profileNews;
    SaveState saveState;
    private Switch switchMode;
    private ProfileAdapter profileAdapter;
    private ArrayList<NewsArticle> articleList;
    ArrayList<NewsArticle> newArticleList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // === SETTING UP NIGHT MODE ===
        saveState = new SaveState(this);
        if(saveState.getState())
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_settings);
        switchMode = findViewById(R.id.switchMode);
        if(saveState.getState())
            switchMode.setChecked(true);

        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.userName);
        profileNews = findViewById(R.id.profileNews);

        String userFirstName = getIntent().getStringExtra("userFirstName");
        articleList = getIntent().getParcelableArrayListExtra("userArticleList");

        Log.d("ARTICLE LIST", articleList.toString());

        userName.setText("Welcome, " + userFirstName);

        profileAdapter = new ProfileAdapter(articleList);
        profileNews.setAdapter(profileAdapter);
        profileNews.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        articleList.clear();
        newArticleList = getIntent().getParcelableArrayListExtra("userArticleList");
        articleList.addAll(newArticleList);
        profileAdapter.notifyDataSetChanged();
    }
}