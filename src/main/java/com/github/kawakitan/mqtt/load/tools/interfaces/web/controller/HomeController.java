package com.github.kawakitan.mqtt.load.tools.interfaces.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * ホームコントローラ.
 *
 * @author kawakitan
 */
@Controller
public class HomeController {

    @GetMapping
    public ModelAndView index(final ModelAndView mv) {
        mv.setViewName("index");
        return mv;
    }
}
