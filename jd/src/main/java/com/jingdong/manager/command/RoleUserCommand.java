package com.jingdong.manager.command;

import com.jingdong.manager.model.entity.RoleUser;
import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleUserCommand {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());


    public List<Long> selectRoleIds(Long userId) {
        String sql = "select role_id from manager_role_user where user_id = ?";
        List<Long> query = new ArrayList<>();
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                query.add(resultSet.getLong("role_id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
                DbUtils.close(pstmt);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return query;
    }

    public void batchInsert(List<Long> roleIds, Long userId){
        if (roleIds == null || userId == null) {
            return;
        }
        if (roleIds.isEmpty()) {
            return;
        }
        String sql = "insert into manager_role_user(role_id, user_id) values(?, ?)";
        Object[][] objects = new Object[roleIds.size()][2];
        for (int i = 0; i < roleIds.size(); i++) {
            objects[i][0] = roleIds.get(i);
            objects[i][1] = userId;
        }
        conn = C3P0Utils.getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.batch(conn, sql, objects);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public List<RoleUser> selectList(Long userId) {
        String sql = "select * from manager_role_user where user_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<RoleUser> roleUserList = new ArrayList<>();
        try {
            roleUserList = queryRunner.query(conn, sql, new BeanListHandler<>(RoleUser.class, basic), new Object[]{userId});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleUserList;
    }



    public Integer delete(Long userId) {
        String sql = "delete from  manager_role_user where user_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Integer cnt = 0;
        try {
            conn.setAutoCommit(false);
             cnt = queryRunner.update(conn, sql, new Object[]{userId});
             conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cnt;
    }

    public void deleteByRoleId(Long roleId) {
        String sql = "delete from  manager_role_user where role_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{roleId});
             conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
      RoleUserCommand roleUserCommand = new RoleUserCommand();
        List<RoleUser> roleUsers = roleUserCommand.selectList(1L);
        System.out.println(roleUsers);
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(444L);
        roleIds.add(555L);
        roleUserCommand.batchInsert(roleIds, 111L);
//        List<Long> longs = roleUserCommand.selectRoleIds(231L);
//        System.out.println(longs);
    }
}
