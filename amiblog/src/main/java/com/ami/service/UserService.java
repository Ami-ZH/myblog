package com.ami.service;

import com.ami.pojo.User;

public interface UserService {

    User checkUser(String username, String password);

}
