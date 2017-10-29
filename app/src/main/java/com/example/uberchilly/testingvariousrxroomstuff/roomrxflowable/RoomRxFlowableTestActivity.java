package com.example.uberchilly.testingvariousrxroomstuff.roomrxflowable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.uberchilly.testingvariousrxroomstuff.MyApplication;
import com.example.uberchilly.testingvariousrxroomstuff.R;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RoomRxFlowableTestActivity extends AppCompatActivity {

    private static final String TAG = "RoomRxFlowableTest";
    private List<Dessert> dessers;
    private int i = 0;
    private Disposable disposable;
    private DessertDao dessertsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_rx_flowable_activity);

        dessertsDao = ((MyApplication) getApplication()).getDatabase().dessertDao();

        subscribeAgain();

        Button insert = findViewById(R.id.insert_dessert_btn);
        insert.setOnClickListener(view -> {

            Completable.<Boolean>create(subscriber -> {

                Dessert dessert1 = new Dessert(i, "Dessert" + i);
                i++;
                dessertsDao.insertDessert(dessert1);
                subscriber.onComplete();
            })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Log.d(TAG, "insert finished ");
                    }, err -> {
                        Log.d(TAG, "insert error " + err.getLocalizedMessage());
                    });

        });
        Button delete = findViewById(R.id.delete_dessert_btn);
        delete.setOnClickListener(view -> {
            if (dessers != null && dessers.size() > 0) {
                Completable.<Boolean>create(subscriber -> {
                    dessertsDao.delete(dessers.get(0));
                    subscriber.onComplete();
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Log.d(TAG, "delete finished");
                        }, err -> {
                            Log.d(TAG, "delete error " + err.getLocalizedMessage());
                        });

            }
        });

        Button unsubscribe = findViewById(R.id.unsubscribe_btn);
        unsubscribe.setOnClickListener(view -> {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        });
        Button subscribeAgain = findViewById(R.id.subscribe_btn);
        subscribeAgain.setOnClickListener(view -> {
            subscribeAgain();
        });

    }

    private void subscribeAgain() {
        disposable = dessertsDao.getDessertsFlowable().subscribe(dessertList -> {
            StringBuilder items = new StringBuilder("");
            for (Dessert dessert : dessertList) {
                items.append(dessert.getName());
                items.append("->");
            }
            Log.d(TAG, "list success " + dessertList.size() + ": " + items);
            RoomRxFlowableTestActivity.this.dessers = dessertList;
        }, err -> {
            Log.d(TAG, "list onError" + err.getLocalizedMessage());
        }, () -> {
            Log.d(TAG, "list onCompleted");
        });
    }
}
