package com.yiling.erp.client.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import com.yiling.erp.client.util.StringUtil;


@Service
public class SQLiteHelper {

    public Connection getConnection(String dbPath, String database)
            throws Exception {
        Class.forName("org.sqlite.JDBC");
        int i = 0;
        Connection connection;
        while (true) {
            i++;
            connection = DriverManager.getConnection("jdbc:sqlite://" + dbPath + "/" + database);
            if ((connection != null) || (i > 10)) {
                break;
            }
        }
        return connection;
    }

    public String queryString(String dbPath, String querySql, String database)
            throws Exception {
        ResultSet rs = null;
        Statement statement = null;
        Connection connection = null;
        String result;
        try {
            connection = getConnection(dbPath, database);
            statement = connection.createStatement();
            rs = statement.executeQuery(querySql);
            if ((rs != null) && (rs.next())) {
                result = rs.getString(1);
            } else {
                result = null;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return result;
    }

    public void executeUpdate(String dbPath,  String database,String sql)
            throws Exception {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = getConnection(dbPath, database);
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public boolean isExistTable(String dbPath, String database, String tableName) {
        String sql = "select * from sqlite_master where type = 'table' and name = '" + tableName + "'";
        try {
            String result = queryString(dbPath, sql, database);
            if (StringUtil.isEmpty(result)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        SQLiteHelper sqLiteHelper = new SQLiteHelper();
//        sqLiteHelper.getConnection("test.db");
//        String sql = "CREATE TABLE user " +
//                "(ID INT PRIMARY KEY     NOT NULL," +
//                " NAME           TEXT    NOT NULL, " +
//                " AGE            INT     NOT NULL)";
//
//        sqLiteHelper.executeUpdate(sql,"test.db");

//        String insert = "INSERT INTO user (ID,NAME,AGE) " +
//                "VALUES (2, 'Paul', 32);";

    }
}
