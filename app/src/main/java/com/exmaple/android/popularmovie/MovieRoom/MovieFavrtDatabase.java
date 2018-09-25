package com.exmaple.android.popularmovie.MovieRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Movie.class},version = 1,exportSchema = false)
public abstract class MovieFavrtDatabase extends RoomDatabase{
    private final static String DATABASE_NAME="favourite_movie_database";
    private static MovieFavrtDatabase databaseInstance=null;
    private static Object LOCK=new Object();

    public static MovieFavrtDatabase getDatabaseInstance(Context context) {
        synchronized (LOCK) {
            if(databaseInstance==null) {
                databaseInstance= Room.databaseBuilder(context.getApplicationContext(),
                        MovieFavrtDatabase.class, DATABASE_NAME)
                        .build();
            }
            return databaseInstance;
        }
    }

    public abstract MovieFavrtTaskDao movieFavrtTaskDao();

}
