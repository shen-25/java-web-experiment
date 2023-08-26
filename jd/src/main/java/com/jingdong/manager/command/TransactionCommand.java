package com.jingdong.manager.command;

import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class TransactionCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public Long todayOrderTotal() {
        String sql = "select count(*) from manager_order where create_time BETWEEN CONCAT(CURDATE(),' 00:00:00') AND CONCAT(CURDATE(),' 23:59:59')";
        Long total = 0L;

        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            total = (Long) qr.query(conn, sql, new ScalarHandler<Number>()).longValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public Long   todayOrderValues(){
        String sql = "select IFNULL(sum(total_price), 0) from manager_order where create_time BETWEEN CONCAT(CURDATE(),' 00:00:00') AND CONCAT(CURDATE(),' 23:59:59')" +
                " and order_status >= 2 ";
        Long total = 0L;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            total =  qr.query(conn, sql, new ScalarHandler<Number>()).longValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total == null ? 0 : total;
    }

    public Long  yesterdayOrderValues(){
        String sql = "SELECT IFNULL(sum(total_price), 0) FROM  manager_order WHERE TO_DAYS(NOW()) - TO_DAYS(create_time) <= 1 and TO_DAYS(NOW()) - TO_DAYS(create_time) != 0 " +
                " and order_status > 2 ";
        Long total = 0L;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            total =  qr.query(conn, sql, new ScalarHandler<Number>()).longValue();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public static void main(String[] args) {
        TransactionCommand transactionCommand = new TransactionCommand();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date todayStart = calendar.getTime();
        Long total = transactionCommand.todayOrderTotal();
        System.out.println(total);

        Long aLong = transactionCommand.todayOrderValues();
        System.out.println(aLong / 100);
        System.out.println(transactionCommand.yesterdayOrderValues());
    }
}
