package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.Brand;
import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.vo.BrandKeyValueVo;
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

public class BrandCommand {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public Brand selectByBrandName(String brandName) {
        String sql = "select* from manager_brand where brand_name=? ";
        Brand brand = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            brand = queryRunner.query(conn, sql, new BeanHandler<>(Brand.class, basic), new Object[]{
                  brandName
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
        return brand;
    }

    public void insert(Brand brand ) {
        String sql = " insert into manager_brand (brand_name," +
                "brand_pic_url, brand_desc, brand_state, brand_order, create_time, update_time)" +
                " values (?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
                 brand.getBrandName(), brand.getBrandPicUrl(), brand.getBrandDesc(),
                    brand.getBrandState(), brand.getBrandState(), brand.getCreateTime(), brand.getUpdateTime()
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

    public Map<String, Object> selectPage(String brandId, String brandName, Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_brand where";
        String sql = "select * from manager_brand where ";

        List<Brand> brandList = null;
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(brandName)) {
            addWhere += "  brand_name like ?  and ";
            objects.add("%" + brandName + "%");
        }
        if (!StringUtils.isNullOrEmpty(brandId)) {
            addWhere += "  brand_Id like ?  and ";
            objects.add("%" + brandId + "%");
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql += (addWhere +  " 1 = 1 ORDER BY brand_id  limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            brandList = (List<Brand>) qr.query(conn,sql, new BeanListHandler<>(Brand.class, basic), objects.toArray());
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
        map.put("records", brandList);
        map.put("total", total);
        return map;
    }

    public void edit(Brand brand) {
        String sql = "update manager_brand set brand_name = ?," +
                "brand_pic_url =?,`brand_desc` =?," +
                " brand_state = ?, " +
                "brand_order = ?, update_time= ?" +
                "  where brand_id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
               brand.getBrandName(), brand.getBrandPicUrl(), brand.getBrandDesc(),
                    brand.getBrandState(), brand.getBrandOrder(),
                    brand.getUpdateTime(),brand.getBrandId()
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

    public void deleteById(Long brandId) {
        String sql = "delete from manager_brand where brand_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int cnt = 0;
        try {
            conn.setAutoCommit(false);
            cnt = queryRunner.update(conn, sql, new Object[]{brandId});
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

    public Brand selectById(Long brandId) {
        String sql = "select * from manager_brand where brand_id = ?";
        Brand brand = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            brand = qr.query(conn, sql, new BeanHandler<>(Brand.class, basic), new Object[]{brandId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return brand;
    }
    public List<BrandKeyValueVo> selectBrandKeyValue(){
        String sql = "select brand_id as brandId , brand_name as brandName from manager_brand where brand_state = 0 order by brand_order";
        QueryRunner queryRunner = new QueryRunner();
        conn = C3P0Utils.getConnection();
        List<BrandKeyValueVo> brandKeyValueVoList= new ArrayList<>();
        try {
            brandKeyValueVoList = queryRunner.query(conn, sql, new BeanListHandler<>(BrandKeyValueVo.class, basic));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return brandKeyValueVoList;
    }

    public static void main(String[] args) {
        BrandCommand brandCommand = new BrandCommand();
//        Brand brand = new Brand();
//        brand.setBrandDesc("描述");
//        brand.setBrandName("名字");
//        brand.setBrandOrder(1);
//        brand.setCreateTime(new Date());
//        brand.setBrandPicUrl("/dddd");
//        brand.setBrandState(1);
//        brandCommand.insert(brand);
//        brandCommand.selectPage(null, "11", 1, 10);
//        brandCommand.deleteById(1L);
//        Brand brand = brandCommand.selectByBrandName("你好");
//        brand.setBrandName("dddd");
//        Brand brand1 = brandCommand.selectById(3L);
//        System.out.println(brand1);
//        brandCommand.edit(brand);

        List<BrandKeyValueVo> brandKeyValueVoList = brandCommand.selectBrandKeyValue();
        System.out.println();

    }


}
