package com.github.kawakitan.mqtt.load.tools;

import lombok.Data;

@Data
public class LoadConfig {

    private int thread;
    private long interval;
    private int length;
}
