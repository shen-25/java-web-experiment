package com.example.shiyan1.service.impl;

import com.example.shiyan1.command.UserCommand;
import com.example.shiyan1.common.Constant;
import com.example.shiyan1.entity.User;
import com.example.shiyan1.service.UserService;
import com.example.shiyan1.utils.MD5Utils;
import com.mysql.cj.util.StringUtils;

/**
 * @author word
 */
public class UserServiceImpl implements UserService {

    private UserCommand userCommand = new UserCommand();
    @Override
    public User checkLogin(String userName, String password){
//        User user = userCommand.checkLogin(userName);
//        if (user == null) {
//            throw new RuntimeException("用户不存在");
//        }
//        String  md5 = MD5Utils.md5Digest(password, Constant.salt);
//        if (!user.getPassword().equals(md5)) {
//            System.out.println(user.getPassword());
//            System.out.println(md5);
//            throw new RuntimeException("账号或者密码不正确");
//        }
//        user.setPassword(null);
//        return user;
        User user = new User();
        for (User temp : Constant.userList) {
            if (temp.getUserName().equals(userName)) {
                if (!temp.getPassword().equals(password)) {
                    throw new RuntimeException("密码不正确");
                } else{
                    user.setUserId(temp.getUserId());
                    user.setUserName(temp.getUserName());
                    user.setPassword(temp.getPassword());
                    break;
                }
            }
        }

        if (StringUtils.isNullOrEmpty(user.getUserName())) {
            throw new  RuntimeException("账号不存在");
        }
        return user;
    }

    public static void main(String[] args){
        System.out.println(MD5Utils.md5Digest("123456", Constant.salt));

    }
}
