package com.github.kawakitan.mqtt.load.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * MQTT負荷マネージャ.
 *
 * @author kawakitan
 */
@Slf4j
public class MqttLoadManager {

    private MqttConfig config;

    private final List<MqttPublishClient> publishList;
    private final List<MqttSubscribeClient> subscribeList;

    public MqttLoadManager(
            final MqttConfig config) {
        this.config = config;

        publishList = new ArrayList<MqttPublishClient>();
        subscribeList = new ArrayList<MqttSubscribeClient>();
    }

    public void init() {
        log.info("MQTT Load Manager init.");
        publishList.clear();
        subscribeList.clear();
    }

    public void destory() {
        log.info("MQTT Load Manager destory.");
        stop();
    }

    public void start(final LoadConfig config) {
        if (0 < publishList.size()) {
            log.warn("Publish list size. [{}]", publishList.size());
            publishList.clear();
        }
        if (0 < subscribeList.size()) {
            log.warn("Subscribe list size. [{}]", subscribeList.size());
            subscribeList.clear();
        }
        log.info("Starting...");

        for (int i = 0; i < config.getThread(); i++) {
            final String uuid = UUID.randomUUID().toString().replace("-", "");
            final String clientId = String.format("%s-%03d", uuid, (i + 1));
            final String topic = String.format("test%03d", (i + 1));

            final MqttPublishClient publish = new MqttPublishClient(
                    this.config.getBroker(),
                    topic,
                    clientId + "-pub");
            publishList.add(publish);

            final MqttSubscribeClient subscribe = new MqttSubscribeClient(
                    this.config.getBroker(),
                    topic,
                    clientId + "-sub");
            subscribeList.add(subscribe);
        }

        for (MqttSubscribeClient client : subscribeList) {
            client.start();
        }
        for (MqttPublishClient client : publishList) {
            client.start(config.getInterval(), config.getLength());
        }
        log.info("Started.");
    }

    public void stop() {
        log.info("Stopping...");
        for (MqttPublishClient client : publishList) {
            client.stop();
        }
        publishList.clear();
        for (MqttSubscribeClient client : subscribeList) {
            client.stop();
        }
        subscribeList.clear();
        log.info("Stoped.");
    }

}
