package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.IUserService;
import com.qf.dao.IUserMapper;
import com.qf.entity.User;
import com.qf.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version 1.0
 * @data 6/4/2019 19:27
 * @user yingyunzhizi
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;
    
    @Override
    public int register(User user) {
        //1判断用户是否被注册了
        User user1 = this.queryUserByUsername(user.getUsername());
        if (user1 != null){
            //用户名已经被注册
            return -1;
        }
        
        //2判断邮箱是否已经注册
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email",user.getEmail());
        User user2 = userMapper.selectOne(queryWrapper);
        if (user2 != null){
            //邮箱已经被注册了
            return  -2;
        }
        
        //可以注册了,注册要对密码进行加密
        user.setPassword(MD5Util.content2MD5(user.getPassword()));

        return userMapper.insert(user);
    }

    @Override
    public User login(User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",user.getUsername());
        queryWrapper.eq("password",MD5Util.content2MD5(user.getPassword()));
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User queryUserByUsername(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int updatePassword(String username, String password) {
        User user = this.queryUserByUsername(username);
        user.setPassword(MD5Util.content2MD5(password));
        return userMapper.updateById(user);
    }
}
