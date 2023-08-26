package com.example.shiyan1.command;

import com.example.shiyan1.entity.User;
import com.example.shiyan1.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author word
 */
public  class UserCommand {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    public User checkLogin(String username) {
        User user = null;
        try{
            connection = DbUtils.getConnection();
            String sql = "select* from user where username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setUserId(rs.getLong("user_id"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DbUtils.closeConnection(null, preparedStatement, connection);
        }
        return user;
    }

    public Integer checkRegister(String userName) throws SQLException {
        Integer cnt = 0;
        try {
            connection  = DbUtils.getConnection();
            String sql = "select* from user where username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                cnt = 1;
                break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DbUtils.closeConnection(rs, preparedStatement, connection);
        }
         return cnt;
    }

    public int create(String userName, String password) {
        connection = DbUtils.getConnection();
        int cnt = 0;
        String sql = "insert into user(username, password) values(?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            cnt =  preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DbUtils.closeConnection(null, preparedStatement, connection);
        };
        return cnt;
    }

    public static void main(String[] args) {
        UserCommand userCommand = new UserCommand();
        User user = userCommand.checkLogin("demo");
        System.out.println(user);
    }

}