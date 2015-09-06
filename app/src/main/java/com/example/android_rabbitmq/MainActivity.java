package com.example.android_rabbitmq;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

/**
 * REFS
 * https://simonwdixon.wordpress.com/2011/06/03/getting-started-with-rabbitmq-on-android-part-1/
 * http://dalelane.co.uk/blog/?p=1599
 */

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MessageConsumer messageConsumer;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);
        messageConsumer = new MessageConsumer("192.168.1.XXX", "logs", "fanout");
        messageConsumer.connectToRabbit();
        messageConsumer.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {
            @Override
            public void onReceiveMessage(byte[] message) {
                String text = "";
                try {
                    text = new String(message, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.getMessage());
                }
                output.append("\n" + text);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onPause();
        messageConsumer.connectToRabbit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageConsumer.dispose();
    }

}