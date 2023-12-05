package com.example.conectamvil;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import android.content.Context;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTManager {

    //mqtt://:@
    private static final String SERVER_URI = "tcp://mensajemqtt.cloud.shiftr.io:1883";
    private static final String CLIENT_ID = "mensajemqtt";
    private static final String TOPIC = "KvJkqEh3DBlDBqWm"; // Cambia esto por tu tema en shiftr.io

    private MqttClient client;

    public MQTTManager() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(SERVER_URI, CLIENT_ID, persistence);
            connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        try {
            client.connect(connOpts);
            subscribeToTopic();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToTopic() {
        try {
            client.subscribe(TOPIC, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    // Manejar el mensaje recibido
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            client.publish(TOPIC, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
