package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.apache.commons.dbutils.DbUtils.close;


/**
 * @author word
 */
public class UserCommand {
    private Connection conn = null;
    private  PreparedStatement pstmt = null;
    private  BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());


    public User selectOne(String username) {
        String sql = "select* from manager_user where user_name=?";
        User user = null;
        try {
            DataSource dataSource = C3P0Utils.dataSource;
            QueryRunner qr = new QueryRunner(dataSource);
            BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());
            user = qr.query(sql, new BeanHandler<>(User.class, basic), new Object[]{username});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void updateById(User user) {
      String sql = "update  manager_user set user_name= ?, user_email = ?" +
               ",mobile = ?,  sex = ?, dept_id = ?, job = ?, `state` = ?" +
              ",password=?, salt= ?, create_time=?, last_login_time=?" +
              " where user_id = ?";
        try {
            conn = C3P0Utils.getConnection();
            QueryRunner qr = new QueryRunner();
            conn.setAutoCommit(false);
            qr.update(conn, sql, new Object[]{
                    user.getUserName(), user.getUserEmail(), user.getMobile(), user.getSex(),
                    user.getDeptId(), user.getJob(), user.getState(),
                    user.getPassword(), user.getSalt(), user.getCreateTime(), user.getLastLoginTime(), user.getUserId()
            });
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } finally {
            try {
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public User selectById(Long userId) {
        String sql = "select * from manager_user where user_id=?";
        List<User> query = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

            query = qr.query(conn, sql, new BeanListHandler<>(User.class, basic), new Object[]{userId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (query.isEmpty() || query.size() == 0) {
            return null;
        }
        if (query.size() >= 2) {
            throw new BusinessException(BusinessExceptionEnum.USER_EXISTS);
        }

        return query.get(0);
    }

    public Map<String, Object> selectPage(String userId, String userName, Integer state, Integer pageNum, Integer pageSize){
        List<User> userList = null;
        List<Object> objects = new ArrayList<>();
        DataSource dataSource = C3P0Utils.dataSource;
        QueryRunner qr = new QueryRunner(dataSource);
        String sql2 = "select user_id, user_name, user_email, mobile, sex,dept_id, job,state,create_time, last_login_time from manager_user where";
        String sql = "select user_id, user_name, user_email, mobile, sex,dept_id, job,state,create_time, last_login_time from manager_user where ";
        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(userId)) {
            addWhere += "  user_id like ?  and ";
            objects.add("%" + userId + "%");
        }
        if (!StringUtils.isNullOrEmpty(userName)) {
            addWhere += "  user_name like ?  and";
            objects.add("%" + userName + "%");
        }
        if (state != null && state != Constant.UserState.ALL) {
            addWhere += "  state=?  and";
            objects.add(state);
        }
        if (state == null) {
            addWhere += " state != " + Constant.UserState.QUITE + " and";
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            userList = (List<User>) qr.query(sql2, new BeanListHandler<>(User.class, basic), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        total = Long.valueOf(userList.size());
        sql += (addWhere +  " 1 = 1   limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            userList = (List<User>) qr.query(sql, new BeanListHandler<>(User.class, basic), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("records", userList);
        map.put("total", total);
        return map;
    }



    public void insert(User user) {

        String sql = "insert into manager_user (user_name, `password`, salt, " +
                "  user_email, mobile, sex, " +
                "      dept_id, job, `state`, " +
                "      create_time, last_login_time)" +
                "    values(?,?,?,?,?,?,?,?, ?,?,?)";
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            conn.setAutoCommit(false);
            qr.insert(conn, sql, new BeanHandler<>(User.class), new Object[]{
                    user.getUserName(),user.getPassword(), user.getSalt(),
                    user.getUserEmail(), user.getMobile(), user.getSex(),
                    user.getDeptId(), user.getJob(), user.getState(), user.getCreateTime(), user.getLastLoginTime()
            });
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn.isClosed() == false && conn != null) {
                    conn.rollback();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                if (conn.isClosed() == false && conn != null) {
                      conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer batchDeleteUser(List<Long> userIds){
       String sql = " delete from manager_user " +
               "    where user_id = ? ";
        try {
            conn = C3P0Utils.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            for (Long userId : userIds) {
                pstmt.setLong(1, userId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } finally {
            try {
                DbUtils.close(conn);
                DbUtils.close(pstmt);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userIds!= null ? userIds.size() : 0;
    }

    public List<Long> userLikeUserNameList(String userName) {
        String sql = "select * from manager_user where ";
        List<Object> objects = new ArrayList<>();
        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(userName)) {
            addWhere += "  user_name like ?  and ";
            objects.add("%" + userName + "%");
        }
        sql += addWhere + " 1 = 1";
        List<Long> longList = new ArrayList<>();
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            List<User> userList = queryRunner.query(conn, sql, new BeanListHandler<>(User.class, basic), objects.toArray());
            for (User user: userList) {
                longList.add(user.getUserId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return longList;
    }

    public static void main(String[] args) {
        UserCommand userCommand = new UserCommand();
        //User admin = userCommand.selectOne("admin");
//        User user = new User();
//        user.setDeptId(5L);
//        user.setJob("adsf");
//        user.setMobile("fdasfas");
//        user.setSex(1);
//        user.setState(1);
//        user.setUserEmail("dasdddas");
//        user.setUserName("dassffdadsa");
//        user.setCreateTime(new Date());
//        user.setPassword("11111");
//        user.setSalt(1111);
//        userCommand.insert(user);
        List<Long> dd = userCommand.userLikeUserNameList("d");
        System.out.println(dd);
    }


}
