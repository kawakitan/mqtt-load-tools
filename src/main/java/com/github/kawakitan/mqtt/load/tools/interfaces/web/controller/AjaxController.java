package com.github.kawakitan.mqtt.load.tools.interfaces.web.controller;

import com.github.kawakitan.mqtt.load.tools.MqttLoadManager;
import com.github.kawakitan.mqtt.load.tools.interfaces.web.controller.entity.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/ajax")
public class AjaxController {

    @Autowired
    private MqttLoadManager manager;

    @PostMapping("/start")
    public ResultResponse start() {
        log.info("Start");
        manager.start();

        return ResultResponse.success();
    }

    @PostMapping("/stop")
    public ResultResponse stop() {
        log.info("Stop");
        manager.stop();

        return ResultResponse.success();
    }
}
