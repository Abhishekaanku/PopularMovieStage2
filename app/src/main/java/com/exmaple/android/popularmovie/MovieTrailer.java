package com.exmaple.android.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable{
    private String videoName;
    private String key;

    public MovieTrailer() {}

    public String getVideoName() {
        return videoName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    private MovieTrailer(Parcel p) {
        videoName=p.readString();
        key=p.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoName);
        parcel.writeString(key);
    }
    public static final Parcelable.Creator<MovieTrailer> CREATOR= new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel parcel) {
            return new MovieTrailer(parcel);
        }

        @Override
        public MovieTrailer[] newArray(int i) {
            return new MovieTrailer[0];
        }
    };
}
