package com.jingdong.manager.command;

import com.jingdong.manager.model.entity.RoleMenu;
import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleMenuCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public List<RoleMenu> selectList(List<Long> roleList){
        if (roleList == null || roleList.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "select * from manager_role_menu where role_id in";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (Long roleId : roleList) {
            stringBuilder.append(roleId + ",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        sql += stringBuilder.toString();
        QueryRunner queryRunner = new QueryRunner();
        List<RoleMenu> roleMenuList = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        try {
            roleMenuList = queryRunner.query(conn, sql, new BeanListHandler<>(RoleMenu.class, basic));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleMenuList;
    }

    public void deleteByRoleId(Long roleId) {
        String sql = "delete from manager_role_menu where role_id = ?";
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn = C3P0Utils.getConnection();
            conn.setAutoCommit(false);
             queryRunner.update(conn, sql, new Object[]{roleId});
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

    public List<RoleMenu> selectListByRoleId(Long roleId) {
        if (roleId == null ) {
            return new ArrayList<>();
        }
        String sql = "select * from manager_role_menu where role_id =?";
        QueryRunner queryRunner = new QueryRunner();
        List<RoleMenu> roleMenuList = new ArrayList<>();

        try {
            conn = C3P0Utils.getConnection();
            roleMenuList = queryRunner.query(conn, sql, new BeanListHandler<>(RoleMenu.class, basic),
                    new Object[]{roleId});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleMenuList;
    }

    public List<RoleMenu> selectListByMenuId(Long menuId) {
        if (menuId == null ) {
            return new ArrayList<>();
        }
        String sql = "select * from manager_role_menu where menu_id =?";
        QueryRunner queryRunner = new QueryRunner();
        List<RoleMenu> roleMenuList = new ArrayList<>();

        try {
            conn = C3P0Utils.getConnection();
            roleMenuList = queryRunner.query(conn, sql, new BeanListHandler<>(RoleMenu.class, basic),
                    new Object[]{menuId});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleMenuList;
    }

    public void batchInsert(List<Long> menuIds, Long roleId) {
        if (menuIds == null || roleId == null) {
            return;
        }
        if (menuIds.isEmpty()) {
            return;
        }
        String sql = "insert into manager_role_menu (menu_id, role_id) values(?, ?)";
        Object[][] objects = new Object[menuIds.size()][2];
        for (int i = 0; i < menuIds.size(); i++) {
            objects[i][0] = menuIds.get(i);
            objects[i][1] = roleId;
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

    public void deleteByMenuId(Long menuId) {
        String sql = "delete from manager_role_menu where menu_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int cnt = 0;
        try {
            conn.setAutoCommit(false);
            cnt = queryRunner.update(conn, sql, new Object[]{menuId});
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

    public void insert(RoleMenu roleMenu){

        String sql = "insert into manager_role_menu(role_id, menu_id) values(?, ?)";
        conn = C3P0Utils.getConnection();
        try {
            conn.setAutoCommit(false);
            QueryRunner queryRunner = new QueryRunner();
            queryRunner.execute(conn, sql, new Object[]{roleMenu.getRoleId(), roleMenu.getMenuId()});
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

    public static void main(String[] args) {
        RoleMenuCommand roleMenuCommand = new RoleMenuCommand();
        List<Long> longArrayList = new ArrayList<Long>();
//        longArrayList.add(222L);
//        longArrayList.add(333L);
//        roleMenuCommand.batchInsert(longArrayList, 444L);
//        roleMenuCommand.deleteByMenuId(333L);
          RoleMenu  roleMenu = new RoleMenu();
          roleMenu.setRoleId(55L);
            roleMenu.setMenuId(66L);
            roleMenuCommand.insert(roleMenu);


//        longArrayList.add(2L);
//        List<RoleMenu> roleMenus = roleMenuCommand.selectList(longArrayList);
//        roleMenuCommand.deleteByRoleId(111L);
//        roleMenuCommand.selectListByRoleId(2L);

    }


}
