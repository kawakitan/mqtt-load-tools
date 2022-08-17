package com.github.kawakitan.mqtt.load.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQTT負荷マネージャコンフィグ.
 *
 * @author kawakitan
 */
@Slf4j
@Configuration
public class MqttLoadManagerConfig {

    @Autowired
    private MqttConfig config;

    @Bean(initMethod = "init", destroyMethod = "destory")
    public MqttLoadManager mqttLoadManager() {
        final MqttLoadManager manager = new MqttLoadManager(config);
        return manager;
    }
}
