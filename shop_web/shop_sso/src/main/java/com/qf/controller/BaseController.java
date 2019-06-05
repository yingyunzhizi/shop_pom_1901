package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @data 6/4/2019 23:55
 * @user yingyunzhizi
 */
@Controller
@RequestMapping("/baseSSO")
public class BaseController {

    @RequestMapping("/{topage}")
    public String toPage(@PathVariable("topage") String topage) {
        return topage;
    }
}
