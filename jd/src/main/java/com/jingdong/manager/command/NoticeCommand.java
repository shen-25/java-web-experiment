package com.jingdong.manager.command;
import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.*;
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

public class NoticeCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public void insert(Notice notice ) {
        String sql = " insert into manager_notice (title," +
                "content, create_time, update_time)" +
                " values (?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Notice.class, basic), new Object[]{
                 notice.getTitle(), notice.getContent(), notice.getCreateTime(), notice.getUpdateTime()
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

    /**
     *     String sql = " insert into manager_comment (user_id," +
     *                 "product_id, content, score, status, reply, create_time, update_time)" +
     *                 " values (?, ?, ?, ?, ?, ?, ?, ?)";
     * 
     */
    public void edit(Notice notice) {
        String sql = "update manager_notice set title = ?," +
                "content =?, create_time = ?, update_time=?" +
                "  where id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Notice.class, basic), new Object[]{
                notice.getTitle(), notice.getContent(), notice.getCreateTime(), notice.getUpdateTime(),
                    notice.getId()
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

    public Notice selectById(Long noticeId) {
        String sql = "select * from manager_notice where id = ?";
        Notice notice = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            notice = qr.query(conn, sql, new BeanHandler<>(Notice.class, basic), new Object[]{noticeId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return notice;
    }

    public void deleteById(Long noticeId) {
        String sql = "delete from manager_notice where id = ?";
        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{noticeId});
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

    public Map<String, Object> selectPage(String title, String content,Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_notice where";
        String sql = "select * from manager_notice where ";

        List<Notice> noticeList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(title)) {
            addWhere += "  title like ?  and ";
            objects.add("%" + title + "%");
        }
        if (!StringUtils.isNullOrEmpty(content)) {
            addWhere += "  content like ?  and ";
            objects.add("%" + content + "%");
        }

        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total =  qr.query(conn, sql2, new ScalarHandler<Number>(), objects.toArray()).longValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql += (addWhere +  " 1 = 1   limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            noticeList = (List<Notice>) qr.query(conn,sql, new BeanListHandler<>(Notice.class, basic), objects.toArray());
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
        map.put("records", noticeList);
        map.put("total", total);
        return map;
    }

    public Notice selectByTitle(String title) {
        String sql = "select * from manager_notice where title = ?";
        Notice notice = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            notice = qr.query(conn, sql, new BeanHandler<>(Notice.class, basic), new Object[]{title});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return notice;
    }



    public static void main(String[] args) {

//        Notice notice = new Notice();
//        notice.setTitle("测试一笑");
//        notice.setContent("dddddddddddd");
//        notice.setCreateTime(new Date());
         NoticeCommand noticeCommand = new NoticeCommand();
//        noticeCommand.insert(notice);

//        Notice notice = noticeCommand.selectById(1L);
//        notice.setUpdateTime(new Date());
//
//        notice.setContent("发送");
//        noticeCommand.edit(notice);
        Map<String, Object> stringObjectMap = noticeCommand.selectPage(null, null, 1, 10);
        System.out.println(stringObjectMap);

    }
}
