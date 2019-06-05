package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @data 6/5/2019 21:15
 * @user yingyunzhizi
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    /**
     * 添加购物车
     * @param gid
     * @param gnumber
     * @param user
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/addCart")
    public String addCart(int gid, int gnumber, User user){
        System.out.println("添加购物车的商品：" + gid + " 购买数量为：" + gnumber);
        System.out.println("当前是否登录：" + user);
        return "succ";
    }

}
