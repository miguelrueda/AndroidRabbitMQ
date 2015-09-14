package com.example.services;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class NotificationService {

    private static final String TAG = NotificationService.class.getSimpleName();
    private String broker = "tcp://192.168.8.141:1883";
    private int qos = 0;
    private MqttCallback mqttCallback;
    private Context context;
    private MqttClient mqttClient = null;
    private MemoryPersistence memoryPersistence = new MemoryPersistence();


    public NotificationService(Context context, MqttCallback mqttCallback) {
        this.context = context;
        this.mqttCallback = mqttCallback;
    }

    public void start() {
        try {
            Log.i(TAG, "start()");
            mqttClient = new MqttClient(broker, MqttClient.generateClientId(), memoryPersistence);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName("testuser");
            mqttConnectOptions.setPassword("testuser".toCharArray());
            mqttConnectOptions.setCleanSession(true);

            mqttClient.setCallback(mqttCallback);
            mqttClient.connect(mqttConnectOptions);
            if (!mqttClient.isConnected()) {
                Log.e(TAG, "start() - Error connecting on notification server:");
                return;
            }
            mqttClient.subscribe("pahoqueue", qos);
            Log.i(TAG, "start() - Connected");
        } catch (MqttException e) {
            Log.e(TAG, "start() - Error while creating mqtt client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            Log.e(TAG, "disconnect() - Error while disconnecting mqtt client: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
