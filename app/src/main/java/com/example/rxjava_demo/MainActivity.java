package com.example.rxjava_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private TextView text;
    private static final  String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);

        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                //明定於ioThread需要操作的事情
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Throwable {
                        Log.d(TAG,"test: "+ Thread.currentThread().getName());
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.safeSubscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG,"onSubscribe: called.");
            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG,"onNext: "+ Thread.currentThread().getName());
                Log.d(TAG,"onNext: "+ task.getDescription());

//                try{
//                    Thread.sleep(1000);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"onError: "+ e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"onComplete: called.");
            }
        });

        /*
            RXJAVA3操作符詳解：https://blog.csdn.net/qingjuyashi/article/details/106220526
         */

    }
}