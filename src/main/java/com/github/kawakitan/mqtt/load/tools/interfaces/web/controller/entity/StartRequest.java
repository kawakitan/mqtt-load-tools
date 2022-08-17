package com.github.kawakitan.mqtt.load.tools.interfaces.web.controller.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 開始リクエスト.
 *
 * @author kawakitan
 */
@Slf4j
@Data
public class StartRequest {

    private int thread;
    private long interval;
    private String length;

    public Integer getLengthToInteger() {
        final Pattern ptn = Pattern.compile("^\\s*([0-9]+)\\s*([km]{0,1})$");
        final Matcher m = ptn.matcher(length);
        if (m.find()) {
            String value = m.group(1);
            String unit = m.group(2);

            try {
                int v = Integer.parseInt(value);
                if ("k".equals(unit)) {
                    v *= 1024;
                } else if ("m".equals(unit)) {
                    v *= 1024 ^ 2;
                }
                log.info("Data Length: {}", v);
                return v;
            } catch (NumberFormatException ex) {
                return null;
            }
        } else {
            log.error("Unmatch [{} != {}]", length, ptn.pattern());
            return null;
        }
    }
}
