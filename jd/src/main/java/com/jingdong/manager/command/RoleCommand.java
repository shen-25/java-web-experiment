package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.Role;
import com.jingdong.manager.model.vo.RoleKeyValueVo;
import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.apache.commons.dbutils.DbUtils.close;

public class RoleCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public Role selectById(Long roleId) {
        String sql = "select * from manager_role where role_id = ?";
        Role role = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            role = qr.query(conn, sql, new BeanHandler<>(Role.class, basic), new Object[]{roleId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return role;
    }

    public List<RoleKeyValueVo> selectRoleKeyValue(){
        String sql = "select role_id as value ,role_name as name from manager_role where `state` = 0";
        QueryRunner queryRunner = new QueryRunner();
        conn = C3P0Utils.getConnection();
        List<RoleKeyValueVo> roleKeyValueVos = null;
        try {
            roleKeyValueVos = queryRunner.query(conn, sql, new BeanListHandler<>(RoleKeyValueVo.class, basic));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleKeyValueVos;
    }

    public   List<Role> selectList(String roleName) {
        String sql = "select * from manager_role where role_name = ?";
        List<Role> roleList = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            roleList = qr.query(conn, sql, new BeanListHandler<>(Role.class, basic), new Object[]{roleName});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleList;
    }

    public void insert(Role role) {
        String sql = " insert into manager_role (role_name, remark, `state`,create_time, update_time)" +
                " values (?, ?, ?, ?, ?)";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Role.class, basic), new Object[]{
                    role.getRoleName(), role.getRemark(), role.getState(), role.getCreateTime(), role.getUpdateTime()
            });
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {

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


    public void updateById(Role role) {
        String sql = "update manager_role set role_name = ?," +
                "remark =?,`state` =?," +
                " create_time = ?, " +
                "update_time = ?  where role_id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Role.class, basic), new Object[]{
                    role.getRoleName(), role.getRemark(), role.getState(), role.getCreateTime(), role.getUpdateTime(),
                    role.getRoleId()
            });
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {

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

    public Integer deleteById(Long roleId) {
        String sql = "delete from manager_role where role_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int cnt = 0;
        try {
            conn.setAutoCommit(false);
            cnt = queryRunner.update(conn, sql, new Object[]{roleId});
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {

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
    public Map<String, Object> selectPage(String roleName,  Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_role where";
        String sql = "select * from manager_role where ";

        List<Role> roleList = null;
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(roleName)) {
            addWhere += "  role_name like ?  and ";
            objects.add("%" + roleName + "%");
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
             total = (Long) qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql += (addWhere +  " 1 = 1 ORDER BY role_id  limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            roleList = (List<Role>) qr.query(conn,sql, new BeanListHandler<>(Role.class, basic), objects.toArray());
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
        map.put("records", roleList);
        map.put("total", total);
        return map;
    }



    public static void main(String[] args) {
        RoleCommand roleCommand = new RoleCommand();
//        List<Role> roles = roleCommand.selectList("超级管理员");
//        System.out.println(roles);
//        Role role = roleCommand.selectById(4L);

//        System.out.println(role);
//        Role role = new Role();
//        role.setRoleId(134L);
//        role.setUpdateTime(new Date());
//        role.setCreateTime(new Date());
//        role.setRemark("测试一下");
//        role.setRoleName("11");
//        role.setState(0);
//
//        roleCommand.updateById(role);
//        Integer integer = roleCommand.deleteById(134L);
//        System.out.println(integer);
        Map<String, Object> stringObjectMap = roleCommand.selectPage("工程师", 1, 10);
        System.out.println(stringObjectMap);

    }

}
