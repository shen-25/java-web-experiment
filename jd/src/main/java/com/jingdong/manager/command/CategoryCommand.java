package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.Category;
import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.vo.CategoryKeyValueVo;
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

public class CategoryCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());


    public void insert(Category category) {
        String sql = " insert into manager_category (category_name,category_state," +
                "category_order,  create_time, update_time )" +
                " values (?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
                    category.getCategoryName(), category.getCategoryState(),
                    category.getCategoryOrder(), category.getCreateTime(), category.getUpdateTime()
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


    public Category selectByCategoryName(String categoryName) {
        String sql = "select* from manager_category where category_name=? ";
        Category category = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            category = queryRunner.query(conn, sql, new BeanHandler<>(Category.class, basic), new Object[]{
                    categoryName
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
        return category;
    }

    public Category selectById(Long categoryId) {
        String sql = "select* from manager_category where category_id=? ";
        Category category = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            category = queryRunner.query(conn, sql, new BeanHandler<>(Category.class, basic), new Object[]{
                    categoryId
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
        return category;
    }

    public void edit(Category category) {
        String sql = "update manager_category set category_name = ?," +
                "category_state =?,`category_order` =?, update_time= ?" +
                "  where category_id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Menu.class, basic), new Object[]{
                   category.getCategoryName(), category.getCategoryState(),
                    category.getCategoryOrder(), category.getUpdateTime(),
                    category.getCategoryId()
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

    public void deleteById(Long categoryId) {
        String sql = "delete from manager_category where category_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
             queryRunner.update(conn, sql, new Object[]{categoryId});
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

    public Map<String, Object> selectPage(String categoryName, Integer pageNum, Integer pageSize) {
        String sql2 = "select count(*) from manager_category where";
        String sql = "select * from manager_category where ";
        List<Category> categories = null;
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(categoryName)) {
            addWhere += "  category_name like ?  and ";
            objects.add("%" + categoryName + "%");
        }
        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql += (addWhere +  " 1 = 1 ORDER BY category_order  limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
            categories = (List<Category>) qr.query(conn,sql, new BeanListHandler<>(Category.class, basic), objects.toArray());
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
        map.put("records", categories);
        map.put("total", total);
        return map;
    }

    public List<CategoryKeyValueVo> selectCategoryKeyValue(){
        String sql = "select category_id as categoryId ,category_name as categoryName from manager_category where category_state = 0 order by category_order";
        QueryRunner queryRunner = new QueryRunner();
        conn = C3P0Utils.getConnection();
        List<CategoryKeyValueVo> categoryKeyValueVos = new ArrayList<>();
        try {
            categoryKeyValueVos = queryRunner.query(conn, sql, new BeanListHandler<>(CategoryKeyValueVo.class, basic));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return categoryKeyValueVos;
    }


        public static void main(String[] args) {
        CategoryCommand categoryCommand = new CategoryCommand();
//        Category category = new Category();
//        category.setCategoryName("测试");
//        category.setCategoryOrder(111);
//        category.setCategoryState(1);
//        category.setCreateTime(new Date());
//        category.setUpdateTime(new Date());
//        categoryCommand.insert(category);
//        Category category1 = categoryCommand.selectById(1L);
//        System.out.println(category1);
            Map<String, Object> stringObjectMap = categoryCommand.selectPage(null, 1, 10);
            System.out.println(stringObjectMap);
//        Category category = categoryCommand.selectByCategoryName("测试");
//        System.out.println(category);
//        category.setCategoryName("dddd");
//        category.setUpdateTime(new Date());
//        category.setCreateTime(new Date());
//
//        categoryCommand.edit(category);
//        categoryCommand.deleteById(1L);
            List<CategoryKeyValueVo> categoryKeyValueVos = categoryCommand.selectCategoryKeyValue();
            System.out.println();
        }
}
