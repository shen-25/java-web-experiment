package com.jingdong.manager.utils;



import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @author word
 */
public class C3P0Utils {

    public static DataSource dataSource = new ComboPooledDataSource();
    public static Connection connection;


    public static Connection getConnection() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    public static void closeConnection(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = C3P0Utils.getConnection();
         PreparedStatement pstmt = connection.prepareStatement("select * from manager_user limit 0,10");
        ResultSet rs =  pstmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1));

        }
    }


}
