package com.qf;

import com.qf.entity.User;

public interface IUserService {

    User insertUser(User user);

    User getUserByUsername(String username);
}
