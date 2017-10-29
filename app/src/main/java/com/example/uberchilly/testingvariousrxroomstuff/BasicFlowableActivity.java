package com.example.uberchilly.testingvariousrxroomstuff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * Created by uberchilly on 29-Oct-17.
 */

public class BasicFlowableActivity extends AppCompatActivity {
    public static final String TAG = "BasicFlowableActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        basicFlowableCreating();

//        creatingObservable();

//        creatingFlowable();

//        multipleSubscribeToSameFlowable();


//        hotPublishers();

        //ConnectableObservable / ConnectableFlowable and resource sharing
        connectableObserablesOrFlowables();
    }

    private void connectableObserablesOrFlowables() {
        //There are cases when we want to share a single subscription between subscribers, meaning while the code
        // that executes on subscribing should be executed once, the events should be published to all subscribers.
        // For ex. when we want to share a connection between multiple Observables / Flowables.
        // Using a plain Observable would just re-execute the code inside .create() and opening / closing a new connection
        // for each new subscriber when it subscribes / cancels its subscription.
        //ConnectableObservable are a special kind of Observable. No matter how many Subscribers subscribe to ConnectableObservable,
        // it opens just one subscription to the Observable from which it was created.

        //Anyone who subscribes to ConnectableObservable is placed in a set of Subscribers(it doesn't trigger the .create()
        // code a normal Observable would when .subscribe() is called). A .connect() method is available for ConnectableObservable.
        // As long as connect() is not called, these Subscribers are put on hold, they never directly subscribe to upstream Observable
        ConnectableObservable<Integer> connectableObservable =
                Observable.<Integer>create(subscriber -> {
                    Log.d(TAG, "Inside create()");
                    /* A JMS connection listener example
                         Just an example of a costly operation that is better to be shared **/
                    subscriber.setCancellable(() -> Log.d(TAG, "Subscription cancelled"));

                    Log.d(TAG, "Emitting 1");
                    subscriber.onNext(1);

                    Log.d(TAG, "Emitting 2");
                    subscriber.onNext(2);

                    subscriber.onComplete();
                }).publish();
        //can do //publish().refCount() to make it do create only for first subscriber but emmit for every other
        //same can be done with .share() <=> publish().refCount()
        //this is useful to avoid manual call to connect() which starts everything


        connectableObservable
                .take(1)
                .subscribe((val) -> Log.d(TAG, "Subscriber 1 received: " + val),
                        err -> Log.d(TAG, "onError 1"), () -> Log.d(TAG, "onCompleted 1"));

        connectableObservable
                .subscribe((val) -> Log.d(TAG, "Subscriber 2 received: " + val),
                        err -> Log.d(TAG, "onError2"), () -> Log.d(TAG, "onCompleted 2"));

        Log.d(TAG, "Now connecting to the ConnectableObservable");
        connectableObservable.connect(); //First this must be called for it to start, before that every sub is in queue chilling

    }

    private void hotPublishers() {
        //ReplaySubject keeps a buffer of events that it 'replays' to each new subscriber,
        //first he receives a batch of missed and only later events in real-time.
        //createWithSize(50) will probably great new subscriber with 50 missed events
        Subject<Integer> subject = ReplaySubject.createWithSize(50);

        Log.d(TAG, "Pushing 0");
        subject.onNext(0);
        Log.d(TAG, "Pushing 1");
        subject.onNext(1);


        Log.d(TAG, "Subscribing 1st");
        subject.subscribe(val -> Log.d(TAG, "Subscriber1 received " + val),
                err -> {
                    Log.d(TAG, err.getLocalizedMessage());
                }, () -> {
                    Log.d(TAG, "completed() 1");
                });

        Log.d(TAG, "Pushing 2");
        subject.onNext(2);

        Log.d(TAG, "Subscribing 2nd");
        subject.subscribe(val -> Log.d(TAG, "Subscriber2 received " + val),
                err -> {
                    Log.d(TAG, err.getLocalizedMessage());
                }, () -> {
                    Log.d(TAG, "completed() 2");
                });

        Log.d(TAG, "Pushing 3");
        subject.onNext(3);

        subject.onComplete();
    }


    private void multipleSubscribeToSameFlowable() {
        Log.d(TAG, "=======================");
        Flowable<Integer> createdFlowable = createFlowable();
        createdFlowable
                .subscribe(val -> Log.d(TAG, "onNext: " + val),
                        err -> Log.d(TAG, "onError: " + err.getLocalizedMessage()),
                        () -> Log.d(TAG, "onComplete"));
        //this illustrates subscribing again to the same flowable.Every subscriber gets every event (COLD)
        createdFlowable
                .subscribe(val -> Log.d(TAG, "onNext: " + val),
                        err -> Log.d(TAG, "onError: " + err.getLocalizedMessage()),
                        () -> Log.d(TAG, "onComplete"));
    }

    private void creatingFlowable() {
        //testing creating flowable
        Flowable<Integer> createdFlowable = createFlowable();
        createdFlowable
                .subscribe(val -> Log.d(TAG, "onNext: " + val),
                        err -> Log.d(TAG, "onError: " + err.getLocalizedMessage()),
                        () -> Log.d(TAG, "onComplete"));
    }

    private void creatingObservable() {
        //testing creating observable
        createObservable()
                .subscribe(val -> Log.d(TAG, "onNext: " + val),
                        err -> Log.d(TAG, "onError: " + err.getLocalizedMessage()),
                        () -> Log.d(TAG, "onComplete"));
    }

    private void basicFlowableCreating() {
        Flowable<Integer> flowable1 = Flowable.just(1, 2, 3);
        Flowable<Integer> flowable2 = Flowable.range(1, 10);
        Flowable<String> flowable3 = Flowable.fromArray(new String[]{"red", "green", "blue"});

        //        Flowable<String> flowable4 = Flowable.fromIterable(List.of("red", "green", "blue"));

        //test with flowable1, flowable2, flowable3, radi subscribe i subscribeWith
//        flowable2.subscribe(new DisposableSubscriber<Integer>() {
//            @Override
//            public void onNext(Integer val) {
//                Log.d(TAG, "onNext: " + val);
//            }
//
//            @Override
//            public void onError(Throwable err) {
//                Log.d(TAG, "onError: " + err.getLocalizedMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete");
//            }
//        });
        //this will work with java 8 if you can set it up
//        flowable2.subscribe(val -> {
//                    Log.d(TAG, "onNext: " + val);
//                },
//                err -> {
//                    Log.d(TAG, "onError: " + err.getLocalizedMessage());
//                },
//                () -> {
//                    Log.d(TAG, "onComplete");
//                });
//        flowable3.subscribeWith(new DisposableSubscriber<String>() {
//            @Override
//            public void onNext(String integer) {
//                Log.d(TAG, "onNext: " + integer);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.d(TAG, "onError: " + t.getLocalizedMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete");
//            }
//        });

    }

    private Observable<Integer> createObservable() {
        //Streams are lazy meaning that the code inside create() doesn't get executed without subscribing to the stream.
        //U prevodu samo kad se subscribe-uje neko krece da se izvrasava kod
        //When subscribing to an Observable/Flowable,
        // the create() method gets executed for each Subscriber, the events inside create(..)
        // are re-emitted to each subscriber independently.
        //So every subscriber will get the same events and will not lose any events - this behavior is named 'cold observable'
        //See Hot Publishers to understand sharing a subscription and multicasting events.
        return Observable.create(subscriber -> {
            Log.d(TAG, "Started emitting");

            Log.d(TAG, "Emitting 1st");
            subscriber.onNext(1);

            Log.d(TAG, "Emitting 2nd");
            subscriber.onNext(2);

            subscriber.onComplete();
        });
    }

    private Flowable<Integer> createFlowable() {
        //Flowable version same Observable but with a BackpressureStrategy that will be discussed separately.
        return Flowable.create(subscriber -> {
            Log.d(TAG, "Started emitting");

            Log.d(TAG, "Emitting 1st");
            subscriber.onNext(1);

            Log.d(TAG, "Emitting 2nd");
            subscriber.onNext(2);

            subscriber.onComplete();
        }, BackpressureStrategy.MISSING); //missing, buffer, drop, error (research this options)
    }
}
