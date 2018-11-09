package com.webex.dap.data.jdbc.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by harry on 6/6/18.
 */
public class FetchSizeDemo {
    public static void main(String[] args) throws Exception {
//        String url = "jdbc:mysql://10.225.17.49:3306/oneportal?useCursorFetch=true";
        String url = "jdbc:oracle:thin:@10.252.8.134:1909:stapdb1";
        Properties properties = new Properties();
        properties.setProperty("user", "seuser");
        properties.setProperty("password", "se#1stapdb");
//        properties.setProperty("useCursorFetch","true");
        Connection connection = DriverManager.getConnection(url, properties);
        connection.setAutoCommit(false);

        String sql = "SELECT t.* FROM stapuser.stap_inc_analyze_temp t where 1=0";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);

//            pstmt.setFetchSize(10);
//            pstmt.setFetchSize(Integer.MIN_VALUE);

            System.out.println("ps.getQueryTimeout():" + pstmt.getQueryTimeout());
            System.out.println("ps.getFetchSize():" + pstmt.getFetchSize());
            System.out.println("ps.getFetchDirection():" + pstmt.getFetchDirection());
            System.out.println("ps.getMaxFieldSize():" + pstmt.getMaxFieldSize());

            rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();

            System.out.println(col);

//            while (rs.next()) {
//                String id_b = rs.getString("id_b");
//                System.out.println(id_b);
//
//                String d_date = rs.getString("d_date");
//                System.out.println(d_date);
//
//                String d_timestamp = rs.getString("d_timestamp");
//                System.out.println(d_timestamp);
////                rs.get
////                for (int i = 1; i <= col; i++) {
////                    System.out.print(rs.getObject(i));
////                }
////                System.out.println("");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.commit();
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null){
                pstmt.close();
            }

            if (connection != null){
                connection.close();
            }
        }
    }
}
