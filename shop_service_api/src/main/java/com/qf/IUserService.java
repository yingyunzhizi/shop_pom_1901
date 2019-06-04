package com.qf;

import com.qf.entity.User;

/**
 * @version 1.0
 * @data 6/4/2019 19:04
 * @user yingyunzhizi
 */
public interface IUserService {

    int register(User user);

    User login(User user);

    User queryUserByUsername(String username);

    int updatePassword(String username, String password);
}
