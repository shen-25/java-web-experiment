package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.Advert;
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

public class AdvertCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());


    public Map<String, Object> selectPage(String advertName, Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_advert where";
        String sql = "select * from manager_advert where ";

        List<Advert> advertList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(advertName)) {
            addWhere += "  name like ?  and ";
            objects.add("%" + advertName + "%");
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql += (addWhere +  " 1 = 1 ORDER BY advert_order  limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            advertList = (List<Advert>) qr.query(conn,sql, new BeanListHandler<>(Advert.class, basic), objects.toArray());
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
        map.put("records", advertList);
        map.put("total", total);
        return map;
    }

    public Advert selectById(Long advertId) {
        String sql = "select * from manager_advert where id = ?";
        Advert advert = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            advert = qr.query(conn, sql, new BeanHandler<>(Advert.class, basic), new Object[]{advertId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return advert;
    }

    public Advert selectByName(String advertName) {
        String sql = "select * from manager_advert where `name` = ?";
        Advert advert = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            advert = qr.query(conn, sql, new BeanHandler<>(Advert.class, basic), new Object[]{advertName});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return advert;
    }

    public void deleteById(Long advertId) {
        String sql = "delete from manager_advert where id = ?";
        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{advertId});
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

    public void insert(Advert advert) {
        String sql = " insert into manager_advert(`name`, `advert_desc`, status, advert_order, pic_url, url, start_time," +
                "end_time,create_time, update_time )" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Advert.class, basic), new Object[]{
                    advert.getName(), advert.getAdvertDesc(), advert.getStatus(), advert.getAdvertOrder(),
                    advert.getPicUrl(), advert.getUrl(), advert.getStartTime(), advert.getEndTime(),
                    advert.getCreateTime(), advert.getUpdateTime()
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



    public void updateById(Advert advert) {
        String sql = "update manager_advert set `name` = ?," +
                "`advert_desc` =?,`status` =?," +
                " advert_order = ?, pic_url = ?, url=?, start_time= ?,end_time= ?" +
                ", create_time = ?, update_time= ?" +
                "  where id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Advert.class, basic), new Object[]{
                    advert.getName(), advert.getAdvertDesc(), advert.getStatus(), advert.getAdvertOrder(),
                    advert.getPicUrl(), advert.getUrl(), advert.getStartTime(), advert.getEndTime(),
                    advert.getCreateTime(), advert.getUpdateTime(), advert.getId()
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
        AdvertCommand advertCommand = new AdvertCommand();
//        Advert advert = new Advert();
//        advert.setName("曾深");
//        advert.setCreatTime(new Date());
//        advert.setEndTime(new Date());
//        advert.setStartTime(new Date());
//        advert.setStatus(1);
//        advert.setAdvertOrder(200);
//        advertCommand.insert(advert);
        Advert advert = advertCommand.selectById(1L);
        advert.setName("ddd");
        advertCommand.updateById(advert);

    }


}
