package com.exmaple.android.popularmovie.MovieRoom;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieDatabaseCallExecutor {
    private Executor databaseIO;
    private Executor mainThreadExecutor;
    private static Object LOCK=new Object();
    private static MovieDatabaseCallExecutor movieDatabaseCallExecutor=null;

    private MovieDatabaseCallExecutor(Executor databaseIO,Executor mainThreadExecutor) {
        this.databaseIO=databaseIO;
        this.mainThreadExecutor=mainThreadExecutor;
    }

    public Executor getDatabaseIO() {
        return databaseIO;
    }

    public Executor getMainThreadExecutor() {
        return mainThreadExecutor;
    }

    public static MovieDatabaseCallExecutor getExecutorInstance() {
        if(movieDatabaseCallExecutor==null) {
            synchronized (LOCK) {
                movieDatabaseCallExecutor=new MovieDatabaseCallExecutor(Executors.newSingleThreadExecutor(),
                        new MainThreadExecutor());
            }
        }
        return movieDatabaseCallExecutor;
    }
    static class MainThreadExecutor implements Executor{
        private Handler mHandler=new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mHandler.post(runnable);
        }
    }

}
