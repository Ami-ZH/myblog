package com.ami.service.impl;

import com.ami.dao.UserDao;
import com.ami.pojo.User;
import com.ami.service.UserService;
import com.ami.uitls.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User user = userDao.findUser(username, MD5Utils.code(password));
        return user;
    }
}
