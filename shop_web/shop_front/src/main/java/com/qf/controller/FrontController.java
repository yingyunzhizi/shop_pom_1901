package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @data 5/29/2019 15:23
 * @user yingyunzhizi
 */
@Controller
@RequestMapping("/front")
public class FrontController {

    /**
     * 让每个请求调到对应的页面去
     * @param topage
     * @return
     */
    @RequestMapping("/{topage}")
    public String toPage(@PathVariable("topage") String topage){
        return topage;
    }
}
