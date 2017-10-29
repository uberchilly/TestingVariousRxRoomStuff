package com.example.uberchilly.testingvariousrxroomstuff;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.uberchilly.testingvariousrxroomstuff.roomrxflowable.AppDatabase;

/**
 * Created by uberchilly on 29-Oct-17.
 */

public class MyApplication extends Application {
    private AppDatabase database;

    public AppDatabase getDatabase() {
        return this.database;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "nexoslav_db").build();
    }
}
