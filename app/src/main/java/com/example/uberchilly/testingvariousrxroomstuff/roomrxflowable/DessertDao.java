package com.example.uberchilly.testingvariousrxroomstuff.roomrxflowable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by uberchilly on 29-Oct-17.
 */

@Dao
public interface DessertDao {
    @Query("SELECT * FROM desserts")
    List<Dessert> getDesserts();

    @Query("SELECT * FROM desserts")
    Flowable<List<Dessert>> getDessertsFlowable(); //should emmit events on every change afterwards

    @Insert
    void insertDessert(Dessert dessert);

    @Update
    void updateDessert(Dessert dessert);

    @Delete
    void delete(Dessert dessert);
}
