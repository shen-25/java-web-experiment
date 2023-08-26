package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.Cart;
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

public class CartCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());


    public void insert(Cart cart) {
        String sql = " insert into manager_cart (product_id,user_id," +
                "quantity, selected, create_time, update_time )" +
                " values (?, ?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Cart.class, basic), new Object[]{
                    cart.getProductId(), cart.getUserId(), cart.getQuantity(), cart.getSelected(),
                    cart.getCreateTime(), cart.getUpdateTime()
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

    public   List<Cart> selectByUserId(Long userId) {
        String sql = "select* from manager_cart where user_id=? ";
       List<Cart> cartList = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
         cartList = queryRunner.query(conn, sql, new BeanListHandler<>(Cart.class, basic), new Object[]{
                    userId
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
        return cartList;
    }

    public void deleteByUserId(Long userId) {
        String sql = "delete from manager_cart where user_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{userId});
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


    public void deleteById(Long id) {
        String sql = "delete from manager_cart where id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{id});
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


    public void deleteByUserIdAndProductId(Long userId, Long productId) {
        String sql = "delete from manager_cart where user_id = ? and prodcut_id";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{userId, productId});
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

    public void edit(Cart cart) {
        String sql = "update manager_cart set product_id = ?," +
                "user_id =?,`quantity` =?, selected=?,create_time = ?, update_time= ?" +
                "  where id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Cart.class, basic), new Object[]{
                    cart.getProductId(),cart.getUserId(), cart.getQuantity(), cart.getSelected(),
                    cart.getCreateTime(), cart.getUpdateTime(), cart.getId()
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

    public Map<String, Object> selectPage(String userId, Integer pageNum, Integer pageSize) {
        String sql2 = "select count(*) from manager_cart where";
        String sql = "select * from manager_cart where ";
        List<Cart> cartList = null;
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(userId)) {
            addWhere += "  user_id like ?  and ";
            objects.add("%" + userId + "%");
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql += (addWhere +  " 1 = 1   limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            cartList = (List<Cart>) qr.query(conn,sql, new BeanListHandler<>(Cart.class, basic), objects.toArray());
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
        map.put("records", cartList);
        map.put("total", total);
        return map;
    }

    public Cart selectCartByUserIdAndProductId(Long userId, Long productId) {
        String sql = "select* from manager_cart where user_id=? and product_id = ? ";
        Cart cart = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            cart= queryRunner.query(conn, sql, new BeanHandler<>(Cart.class, basic), new Object[]{
                    userId, productId
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
        return cart;
    }


    public Cart selectById(Long cartId) {
        String sql = "select* from manager_cart where id=  ? ";
        Cart cart = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            cart= queryRunner.query(conn, sql, new BeanHandler<>(Cart.class, basic), new Object[]{
                  cartId
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
        return cart;
    }


}
