package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.vo.PermissionTreeVo;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.dbutils.DbUtils.close;

public class MenuCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public Menu selectById(Long menuId) {
        String sql = "select * from manager_menu where menu_id = ?";
        Menu menu = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            menu = qr.query(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{menuId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    public List<Menu> selectByALL() {
        String sql = "select * from manager_menu";
        List<Menu> menuList = new ArrayList<>();
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            menuList = qr.query(conn, sql, new BeanListHandler<>(Menu.class, basic));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return menuList;
    }

    public Menu selectByMenuName(String menuName) {
        String sql = "select * from manager_menu where menu_name = ?";
        Menu menu = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            menu = qr.query(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{menuName});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }


    public void updateById(Menu menu) {
        String sql = "update manager_menu set menu_name = ?," +
                "menu_type =?,`url` =?," +
                " icon = ?, " +
                "parent_id = ?, menu_order=?, menu_state= ?, create_time = ?, update_time= ?" +
                "  where menu_id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
                   menu.getMenuName(), menu.getMenuType(), menu.getUrl(),
                    menu.getIcon(), menu.getParentId(), menu.getMenuOrder(),
                    menu.getMenuState(), menu.getCreateTime(), menu.getUpdateTime(), menu.getMenuId()
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

    public Map<String, Object> selectPage(String menuName, String url, String menuState, Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_menu where";
        String sql = "select * from manager_menu where ";

        List<Menu> menuList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(menuName)) {
            addWhere += "  menu_name like ?  and ";
            objects.add("%" + menuName + "%");
        }
        if (!StringUtils.isNullOrEmpty(url)) {
            addWhere += "  url like ?  and ";
            objects.add("%" + url + "%");
        }

        if (!StringUtils.isNullOrEmpty(menuState) && (Integer.parseInt(menuState)) != (Constant.MenuState.All)) {
            addWhere += " menu_state = ? and";
            objects.add(menuState);
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total =  qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql += (addWhere +  " 1 = 1 ORDER BY menu_id  limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            menuList = (List<Menu>) qr.query(conn,sql, new BeanListHandler<>(Menu.class, basic), objects.toArray());
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
        map.put("records", menuList);
        map.put("total", total);
        return map;
    }

    public Menu selectOne(String menuName, int menuType) {
        String sql = "select* from manager_menu where menu_name=? and menu_type = ?";
        Menu menu = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            menu = queryRunner.query(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
                    menuName, menuType
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    public Menu selectByUrl(String url) {
        String sql = "select* from manager_menu where url = ?";
        Menu menu = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            menu = queryRunner.query(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
               url
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    public List<Menu> selectListByMenuType(int menuType) {
        String sql = "select* from manager_menu where menu_type = ?";
        List<Menu> menuList = new ArrayList<>();
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            menuList = queryRunner.query(conn, sql, new BeanListHandler<>(Menu.class, basic), new Object[]{
                    menuType
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return menuList;
    }

    public void insert(Menu menu) {
        String sql = " insert into manager_menu (menu_name, menu_type, `url`, icon, parent_id," +
                "menu_order, menu_state, create_time, update_time )" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
                    menu.getMenuName(), menu.getMenuType(), menu.getUrl(),
                    menu.getIcon(), menu.getParentId(), menu.getMenuOrder(),
                    menu.getMenuState(), menu.getCreateTime(), menu.getUpdateTime()
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

    public Integer batchDeleteMenu(List<Long> menuIds){
        if (menuIds == null || menuIds.isEmpty()) {
            return 0;
        }
        String sql = " delete from manager_menu" +
                "    where menu_id = ? ";
        try {
            conn = C3P0Utils.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            for (Long menuId : menuIds) {
                pstmt.setLong(1, menuId);
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
            C3P0Utils.closeConnection(null, pstmt, conn);
        } finally {
            C3P0Utils.closeConnection(null, pstmt, conn);
        }
        return menuIds.size();
    }

    public List<PermissionTreeVo> selectPermissionByParentId(Long menuId){
        String sql = "  select menu_id as id, menu_name as title from manager_menu " +
                "where parent_id = ? order by menu_order ";
        List<PermissionTreeVo> permissionTreeVos = new ArrayList<>();
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
             permissionTreeVos = queryRunner.query(conn, sql, new BeanListHandler<>(PermissionTreeVo.class, basic), new Object[]{
                    menuId
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return permissionTreeVos;
    }


    public static void main(String[] args) {
        MenuCommand menuCommand = new MenuCommand();
        Menu menu = menuCommand.selectById(1L);
        System.out.println(menu);
//        Menu menu = menuCommand.selectById(5L);
//        System.out.println(menu);
//        menu.setMenuId(null);
//        menuCommand.insert(menu);
//        Menu menu = menuCommand.selectById(1L);
//        menu.setMenuName("系统管理");
//        menu.setUpdateTime(new Date());
//        menu.setCreateTime(new Date());
//        menuCommand.updateById(menu);
//        Menu 系统管理 = menuCommand.selectByMenuName("系统管理");
//        System.out.println();
//        Map<String, Object> stringObjectMap = menuCommand.selectPage(null, null, "1", 1, 10);
//        System.out.println(stringObjectMap);
////        Menu 系统管理 = menuCommand.selectOne("系统管理", 0);
////        System.out.println();
//
//        List<Menu> menuList = menuCommand.selectListByUrl("/shop/add");
//        System.out.println(menuList);
//        List<Long> menuIds = new ArrayList<>();
//
//        menuIds.add(53L);
//
//        menuCommand.batchDeleteMenu(menuIds);
    }
}
