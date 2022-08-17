package com.github.kawakitan.mqtt.load.tools.interfaces.web.controller.entity;

import lombok.Data;

/**
 * 結果レスポンス.
 *
 * @author kawakitan
 */
@Data
public class ResultResponse {

    /** 結果. */
    private boolean result;

    /**
     * 正常.
     *
     * @return 正常レスポンス.
     */
    public static ResultResponse success() {
        final ResultResponse dst = new ResultResponse();
        dst.setResult(true);
        return dst;
    }
}
