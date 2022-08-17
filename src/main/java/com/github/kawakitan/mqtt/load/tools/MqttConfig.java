package com.github.kawakitan.mqtt.load.tools;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {

    private Broker broker;

}
