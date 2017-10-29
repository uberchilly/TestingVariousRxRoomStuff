package com.example.uberchilly.testingvariousrxroomstuff.roomrxflowable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by uberchilly on 29-Oct-17.
 */

@Entity(tableName = "desserts")
public class Dessert {
    @PrimaryKey
    @ColumnInfo(name = "api_level")
    private int apiLevel;
    private String name;

    public Dessert(int apiLevel, String name) {
        this.apiLevel = apiLevel;
        this.name = name;
    }

    public int getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(int apiLevel) {
        this.apiLevel = apiLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
