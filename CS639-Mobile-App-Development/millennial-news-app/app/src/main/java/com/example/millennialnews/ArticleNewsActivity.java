package com.example.millennialnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ArticleNewsActivity extends AppCompatActivity {

    TextView tvNewsTitleFull;
    TextView tvNewsAuthorFull;
    TextView tvNewsDateFull;
    TextView tvNewsContentFull;
    ImageView ivNewsImageFull;
    Button btnFav;
    private DatabaseReference db;
    private Switch switchMode;
    private boolean viewingArticle;
    private boolean loginStatus;
    SaveState saveState;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveState = new SaveState(this);
        if(saveState.getState() == true)
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_settings);

        switchMode = findViewById(R.id.switchMode);
        if(saveState.getState() == true)
            switchMode.setChecked(true);

        setContentView(R.layout.activity_article_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        userID = getIntent().getStringExtra("userID");
        loginStatus = getIntent().getBooleanExtra("isLoggedIn", false);
        viewingArticle = true;
        tvNewsTitleFull = findViewById(R.id.tvNewsTitleFull);
        tvNewsAuthorFull = findViewById(R.id.tvNewsAuthorFull);
        tvNewsDateFull = findViewById(R.id.tvNewsDateFull);
        ivNewsImageFull = findViewById(R.id.ivNewsImageFull);
        tvNewsContentFull = findViewById(R.id.tvNewsContentFull);
        btnFav = findViewById(R.id.btn_fav);

        if (loginStatus == true)
            btnFav.setVisibility(View.VISIBLE);
        else
            btnFav.setVisibility(View.GONE);

        Intent intent = this.getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String date = intent.getStringExtra("date");
        String description = intent.getStringExtra("content");
        String image = intent.getStringExtra("image");

        tvNewsTitleFull.setText(title);
        tvNewsAuthorFull.setText(author);
        tvNewsDateFull.setText(date);
        tvNewsContentFull.setText(description);
        LoadImageNews imageFull = new LoadImageNews(ivNewsImageFull);
        imageFull.execute(image);

        if (userID != null)
            Log.d("ArticleNewsActivity", userID);

        db = FirebaseDatabase.getInstance().getReference("users");

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ArticleNewsActivity.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (userID != null) {
                            db.child(userID + "/articles").push().setValue(new NewsArticle(
                                    title, author, date, description, image
                            ));
                            Toast.makeText(v.getContext(), "Added to Favorites!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}
