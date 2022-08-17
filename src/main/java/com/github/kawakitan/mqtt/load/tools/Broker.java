package com.github.kawakitan.mqtt.load.tools;

import lombok.Data;

/**
 * ブローカ.
 *
 * @author kawakitan
 */
@Data
public class Broker {

    /** ホスト. */
    private String host;
    /** ポート番号. */
    private Integer port;

    public String getServerUri() {
        return String.format("tcp://%s:%d", host, port);
    }
}
