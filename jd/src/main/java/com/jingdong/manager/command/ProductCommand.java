package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.entity.Product;
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
import static org.apache.commons.dbutils.DbUtils.loadDriver;

/**
 * @author word
 */
public class ProductCommand {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public Map<String, Object> selectPage(String productName, String categoryId, String brandId, String productState, Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_product where";
        String sql = "select * from manager_product where ";

        List<Product> productList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(productName)) {
            addWhere += "  product_name like ?  and ";
            objects.add("%" + productName + "%");
        }
        if (!StringUtils.isNullOrEmpty(categoryId) && (Integer.parseInt(categoryId)) != -1) {
            addWhere += " category_id = ? and";
            objects.add(categoryId);
        }
        if (!StringUtils.isNullOrEmpty(brandId) && (Integer.parseInt(brandId)) != -1) {
            addWhere += " brand_id = ? and";
            objects.add(brandId);
        }

        if (!StringUtils.isNullOrEmpty(productState) && (Integer.parseInt(productState)) != (Constant.ProductState.All)) {
            addWhere += " product_state = ? and";
            objects.add(productState);
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql += (addWhere +  " 1 = 1 ORDER BY product_order  limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            productList = (List<Product>) qr.query(conn,sql, new BeanListHandler<>(Product.class, basic), objects.toArray());
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
        map.put("records", productList);
        map.put("total", total);
        return map;
    }

    public Long selectByUpState() {
        String sql = "select count(*) from manager_product where  product_state =" + Constant.ProductState.NORMAL;
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public Long selectByDownState() {
        String sql = "select count(*) from manager_product where  product_state =" + Constant.ProductState.STOP;
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public Long selectAllProduct() {
        String sql = "select count(*) from manager_product";
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public Long selectByTightProduct() {
        String sql = "select count(*) from manager_product where product_stock <=" + Constant.tightProductNumber;
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public Product selectById(Long productId) {
        String sql = "select * from manager_product where product_id = ?";
       Product product = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            product = qr.query(conn, sql, new BeanHandler<>(Product.class, basic), new Object[]{productId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return product;
    }

    public Product selectByName(String productName) {
        String sql = "select * from manager_product where product_name = ?";
        Product product = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            product = qr.query(conn, sql, new BeanHandler<>(Product.class, basic), new Object[]{productName});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return product;
    }

    public void deleteById(Long productId) {
        String sql = "delete from manager_product where product_id = ?";
        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{productId});
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

    public void insert(Product product) {
        String sql = " insert into manager_product(product_name, product_pic_url, `product_desc`, brand_id, category_id," +
                "product_price, product_stock, product_state, product_order,create_time, update_time )" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
              product.getProductName(), product.getProductPicUrl(), product.getProductDesc(),
              product.getBrandId(), product.getCategoryId(), product.getProductPrice(),
              product.getProductStock(), product.getProductState(), product.getProductOrder(), product.getCreateTime(),
              product.getUpdateTime()
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

    public void updateById(Product product) {
        String sql = "update manager_product set product_name = ?," +
                "product_pic_url =?,`product_desc` =?," +
                " brand_id = ?, category_id = ?, product_price=?, product_stock= ?,product_state= ?" +
                ",product_order = ?, create_time = ?, update_time= ?" +
                "  where product_id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
                 product.getProductName(), product.getProductPicUrl(),product.getProductDesc(), product.getBrandId(),
                    product.getCategoryId(), product.getProductPrice(), product.getProductStock(),
                    product.getProductState(), product.getProductOrder(), product.getCreateTime(),
                    product.getUpdateTime(), product.getProductId()
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

    public List<Long> productLikeProductNameList(String productName) {
        String sql = "select * from manager_product where ";
        List<Object> objects = new ArrayList<>();
        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(productName)) {
            addWhere += "  product_name like ?  and ";
            objects.add("%" + productName + "%");
        }
        sql += addWhere + " 1 = 1";
        List<Long> longList = new ArrayList<>();
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            List<Product> productList = queryRunner.query(conn, sql, new BeanListHandler<>(Product.class, basic), objects.toArray());
            for (Product product: productList) {
                longList.add(product.getProductId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return longList;
    }


    public static void main(String[] args) {
        ProductCommand productCommand = new ProductCommand();
//        Map<String, Object> stringObjectMap = productCommand.selectPage(null, "1", "5", "1", 1, 10);
//        System.out.println();
//        Long aLong = productCommand.selectByUpState();
//        Long aLong1 = productCommand.selectByDownState();
//        Long aLong2 = productCommand.selectByTightProduct();
//        Long aLong3 = productCommand.selectAllProduct();
//        System.out.println(aLong);
//        System.out.println(aLong1);
//        System.out.println(aLong2);
//        System.out.println(aLong3);
        Product product = productCommand.selectByName("测试");
        product.setProductName("测试2");
        product.setProductState(1);
        productCommand.insert(product);
//        productCommand.deleteById(product.getProductId());

//        Product product1 = productCommand.selectById(1L);
//        System.out.println(product);

        List<Long> 测试 = productCommand.productLikeProductNameList("测试");
        System.out.println(测试);
    }

}
