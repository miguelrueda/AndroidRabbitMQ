package com.example.android_rabbitmq;

import android.os.Handler;
import android.util.Log;

import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class MessageConsumer extends BaseConnector {

    private static final String TAG = MessageConsumer.class.getSimpleName();
    private String queue;
    private QueueingConsumer queueingConsumer;
    private byte[] lastMessage;
    private Handler messageHandler = new Handler();
    private Handler consumeHandler = new Handler();

    public interface OnReceiveMessageHandler {
        public void onReceiveMessage(byte [] message);
    };

    private OnReceiveMessageHandler onReceiveMessageHandler;

    public MessageConsumer(String server, String exchange, String exchangeType) {
        super(server, exchange, exchangeType);
    }

    public void setOnReceiveMessageHandler(OnReceiveMessageHandler onReceiveMessageHandler) {
        this.onReceiveMessageHandler = onReceiveMessageHandler;
    }

    final Runnable returnMessage = new Runnable() {
        @Override
        public void run() {
            onReceiveMessageHandler.onReceiveMessage(lastMessage);
        }
    };

    final Runnable consumeRunner = new Runnable() {
        @Override
        public void run() {
            consume();
        }
    };

    @Override
    public boolean connectToRabbit() {
        if (super.connectToRabbit()) {
            try {
                queue = channel.queueDeclare().getQueue();
                queueingConsumer = new QueueingConsumer(channel);
                channel.basicConsume(queue, false, queueingConsumer);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
            if (exchangeType == "fanout") {
                addBinding("");
            }
            running = true;
            consumeHandler.post(consumeRunner);
            return true;
        }
        return false;
    }

    private void addBinding(String routingKey) {
        try {
            channel.queueBind(queue, exchange, routingKey);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void removeBinding(String routingKey) {
        try {
            channel.queueUnbind(queue, exchange, routingKey);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void consume() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (running) {
                    QueueingConsumer.Delivery delivery;
                    try {
                        delivery = queueingConsumer.nextDelivery();
                        lastMessage = delivery.getBody();
                        messageHandler.post(returnMessage);
                        try {
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        };
    }

    public void dispose() {
        running = false;
    }
}