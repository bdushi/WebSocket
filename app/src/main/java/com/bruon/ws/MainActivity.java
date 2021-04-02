package com.bruon.ws;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.craftsman.websockets.Ws;

public class MainActivity extends AppCompatActivity {
    final String HOST_NAME = "ozzir.isnalbania.com:6001";
    final String CHAT_SERVER_URL = "http://socket-io-chat.now.sh/";
    final String TRAMP_SERVER_URL = "ws://rsocket-demo.herokuapp.com/ws";
    private final MutableLiveData<String>  mData = new MutableLiveData<>();
    private final LiveData<String> data = mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.text);
        data.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });
        Ws ws = new Ws.Builder().from(TRAMP_SERVER_URL);
        try {
            ws.connect();
            ws.on("ride.42.accept", new Ws.WsListner() {
                @Override
                public void onEvent(String eventUri, Object data) {
                    mData.postValue(eventUri);
                    if(data != null)
                        Log.d(MainActivity.class.getName(), data.toString());
                }
            }).on("trump", new Ws.WsListner() {
                @Override
                public void onEvent(String eventUri, Object data) {
                    mData.postValue(eventUri);
                    if(data != null)
                        Log.d(MainActivity.class.getName(), data.toString());
                }
            }).on("new message", new Ws.WsListner() {
                @Override
                public void onEvent(String eventUri, Object data) {
                    mData.postValue(eventUri);
                    if(data != null)
                        Log.d(MainActivity.class.getName(), data.toString());
                }
            });
        } catch (Exception e) {
            Log.d(MainActivity.class.getName(), e.getMessage());
        }
    }
}
