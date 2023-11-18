package com.yiling.open.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import org.apache.log4j.Logger;

public class DataBaseConnection {
    private static Logger logger = Logger.getLogger(DataBaseConnection.class);
    public static  String url;
    public static  String driverName;
    public static  String dbName;
    public static  String passWord;
    public static  String userName;
    public static  String FLAG   = "flag";
    public static  String RESULT = "result";

    public static final DataBaseConnection getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Connection openCon(String dbName, String dbLoginName, String dbLoginPW, String dbIp, String dbPort)
            throws Exception {
        driverName = "com.mysql.jdbc.Driver";
        userName = dbLoginName;
        passWord = dbLoginPW;
        url = "jdbc:mysql://" + dbIp + ":" + dbPort + "/" + dbName;
        try {
            Class.forName(driverName).newInstance();
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void closeConAll(Connection conn, PreparedStatement prsts, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prsts != null) {
                prsts.close();
            }
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EnterpriseDTO executeEnterpriseQuery(Connection connection, String sql) {
        ResultSet rs = null;
        PreparedStatement prsts = null;
        try {
            prsts = connection.prepareStatement(sql);
            rs = prsts.executeQuery();
            while (rs.next()) {
                EnterpriseDTO enterpriseDTO = new EnterpriseDTO();
                enterpriseDTO.setId(rs.getLong("id"));
                enterpriseDTO.setName(rs.getString("name"));
                enterpriseDTO.setLicenseNumber(rs.getString("license_number"));
                return enterpriseDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (prsts != null) {
                try {
                    prsts.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public TycEnterpriseInfoDTO executeTycQuery(Connection connection, String sql) {
        ResultSet rs = null;
        PreparedStatement prsts = null;
        try {
            prsts = connection.prepareStatement(sql);
            rs = prsts.executeQuery();
            while (rs.next()) {
                TycEnterpriseInfoDTO tycEnterpriseInfoDTO = new TycEnterpriseInfoDTO();
                tycEnterpriseInfoDTO.setCreditCode(rs.getString("credit_code"));
                tycEnterpriseInfoDTO.setName(rs.getString("name"));
                tycEnterpriseInfoDTO.setId(rs.getLong("id"));
                return tycEnterpriseInfoDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (prsts != null) {
                try {
                    prsts.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static class LazyHolder {
        private static final DataBaseConnection INSTANCE = new DataBaseConnection();
    }
}
