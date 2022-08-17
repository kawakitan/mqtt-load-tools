package com.github.kawakitan.mqtt.load.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーション.
 *
 * @author kawakitan
 */
@Slf4j
@SpringBootApplication
public class Application {

    /**
     * メイン関数.
     *
     * @param args 引数
     */
    public static void main(final String[] args) {
        log.info("Application starting.");
        SpringApplication.run(Application.class, args);
        log.info("Application started.");
    }

}
