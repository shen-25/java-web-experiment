package com.jingdong.manager.command;

import com.jingdong.manager.model.entity.Service;
import com.jingdong.manager.utils.C3P0Utils;
import com.mysql.cj.util.StringUtils;
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

public class ServiceCommand {

    private Connection conn = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());


    public Map<String, Object> selectPage(String serviceId, Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_service where";
        String sql = "select * from manager_service where ";

        List<Service> serviceList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(serviceId)) {
            addWhere += "  id like ?  and ";
            objects.add("%" + serviceId + "%");
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
            serviceList = (List<Service>) qr.query(conn,sql, new BeanListHandler<>(Service.class, basic), objects.toArray());
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
        map.put("records", serviceList);
        map.put("total", total);
        return map;
    }

    public Service selectById(Long serviceId) {
        String sql = "select * from manager_service where id = ?";
        Service service = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            service = qr.query(conn, sql, new BeanHandler<>(Service.class, basic), new Object[]{serviceId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return service;
    }

    public void deleteById(Long serviceId) {
        String sql = "delete from manager_service where id = ?";
        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{serviceId});
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

    public void insert(Service service) {
        String sql = " insert into manager_service(`create_time`, `user_id`, product_id, return_reason, problem_desc, service_status, process_time, order_id)" +
                " values (?, ?, ?, ?, ?, ?,?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Service.class, basic), new Object[]{
                 service.getCreateTime(), service.getUserId(),
                    service.getProductId(),service.getReturnReason(), service.getProblemDesc(),
                    service.getServiceStatus(), service.getProcessTime(), service.getOrderId()
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




    public void updateById(Service service) {
        String sql = "update manager_service set `service_status` = ?," +
                "`process_time` =? where id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Service.class, basic), new Object[]{
                   service.getServiceStatus(), service.getProcessTime(),
                    service.getId()
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

    public static void main(String[] args) {
        ServiceCommand serviceCommand = new ServiceCommand();
//        Service service = serviceCommand.selectById(1L);
//        service.setId(null);
//        service.setProcessTime(null);
//        serviceCommand.insert(service);
  //      serviceCommand.deleteById(2L);
        Map<String, Object> stringObjectMap = serviceCommand.selectPage(null, 1, 10);
        System.out.println(stringObjectMap);


//        service.setServiceStatus(2);
//        service.setProcessTime(new Date());
//        serviceCommand.updateById(service);


    }

}
