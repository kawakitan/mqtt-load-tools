package com.github.kawakitan.mqtt.load.tools;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTTコンフィグ.
 *
 * @author kawakitan
 */
@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {

    /** ブローカ. */
    private Broker broker;

}
