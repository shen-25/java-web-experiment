package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.model.entity.Department;
import com.jingdong.manager.model.vo.DepartmentNameVo;
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

public class DeptCommand {


    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public  List<DepartmentNameVo> selectDepartmentName(){
        String sql = " select dept_id ,dept_name from manager_dept";
        List<DepartmentNameVo> departmentNameVos = new ArrayList<>();
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
           departmentNameVos = queryRunner.query(conn, sql, new BeanListHandler<>(DepartmentNameVo.class, basic));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return departmentNameVos;
    }

    public void insert(Department department) {
        String sql = " insert into manager_dept (dept_name, create_time, update_time )" +
                " values (?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Department.class, basic), new Object[]{
                    department.getDeptName(), department.getCreateTime(), department.getUpdateTime()
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

    public Department selectByName(String deptName) {
        String sql = "select* from manager_dept where dept_name=? ";
        Department department = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            department = queryRunner.query(conn, sql, new BeanHandler<>(Department.class, basic), new Object[]{
                    deptName
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
        return department;
    }

    public Department selectById(Long deptId) {
        String sql = "select* from manager_dept where dept_id=? ";
        Department department = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            conn = C3P0Utils.getConnection();
            department = queryRunner.query(conn, sql, new BeanHandler<>(Department.class, basic), new Object[]{
                    deptId
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
        return department;
    }

    public void edit(Department department) {
        String sql = "update manager_dept set dept_name = ?," +
                "update_time= ?" +
                "  where dept_id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Department.class, basic), new Object[]{
                  department.getDeptName(), department.getUpdateTime(), department.getDeptId()
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

    public void deleteById(Long deptId) {
        String sql = "delete from manager_dept where dept_id = ?";
        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{deptId});
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

    public Map<String, Object> selectPage(String deptName, Integer pageNum, Integer pageSize) {
        String sql2 = "select count(*) from manager_dept where";
        String sql = "select * from manager_dept where ";
        List<Department> departmentList = null;
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(deptName)) {
            addWhere += "  dept_name like ?  and ";
            objects.add("%" + deptName + "%");
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
            departmentList = (List<Department>) qr.query(conn,sql, new BeanListHandler<>(Department.class, basic), objects.toArray());
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
        map.put("records", departmentList);
        map.put("total", total);
        return map;
    }

    public static void main(String[] args) {
        DeptCommand deptCommand = new DeptCommand();
//        List<DepartmentNameVo> departmentNameVos = deptCommand.selectDepartmentName();
//        Department department = new Department();
//        department.setDeptName("部门测试");
//        department.setCreateTime(new Date());
//        department.setUpdateTime(new Date());
//        deptCommand.insert(department);

//        Department department = deptCommand.selectByName("部门测试");
////        department.setDeptName("hhhh");
////        department.setUpdateTime(new Date());
////
////        deptCommand.edit(department);
////
//////        Department department1 = deptCommand.selectById(1L);
////        System.out.println("dd");
//        deptCommand.deleteById(7L);
        Map<String, Object> stringObjectMap = deptCommand.selectPage(null, 1, 10);
        System.out.println();
    }

}
