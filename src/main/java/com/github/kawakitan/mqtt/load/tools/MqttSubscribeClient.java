package com.github.kawakitan.mqtt.load.tools;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * MQTTサブスクライブクライアント.
 *
 * @author kawakitan
 */
@Slf4j
public class MqttSubscribeClient {

    private final Broker broker;
    private final String topic;
    private final String clientId;

    private Thread thread;
    private boolean stopReqFlag;

    public MqttSubscribeClient(
            final Broker broker,
            final String topic,
            final String clientId) {
        this.broker = broker;
        this.topic = topic;
        this.clientId = clientId;
    }

    public void start() {
        if (null != thread) {
            return;
        }

        stopReqFlag = false;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MqttAsyncClient client = null;
                try {
                    client = new MqttAsyncClient(
                            broker.getServerUri(),
                            clientId,
                            new MemoryPersistence());

                    client.setCallback(new MqttCallback() {
                        @Override
                        public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                            log.debug("Subscribe.");
                        }

                        @Override
                        public void deliveryComplete(final IMqttDeliveryToken token) {
                        }

                        @Override
                        public void connectionLost(final Throwable cause) {
                        }
                    });

                    final MqttConnectOptions opts = new MqttConnectOptions();
                    opts.setCleanSession(true);

                    client.connect(opts);

                    while (true) {
                        Thread.sleep(500);
                        if (client.isConnected()) {
                            break;
                        }
                        log.debug("Connect wait.");
                    }

                    client.subscribe(topic, 2);

                    while (true) {
                        final long tsStart = System.currentTimeMillis();
                        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        if (stopReqFlag) {
                            break;
                        }
                        final long tsEnd = System.currentTimeMillis();
                        final long interval = 1000 - (tsEnd - tsStart);
                        if (0 > interval) {
                            log.warn("Over time.");
                        } else {
                            Thread.sleep(interval);
                        }
                    }
                } catch (MqttException ex) {
                    log.error("", ex);
                } catch (InterruptedException ex) {
                    log.error("", ex);
                } finally {
                    if (null != client) {
                        try {
                            client.disconnect();
                        } catch (MqttException ex) {
                            log.warn("", ex);
                        }
                        try {
                            client.close();
                        } catch (MqttException ex) {
                            log.warn("", ex);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    public void stop() {
        stopReqFlag = true;
        try {
            while (thread.isAlive()) {
                log.debug("stop wait...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            log.error("", ex);
        }
        thread = null;
    }
}
