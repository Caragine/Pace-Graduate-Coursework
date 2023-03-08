package com.example.millennialnews;

import android.os.Parcel;
import android.os.Parcelable;

import com.kwabenaberko.newsapilib.models.Source;

public class NewsArticle implements Parcelable {
    private Source source;
    private String author;
    private String title;
    private String description;
    private String date;
    private String image;

    public NewsArticle() {}

    public NewsArticle(
            Source source,
            String author,
            String title,
            String description,
            String date,
            String image
    ) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public NewsArticle (
            String title,
            String author,
            String date,
            String description,
            String image
    ) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public NewsArticle (Parcel in) {
        title = in.readString();
        author = in.readString();
        date = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public Source getSource() {
        if (source != null) {
            return source;
        }
        return null;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "News {" +
                "source='" + source + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<NewsArticle> CREATOR = new Parcelable.Creator<NewsArticle>()
    {
        public NewsArticle createFromParcel(Parcel in) {
            return new NewsArticle(in);
        }
        public NewsArticle[] newArray(int size) {
            return new NewsArticle[size];
        }
    };
}
