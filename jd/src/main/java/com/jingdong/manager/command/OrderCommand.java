package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.Order;
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

public class OrderCommand {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public Map<String, Object> selectPage(String orderNo, String receiverName , String createTime, String orderStatus, Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_order where";
        String sql = "select * from manager_order where ";

        List<Order> orderList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(orderNo)) {
            addWhere += "  order_no like ?  and ";
            objects.add("%" + orderNo + "%");
        }
        if (!StringUtils.isNullOrEmpty(receiverName)) {
            addWhere += "  receiver_name like ?  and ";
            objects.add("%" + receiverName + "%");
        }
        if (!StringUtils.isNullOrEmpty(orderStatus) && (Integer.parseInt(orderStatus)) != -1) {
            addWhere += " order_status = ? and";
            objects.add(orderStatus);
        }
        if (!StringUtils.isNullOrEmpty(createTime)) {
            addWhere += "  create_time >= ?  and ";
            objects.add(createTime);
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
            orderList = (List<Order>) qr.query(conn,sql, new BeanListHandler<>(Order.class, basic), objects.toArray());
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
        map.put("records", orderList);
        map.put("total", total);
        return map;
    }

    public Order selectById(Long orderId) {
        String sql = "select * from manager_order where id = ?";
        Order order = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            order = qr.query(conn, sql, new BeanHandler<>(Order.class, basic), new Object[]{orderId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }


    public Order selectByOrderNo(String orderNo) {
        String sql = "select * from manager_order where order_no = ?";
        Order order = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            order = qr.query(conn, sql, new BeanHandler<>(Order.class, basic), new Object[]{orderNo});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }

    public void edit(Order order) {
        String sql = "update manager_order set order_no = ?," +
                "user_id= ?, total_price = ?, receiver_name = ?, receiver_mobile= ?," +
                "receiver_address=?, order_status=?, postage=?,payment_type=?," +
                "delivery_no=?, delivery_company=?, delivery_time = ?, pay_time=?, end_time=?, create_time=?," +
                "update_time=?" +
                "  where id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Order.class, basic), new Object[]{
               order.getOrderNo(), order.getUserId(), order.getTotalPrice(),
                    order.getReceiverName(), order.getReceiverMobile(),order.getReceiverAddress(),
                    order.getOrderStatus(), order.getPostage(), order.getPaymentType(), order.getDeliveryNo(),
                    order.getDeliveryCompany(),order.getDeliveryTime(),order.getPayTime(), order.getEndTime(),
                    order.getCreateTime(), order.getUpdateTime(), order.getId()
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

    public void insert(Order order) {
        String sql = " insert into manager_order(order_no, user_id, `total_price`, receiver_name, receiver_mobile," +
                "receiver_address, order_status, postage, payment_type,delivery_no, delivery_company, delivery_time," +
                " pay_time, end_time,create_time, update_time )" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Order.class, basic), new Object[]{
                    order.getOrderNo(), order.getUserId(), order.getTotalPrice(),
                    order.getReceiverName(), order.getReceiverMobile(),order.getReceiverAddress(),
                    order.getOrderStatus(), order.getPostage(), order.getPaymentType(), order.getDeliveryNo(),
                    order.getDeliveryCompany(),order.getDeliveryTime(),order.getPayTime(), order.getEndTime(),
                    order.getCreateTime(), order.getUpdateTime()
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

    public void deleteById(Long orderId) {
        String sql = "delete from manager_order where id = ?";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{orderId});
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
    public static void main(String[] args) {
        OrderCommand orderCommand = new OrderCommand();
//        Map<String, Object> stringObjectMap = orderCommand.selectPage(null, null, null, null, 1, 10);
//        System.out.println(stringObjectMap);

//        Order order = orderCommand.selectById(9L);
//        order.setOrderNo("111111111111");
//        orderCommand.insert(order);
        Order order = orderCommand.selectByOrderNo(null);
        System.out.println(order);
    }



}
