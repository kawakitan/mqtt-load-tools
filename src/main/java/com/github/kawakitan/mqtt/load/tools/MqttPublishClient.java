package com.github.kawakitan.mqtt.load.tools;

import java.util.Random;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * MQTTパブリッシュクライアント.
 *
 * @author kawakitan
 */
@Slf4j
public class MqttPublishClient {

    private final Broker broker;
    private final String topic;
    private final String clientId;

    private Thread thread;
    private boolean stopReqFlag;

    private long interval;
    private long tsStart;
    private long tsNext;

    private byte[] data;

    public MqttPublishClient(
            final Broker broker,
            final String topic,
            final String clientId) {
        this.broker = broker;
        this.topic = topic;
        this.clientId = clientId;
    }

    @Data
    private static class PublishInfo {
        private long start;
        private long end;
    }

    private void loop(final IMqttAsyncClient client) throws MqttException {
        PublishInfo info = new PublishInfo();
        info.setStart(System.currentTimeMillis());

        final MqttMessage message = new MqttMessage(data);
        message.setQos(2);

        // log.info("Publish start.");
        final IMqttDeliveryToken token = client.publish(topic, message, info, new IMqttActionListener() {

            @Override
            public void onSuccess(final IMqttToken asyncActionToken) {
                PublishInfo info = (PublishInfo) asyncActionToken.getUserContext();
                info.setEnd(System.currentTimeMillis());
                log.info("Publish success. [{} ms]", (info.end - info.start));
            }

            @Override
            public void onFailure(final IMqttToken asyncActionToken, final Throwable exception) {
                log.error("Publish failure.", exception);
            }
        });
    }

    public synchronized void start(final long interval, final int dataLength) {
        if (null != thread) {
            return;
        }

        this.interval = interval;
        data = new byte[dataLength];
        Random r = new Random(System.currentTimeMillis());
        r.nextBytes(data);

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

                    tsStart = System.currentTimeMillis();
                    tsNext = tsStart;
                    while (true) {
                        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        loop(client);
                        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        if (stopReqFlag) {
                            break;
                        }

                        tsNext += interval;
                        final long tsNow = System.currentTimeMillis();
                        final long in = tsNext - tsNow;

                        if (0 > in) {
                            log.warn("Over time.");
                        } else {
                            // log.info("Sleep. {} - {} = {}", tsNext, tsNow, in);
                            Thread.sleep(in);
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
