package com.example.android_rabbitmq;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pojo.ECHOBody;
import com.example.pojo.ECHOHeader;
import com.example.pojo.OperationRequest;
import com.example.pojo.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REFS
 * https://simonwdixon.wordpress.com/2011/06/03/getting-started-with-rabbitmq-on-android-part-1/
 * http://dalelane.co.uk/blog/?p=1599
 * http://www.programacionj2ee.com/conectando-android-rabbitmq-eclipse-paho/
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView output, responseView;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);
        responseView = (TextView) findViewById(R.id.responseView);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        try {
            String json = createDummyMessage();
            output.setText(json);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "onCreate() - Error while generating message: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private String createDummyMessage() throws JsonProcessingException {
        OperationRequest operationRequest = new OperationRequest();

        ECHOHeader echoHeader = new ECHOHeader();
        echoHeader.setOperatorName("ECHO_ANDROID_USER");
        echoHeader.setTerminalURI("192.168.8.141$T0163485");
        ECHOBody echoBody = new ECHOBody();
        echoBody.setResourceKey("123456789");
        echoBody.setResourceStatusKey(26);

        operationRequest.setBody(echoBody);
        operationRequest.setHeader(echoHeader);
        operationRequest.setOperationType(OperationType.CHANGE_STATUS);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOperationRequest = objectMapper.writeValueAsString(operationRequest);
        return jsonOperationRequest;
    }

    @Override
    protected void onResume() {
        super.onPause();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendButton){
            Log.i(TAG, "onClick() - " + v.getId());
            responseView.append(" Send message");
        }
    }
}