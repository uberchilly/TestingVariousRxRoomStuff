package com.example.uberchilly.testingvariousrxroomstuff.roomrxflowable;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by uberchilly on 29-Oct-17.
 */

@Database(entities = {Dessert.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DessertDao dessertDao();
}
