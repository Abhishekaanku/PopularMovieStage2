package com.exmaple.android.popularmovie.MovieRoom;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "movie_data")
public class Movie implements Parcelable {
    @PrimaryKey
    private int movieID;
    private String title;
    private String overView;
    private String poster;
    private String originalTitle;
    private float user_rating;
    private String releaseDate;
    private float popularity;
    @Ignore
    private boolean isFavrt=false;

    @Ignore
    public Movie() {}

    Movie(int movieID, String title, String overView, String poster,
          String originalTitle, float user_rating, String releaseDate, float popularity) {
        this.movieID=movieID;
        this.title=title;
        this.overView=overView;
        this.poster=poster;
        this.originalTitle=originalTitle;
        this.user_rating=user_rating;
        this.releaseDate=releaseDate;
        this.popularity=popularity;
    }

    private Movie(Parcel parcel) {
        movieID=parcel.readInt();
        title=parcel.readString();
        overView=parcel.readString();
        poster=parcel.readString();
        originalTitle=parcel.readString();
        user_rating=parcel.readFloat();
        releaseDate=parcel.readString();
        popularity=parcel.readFloat();
    }

    //Setter Methods

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setFavrt(boolean favrt) {
        isFavrt = favrt;
    }

    //Getter Methods

    public int getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getOverView() {
        return overView;
    }

    public String getPoster() {
        return poster;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getPopularity() {
        return popularity;
    }

    public boolean getIsFavrt() {
        return isFavrt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieID);
        parcel.writeString(title);
        parcel.writeString(overView);
        parcel.writeString(poster);
        parcel.writeString(originalTitle);
        parcel.writeFloat(user_rating);
        parcel.writeString(releaseDate);
        parcel.writeFloat(popularity);

    }
    public static final Parcelable.Creator<Movie> CREATOR= new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
