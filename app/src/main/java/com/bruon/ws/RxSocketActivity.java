package com.bruon.ws;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import kotlin.text.Charsets;
import moe.codeest.rxsocketclient.RxSocketClient;
import moe.codeest.rxsocketclient.SocketClient;
import moe.codeest.rxsocketclient.SocketSubscriber;
import moe.codeest.rxsocketclient.meta.SocketConfig;
import moe.codeest.rxsocketclient.meta.SocketOption;
import moe.codeest.rxsocketclient.meta.ThreadStrategy;

import static com.bruon.ws.Constants.IP;
import static com.bruon.ws.Constants.PORT;
import static com.bruon.ws.Constants.URL;
import static com.bruon.ws.Constants.URL_AUTH;

/**
 * https://github.com/codeestX/RxSocketClient
 */

public class RxSocketActivity extends AppCompatActivity {
    private final MutableLiveData<String> mData = new MutableLiveData<>();
    private final LiveData<String> data = mData;
    private final String TAG = RxSocketActivity.class.getName();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.text);
        data.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

        SocketClient mClient = RxSocketClient
                .create(new SocketConfig.Builder()
                        .setIp(URL)
                        .setPort(PORT)
                        .setCharset(Charsets.UTF_8)
                        .setThreadStrategy(ThreadStrategy.ASYNC)
                        .setTimeout(30 * 1000)
                        .build())
                .option(new SocketOption.Builder()
//                        .setHeartBeat(HEART_BEAT, 60 * 1000)
//                        .setHead(HEAD)
//                        .setTail(TAIL)
                        .build());
        compositeDisposable.add(
                mClient.connect()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SocketSubscriber() {
                            @Override
                            public void onConnected() {
                                //onConnected
                                Log.e(TAG, "onConnected");
                            }

                            @Override
                            public void onDisconnected() {
                                //onDisconnected
                                Log.e(TAG, "onDisconnected");
                            }

                            @Override
                            public void onResponse(@NotNull byte[] data) {
                                //receive data
                                Log.e(TAG, Arrays.toString(data));
                                mData.postValue(Arrays.toString(data));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                //onError
                                Log.e(TAG, throwable.toString());
                            }
                        })
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
}
