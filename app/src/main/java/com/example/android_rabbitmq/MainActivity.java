package com.example.android_rabbitmq;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
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

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * REFS
 * https://simonwdixon.wordpress.com/2011/06/03/getting-started-with-rabbitmq-on-android-part-1/
 * http://dalelane.co.uk/blog/?p=1599
 * http://www.programacionj2ee.com/conectando-android-rabbitmq-eclipse-paho/
 * http://www.infoq.com/articles/practical-mqtt-with-paho
 * https://gist.github.com/m2mIO-gister/5600366
 * https://cloudplugs.com/developer-guide/mqtt-api/java-example/
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView output, responseView;
    private Button sendButton;
    private BroadcastReceiver broadcastReceiver;

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

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = (String) intent.getSerializableExtra("message");
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(2000);
                Log.i(TAG, "onCreate() - Message received: " + message);
                responseView.setText("Message: " + message);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("mqtt-message"));

    }

    private String createDummyMessage() throws JsonProcessingException {
        OperationRequest operationRequest = new OperationRequest();

        ECHOHeader echoHeader = new ECHOHeader();
        echoHeader.setOperatorName("ECHO_ANDROID_USER");
        echoHeader.setTerminalURI("192.168.8.141$T0163485");
        ECHOBody echoBody = new ECHOBody();
        echoBody.setResourceKey("123456789");
        echoBody.setResourceStatusKey(26);

        operationRequest.setEchoBody(echoBody);
        operationRequest.setEchoHeader(echoHeader);
        operationRequest.setOperationType(OperationType.CHANGE_STATUS);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOperationRequest = objectMapper.writeValueAsString(operationRequest);
        return jsonOperationRequest;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendButton) {
            Log.i(TAG, "onClick() - " + v.getId());
            //responseView.append(" Send message");
            /*
            Intent intent = new Intent("mqtt-message");
            try {
                intent.putExtra("message", createDummyMessage());
            } catch (JsonProcessingException e) {
                Log.e(TAG, "onCLick() - Error while generating the message: " + e.getMessage());
                e.printStackTrace();
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            */
            String topic = "/data/pahoqueue";
            int qos = 0;
            String broker = "tcp://192.168.8.141:1883";
            try {
                MqttClient mqttClient = new MqttClient(broker, MqttClient.generateClientId());
                MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                mqttConnectOptions.setCleanSession(true);
                mqttConnectOptions.setUserName("testuser");
                mqttConnectOptions.setPassword("testuser".toCharArray());
                Log.i(TAG, "Connecting to broker: " + broker);
                mqttClient.connect(mqttConnectOptions);
                Log.i(TAG, "onClick() - Connected");
                String message = createDummyMessage();
                Log.i(TAG, "onClick() - Publish message: " + message);
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(qos);
                mqttClient.publish(topic, mqttMessage);
                Log.i(TAG, "onClick() - Message published");
                mqttClient.disconnect();
                Log.i(TAG, "onClick() - Disconnect");
            } catch (MqttException e) {
                Log.e(TAG, "onClick() - Error while sending message: " + e.getMessage());
                e.printStackTrace();
                Log.e(TAG, "onClick() - Reason: " + e.getReasonCode());
                Log.e(TAG, "onClick() - Localized Message: " + e.getLocalizedMessage());
                Log.e(TAG, "onClick() - Cause: " + e.getCause());
            } catch (JsonProcessingException e) {
                Log.e(TAG, "onClick() - Error while creating message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}