package com.jingdong.manager.command;

import com.jingdong.manager.model.entity.OrderItem;
import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderItemCommand {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public List<OrderItem> selectByOrderNo(String orderNo) {
        String sql = "select * from manager_order_item where order_no = ?";
        List<OrderItem> orderItems = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            orderItems = qr.query(conn, sql, new BeanListHandler<>(OrderItem.class, basic), new Object[]{orderNo});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderItems;
    }

    public void insert(OrderItem orderItem) {
        String sql = " insert into manager_order_item(order_no, product_id, `product_name`, product_img, unit_price," +
        " quantity, total_price, create_time, update_time )" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(OrderItem.class, basic), new Object[]{
              orderItem.getOrderNo(), orderItem.getProductId(), orderItem.getProductName(),
                    orderItem.getProductImg(), orderItem.getUnitPrice(), orderItem.getQuantity(),
                    orderItem.getTotalPrice(), orderItem.getCreateTime(), orderItem.getUpdateTime()
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

    public void deleteByOrderNo(String orderNo) {
        String sql = "delete from manager_order_item where order_no = ?";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{orderNo});
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
}
