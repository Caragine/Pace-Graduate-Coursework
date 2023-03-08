package com.example.millennialnews;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.NewsViewHolder> {

    private final List<NewsArticle> newsList;

    public ProfileAdapter(List<NewsArticle> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.tvNewsTitle.setText(newsList.get(position).getTitle());
        holder.tvNewsAuthor.setText(newsList.get(position).getAuthor());
        holder.tvNewsDate.setText(newsList.get(position).getDate());
        LoadImageNews image = new LoadImageNews(holder.ivNewsImage);
        image.execute(newsList.get(position).getImage());

        holder.newsItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleNewsActivity.class);
                intent.putExtra("title", newsList.get(position).getTitle());
                intent.putExtra("author", newsList.get(position).getAuthor());
                intent.putExtra("date", newsList.get(position).getDate());
                intent.putExtra("content", newsList.get(position).getDescription());
                intent.putExtra("image", newsList.get(position).getImage());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvNewsTitle;
        TextView tvNewsAuthor;
        TextView tvNewsDate;
        ImageView ivNewsImage;
        ConstraintLayout newsItemLayout;

        public NewsViewHolder(View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitleFull);
            tvNewsAuthor = itemView.findViewById(R.id.tvNewsAuthorFull);
            tvNewsDate = itemView.findViewById(R.id.tvNewsDateFull);
            ivNewsImage = itemView.findViewById(R.id.ivNewsImageFull);
            newsItemLayout = itemView.findViewById(R.id.newsItemLayout);
        }
    }
}
