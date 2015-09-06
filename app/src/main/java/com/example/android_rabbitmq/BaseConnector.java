package com.example.android_rabbitmq;

import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class BaseConnector {

    private final static String TAG = BaseConnector.class.getSimpleName();
    public String server;
    public String exchange;
    protected Channel channel = null;
    protected Connection connection;
    protected boolean running;
    protected String exchangeType;

    public BaseConnector(String server, String exchange, String exchangeType) {
        this.server = server;
        this.exchange = exchange;
        this.exchangeType = exchangeType;
    }

    public void dispose() {
        running = false;
        try {
            if (connection != null) {
                connection.close();
            }
            if (channel != null) {
                channel.abort();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean connectToRabbit() {
        if (channel != null && channel.isOpen()) {
            return true;
        }
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(server);
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(exchange, exchangeType, true);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
}