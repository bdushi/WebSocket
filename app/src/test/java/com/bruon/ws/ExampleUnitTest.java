package com.bruon.ws;

import android.util.Log;

import com.craftsman.websockets.Ws;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void ws() {
        Ws ws = new Ws.Builder().from( "ws://http://ozzir.isnalbania.com");
        try {
            ws.connect();
            ws.on("ride.42.accept", new Ws.WsListner() {
                @Override
                public void onEvent(String eventUri, Object data) {
                    if(data != null)
                        Log.d(MainActivity.class.getName(), data.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}