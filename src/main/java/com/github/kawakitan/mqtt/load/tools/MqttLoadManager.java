package com.github.kawakitan.mqtt.load.tools;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttLoadManager {

    private MqttConfig config;

    private final MqttPublishClient publish;
    private final MqttSubscribeClient subscribe;

    public MqttLoadManager(
            final MqttConfig config) {
        this.config = config;

        final UUID uuid = UUID.randomUUID();
        final String clientId = uuid.toString().replace("-", "");
        final String topic = String.format("%03d", 1);

        this.publish = new MqttPublishClient(
                this.config.getBroker(),
                topic,
                clientId + "-pub");

        this.subscribe = new MqttSubscribeClient(
                this.config.getBroker(),
                topic,
                clientId + "-sub");
    }

    public void init() {
        log.info("MQTT Load Manager init.");
    }

    public void destory() {
        log.info("MQTT Load Manager destory.");
        stop();
    }

    public void start() {
        subscribe.start();
        publish.start();
    }

    public void stop() {
        publish.stop();
        subscribe.stop();
    }

}
