package com.github.kawakitan.mqtt.load.tools;

import lombok.Data;

@Data
public class Broker {

    private String host;
    private Integer port;

    public String getServerUri() {
        return String.format("tcp://%s:%d", host, port);
    }
}
