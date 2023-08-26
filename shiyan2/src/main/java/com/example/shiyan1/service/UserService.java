package com.example.shiyan1.service;

import com.example.shiyan1.entity.User;

/**
 * @author word
 */
public interface UserService {
    public User checkLogin(String userName, String password);

    public void create(String userName, String password);

}
