package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @data 5/20/2019 17:25
 * @user yingyunzhizi
 */
@Controller
@RequestMapping("/back")
public class BackController {

    /**
     * 让每个请求调到对应的页面去
     *
     * @param topage
     * @return
     */
    @RequestMapping("/{topage}")
    public String toPage(@PathVariable("topage") String topage) {
        return topage;
    }
}
