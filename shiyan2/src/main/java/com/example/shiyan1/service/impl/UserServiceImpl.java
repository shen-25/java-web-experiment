package com.example.shiyan1.service.impl;

import com.example.shiyan1.command.UserCommand;
import com.example.shiyan1.common.Constant;
import com.example.shiyan1.entity.User;
import com.example.shiyan1.service.UserService;
import com.example.shiyan1.utils.DbUtils;
import com.example.shiyan1.utils.MD5Utils;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author word
 */
public class UserServiceImpl implements UserService {

    private UserCommand userCommand = new UserCommand();
    @Override
    public User checkLogin(String userName, String password){
        User user = userCommand.checkLogin(userName);
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        String  md5 = MD5Utils.md5Digest(password, Constant.salt);
        if (!user.getPassword().equals(md5)) {
            throw new RuntimeException("密码不正确");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void create(String userName, String password) {
        if (StringUtils.isNullOrEmpty(userName)) {
            throw new RuntimeException("用户名不能为空");
        }
        if (StringUtils.isNullOrEmpty(password)) {
            throw new RuntimeException("密码不能为空");
        }
        try {
            Integer ans = userCommand.checkRegister(userName);
            if (ans >= 1) {
                throw new RuntimeException("该用户已存在");
            } else{
                String md5 = MD5Utils.md5Digest(password, Constant.salt);
                int cnt = userCommand.create(userName, md5);
                if (cnt <= 0) {
                    throw new RuntimeException("用户创建失败");
                }
            }
        } catch (SQLException throwables) {
            throw new RuntimeException("sql语句有错");
        }

    }

    public static void main(String[] args){
        System.out.println(MD5Utils.md5Digest("123456", Constant.salt));

    }
}
