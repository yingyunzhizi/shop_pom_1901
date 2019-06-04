package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.IUserService;
import com.qf.entity.User;
import com.qf.util.Base64Util;
import com.qf.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @data 6/4/2019 17:13
 * @user yingyunzhizi
 */
@Controller
@RequestMapping("/sso")
public class SSoController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;

    /**
     * 注册用户
     * @param user
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("/registerUser")
    public String registerUser(User user, String code, Model model){

        //校验验证码,去redis中
        String sCode = (String) redisTemplate.opsForValue().get(user.getEmail() + "_code");
        if (sCode==null || !sCode.equals(code)){
            model.addAttribute("error",1);
            return "register";
        }

        //验证码通过
        //删除验证码
        redisTemplate.delete(user.getEmail()+"_code");

        //通过dubbo调用服务注册
        int register = userService.register(user);
        if(register == -1){
            //用户名已经存在
            model.addAttribute("error", 2);
            return "register";
        } else if(register == -2){
            //邮箱已经存在
            model.addAttribute("error", 3);
            return "register";
        }
        return "redirect:/baseSSO/login";
    }

    /**
     * 跳转到修改密码页面
     * @param username
     * @param token
     * @param model
     * @return
     */
    @RequestMapping("/toUpdatePassword")
    public String toUpdatePassword(String username,String token,Model model){
        //首先把编码过的用户名复原
        String name = Base64Util.decodingBase64(username);

        model.addAttribute("username",name);
        model.addAttribute("token",token);
        return "updatePassword";
    }

    /**
     * 修改密码
     * @param username
     * @param password
     * @param token
     * @param model
     * @return
     */
    @RequestMapping("/updatePassword")
    public String updatePassword(String username,String password,String token,Model model){

        //在这里校验修改密码的有效性
        String myToken = (String) redisTemplate.opsForValue().get(username + "_forget_token");
        if (myToken!=null&&myToken.equals(token)){
            //校验通过,调用服务修改密码
            int i = userService.updatePassword(username, password);
            redisTemplate.delete(username + "_forget_token");
            return "succ";
        }

        //校验未通过,就跳转到error页面,显示信息
        model.addAttribute("error","是一个失效或者非法的字符");
        return "error";
    }

    /**
     * 注册时候的发送邮件
     * @param email
     * @return
     */
    @RequestMapping("/sendEmail")
    @ResponseBody
    public String sendEmail(String email){
        //生成验证码
        int code = CodeUtil.createRandomCode();

        //将code保存到redis中
        redisTemplate.opsForValue().set(email+"_code",code+"");
        redisTemplate.expire(email+"_code",5, TimeUnit.MINUTES);

        //创建一封邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //设置标题
            mimeMessageHelper.setSubject("来自上海青苹果网络科技有限公司");
            //设置发送者
            mimeMessageHelper.setFrom("2675066859@qq.com");
            //设置接收者
            mimeMessageHelper.setTo(email);
            //设置邮件内容
            mimeMessageHelper.setText("<a href='www.baidu.com'>验证码为:</a>"+code,true);
            //设置附件
            mimeMessageHelper.addAttachment("附件.jpg", new File("C:\\Users\\26750\\Pictures\\Saved Pictures\\2.jpg"));
            //发送邮件
            javaMailSender.send(mimeMessage);

            return "succ";
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "error";
    }

    /**
     * 登录认证,并且把token放入浏览器的cookie中
     * @param user
     * @param model
     * @param response
     * @return
     */
    @RequestMapping("login")
    public String login(User user, Model model, HttpServletResponse response){
        //调用服务进行登录认证
        User loginUser = userService.login(user);
        if (loginUser==null){
            //用户名或者密码错误
            model.addAttribute("error","用户名或者密码错误");
            return "login";
        }

        //登录成功之后
        String loginToken = UUID.randomUUID().toString();
        //token作为key,用户信息作为value存入redis中
        redisTemplate.opsForValue().set(loginToken,loginUser);
        redisTemplate.expire(loginToken,7,TimeUnit.DAYS);

        //放入用户浏览器的cookie中
        Cookie cookie = new Cookie("login_token", loginToken);
        cookie.setMaxAge(1*60*60*24*7);//设置最大超时时间
        cookie.setPath("/");//所有请求都会携带该cookie
        //        cookie.setDomain(".sb.com");//所有二级域名共享cookie， cookie不能一级域名共享
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return "redirect:http://localhost:8081";
    }

    /**
     * 发送忘记密码的邮件
     * @param username
     * @return
     */
    @RequestMapping("/sendForgetMail")
    @ResponseBody
    public Map<String,Object> sendForgetMail(String username){
        Map<String,Object> map = new HashMap<>();

        //用username查询用户信息
        User user = userService.queryUserByUsername(username);
        if (user==null){
            map.put("code",-1);//说明用户不存在
            return map;
        }

        //说明账号存在了
        String email = user.getEmail();
        int index = email.indexOf("@");
        String str = email.substring(3,index);//substring是含头bu含尾的
        String strEmail = email.replace(str,"*******");

        //给用户发送的邮件中是一个url,点击url可以调整到修改密码的页面
        //连接要保证时效性,一致性,独立性
        String toEmail = "http://email."+email.substring(index+1);

        //创建一封邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //设置标题
            mimeMessageHelper.setSubject("来自上海青苹果网络科技有限公司的修改密码邮件");
            //设置发送者
            mimeMessageHelper.setFrom("2675066859@qq.com");
            //设置接收者
            mimeMessageHelper.setTo(email);
            //设置邮件内容
            String uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(username+"_forget_token",uuid);
            redisTemplate.expire(username+"_forget_token",5,TimeUnit.MINUTES);
            //这里用户名其实很安全了,但是为了别人不能看到用户名,还是进行了编码加密
            String url = "http://localhost:8084/sso/toUpdatePassword?username=" + Base64Util.encodingBase64(user.getUsername()) + "&token=" + uuid;
            mimeMessageHelper.setText("亲爱的用户，需要修改密码请点击<a href='" + url + "'>这里</a>", true);
            //发送邮件
            javaMailSender.send(mimeMessage);

            //发送成功
            map.put("code", 0);
            map.put("strEmail", strEmail);
            map.put("toEmail", toEmail);
            return map;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        map.put("code", -2);
        return map;
    }

    /*@RequestMapping("/toUpdatePassword")
    public String toUpdatePassword(String username){
        return null;
    }*/
}
