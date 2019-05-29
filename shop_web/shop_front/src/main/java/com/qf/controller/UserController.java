package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.IUserService;
import com.qf.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @data 5/29/2019 16:16
 * @user yingyunzhizi
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Reference
    private IUserService userService;

    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/registerUser")
    public String registerUser(User user){

        User userRegister = userService.insertUser(user);
        System.out.println(userRegister);
        return "login";
    }
}
