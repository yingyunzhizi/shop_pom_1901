package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.IUserService;
import com.qf.dao.IUserMapper;
import com.qf.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version 1.0
 * @data 5/29/2019 15:54
 * @user yingyunzhizi
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public User insertUser(User user) {
        int insert = userMapper.insert(user);
        return user;
    }
}
