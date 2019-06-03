package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.IUserService;
import com.qf.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        //先进行加密,再存入数据库
        String algorithmName = "MD5";// 加密算法
        Object source = user.getPassword();//被加密的密码
        Object salt = ByteSource.Util.bytes(user.getUsername());//颜值
        int hashIterations = 1024; // 加密次数
        SimpleHash simpleHash = new SimpleHash(algorithmName, source, salt, hashIterations);
        user.setPassword(simpleHash.toString());

        User userRegister = userService.insertUser(user);
        System.out.println(userRegister);
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, ModelMap map){
        Subject subject = SecurityUtils.getSubject();

        //1.判断用户是否登录
        if (!subject.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            try {
                subject.login(token);
            }catch (AuthenticationException e) {
                System.err.println("认证失败");
                return "login";
            }
        }

        System.err.println("认证成功");
        return "index";
    }

    @RequestMapping(value = "/checkByUsername", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String checkByUsername(String username){
        User user = userService.getUserByUsername(username);
        if (user==null){
            return "√";
        }
        return "这个用户已经存在";
    }
}
