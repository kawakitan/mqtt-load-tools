package com.github.kawakitan.mqtt.load.tools.interfaces.web.controller;

import com.github.kawakitan.mqtt.load.tools.LoadConfig;
import com.github.kawakitan.mqtt.load.tools.MqttLoadManager;
import com.github.kawakitan.mqtt.load.tools.interfaces.web.controller.entity.ResultResponse;
import com.github.kawakitan.mqtt.load.tools.interfaces.web.controller.entity.StartRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ajaxコントローラ.
 *
 * @author kawakitan
 */
@Slf4j
@RestController
@RequestMapping(path = "/ajax")
public class AjaxController {

    @Autowired
    private MqttLoadManager manager;

    @PostMapping("/start")
    public ResultResponse start(
            @RequestBody final StartRequest request) {
        final LoadConfig config = new LoadConfig();
        config.setThread(request.getThread());
        config.setInterval(request.getInterval());
        config.setLength(request.getLengthToInteger());

        log.info("Start");
        manager.start(config);

        return ResultResponse.success();
    }

    @PostMapping("/stop")
    public ResultResponse stop() {
        log.info("Stop");
        manager.stop();

        return ResultResponse.success();
    }
}
