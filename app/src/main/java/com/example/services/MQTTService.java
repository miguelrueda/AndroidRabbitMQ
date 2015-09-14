package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService extends Service implements MqttCallback {

    private static final String TAG = MQTTService.class.getSimpleName();
    private NotificationService notificationService = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand() - Notification service started...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MQTTService.this.notificationService = new
                        NotificationService(MQTTService.this.getApplicationContext(), MQTTService.this);
            }
        }).start();
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.notificationService.disconnect();
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Intent intent = new Intent("mqtt-message");
        intent.putExtra("message", mqttMessage.toString());
        LocalBroadcastManager.getInstance(MQTTService.this.getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
