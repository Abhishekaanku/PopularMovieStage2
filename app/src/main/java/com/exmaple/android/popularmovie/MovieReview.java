package com.exmaple.android.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable {
    private String author;
    private String content;
    private String id;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MovieReview() {}

    @Override
    public int describeContents() {
        return 0;
    }

    private MovieReview(Parcel p) {
        author=p.readString();
        content=p.readString();
        id=p.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(id);
    }
    public static final Parcelable.Creator<MovieReview> CREATOR= new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel parcel) {
            return new MovieReview(parcel);
        }

        @Override
        public MovieReview[] newArray(int i) {
            return new MovieReview[i];
        }
    };

}
