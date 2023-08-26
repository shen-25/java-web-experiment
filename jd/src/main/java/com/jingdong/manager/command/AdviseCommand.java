package com.jingdong.manager.command;

import com.jingdong.manager.model.entity.Advise;
import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdviseCommand {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());


    public List<Advise> selectAll() {
        String sql = "select * from manager_advise";
        List<Advise> adviseList = new ArrayList<>();
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            adviseList = qr.query(conn, sql, new BeanListHandler<>(Advise.class, basic));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adviseList;
    }

    public static void main(String[] args) {
        AdviseCommand adviseCommand = new AdviseCommand();
        List<Advise> advises = adviseCommand.selectAll();
        System.out.println(advises);
    }
}
