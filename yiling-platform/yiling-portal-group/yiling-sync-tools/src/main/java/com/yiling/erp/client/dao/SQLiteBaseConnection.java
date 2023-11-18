package com.yiling.erp.client.dao;


import java.sql.Connection;


public class SQLiteBaseConnection
{
    public static Connection connection;
    private static SQLiteBaseConnection instance;

    public static synchronized SQLiteBaseConnection getInstance()
    {
        if (instance == null) {
            instance = new SQLiteBaseConnection();
        }
        return instance;
    }

//    public void openCon()throws Exception
//    {
//        try
//        {
//            Class.forName("org.sqlite.JDBC");
//            int i = 0;
//            while (true) {
//                i++;
//                String temp = ErpCommon.filePath;
//                connection = DriverManager.getConnection("jdbc:sqlite://" + temp + "/orderIssued.db");
//                if ((connection != null) || (i > 10))
//                    break;
//            }
//        }
//        catch (ClassNotFoundException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    public void deleteData(List<String> failCodeList, String supplierId)
//    {
//        Statement statement = null;
//        try
//        {
//            StringBuffer sql = new StringBuffer();
//            openCon();
//            connection.setAutoCommit(false);
//
//            if ((failCodeList != null) && (failCodeList.size() > 0))
//            {
//                for (int i = 0; i < failCodeList.size(); i++)
//                {
//                    String code = (String)failCodeList.get(i);
//                    if (i != failCodeList.size() - 1)
//                        sql.append("'" + code + "',");
//                    else {
//                        sql.append("'" + code + "'");
//                    }
//
//                }
//
//                statement = connection.createStatement();
//                statement.setQueryTimeout(30);
//
//                if ((sql != null) && (sql.length() > 0)) {
//                    String createSql = "delete from jk_order where 1=1 and orderCode in (" + sql.toString() + ")";
//                    statement.executeUpdate(createSql);
//                }
//
//            }
//
//            connection.commit();
//        }
//        catch (Exception es)
//        {
//            PopRequest popRequest = new PopRequest();
//            ErpLog erpLog = new ErpLog();
//            erpLog.setClientLog(es.getMessage() + "堆栈信息：" + ExceptionUtils.getFullStackTrace(es));
//            erpLog.setLogApiType(Integer.valueOf(4));
//            erpLog.setLogSourceType(Integer.valueOf(1));
//            erpLog.setLogType(Integer.valueOf(1));
//            erpLog.setSupplierId(supplierId);
//            erpLog.setRemark("订单下发错误记录");
//            popRequest.addErpLog(erpLog);
//            try
//            {
//                connection.rollback();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            logger.error(es.getMessage());
//        }
//        finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null)
//                    connection.close();
//            }
//            catch (Exception e) {
//                logger.error("关闭数据库连接失败" + e.getMessage());
//            }
//        }
//    }
//
//    public List<String> queryFailOrderCode(String supplierId)
//    {
//        String createSql = "create table if not exists jk_order (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,orderCode VARCHAR(200),queryCount INTEGER)";
//        String querySql = "select orderCode,queryCount from jk_order where 1=1 and queryCount<3";
//
//        List list = new ArrayList();
//        ResultSet rs = null;
//        Statement statement = null;
//        try
//        {
//            openCon();
//            connection.setAutoCommit(false);
//
//            statement = connection.createStatement();
//
//            statement.executeUpdate(createSql);
//
//            rs = statement.executeQuery(querySql);
//            while (rs.next())
//            {
//                String orderCode = rs.getString("orderCode");
//
//                list.add(orderCode);
//            }
//
//            if ((list != null) && (list.size() > 0)) {
//                for (String innerCode : list) {
//                    String updateSql = "update jk_order set queryCount=queryCount+1 where orderCode='" + innerCode + "'";
//                    statement.executeUpdate(updateSql);
//                }
//
//            }
//
//            String deleteSql = "delete from jk_order where 1=1 and queryCount>=3";
//            statement.executeUpdate(deleteSql);
//
//            connection.commit();
//        }
//        catch (Exception e)
//        {
//            PopRequest popRequest = new PopRequest();
//            ErpLog erpLog = new ErpLog();
//            erpLog.setClientLog(e.getMessage() + "堆栈信息：" + ExceptionUtils.getFullStackTrace(e));
//            erpLog.setLogApiType(Integer.valueOf(4));
//            erpLog.setLogSourceType(Integer.valueOf(1));
//            erpLog.setLogType(Integer.valueOf(1));
//            erpLog.setSupplierId(supplierId);
//            erpLog.setRemark("订单下发错误记录");
//            popRequest.addErpLog(erpLog);
//            logger.error(e.getMessage());
//            try
//            {
//                connection.rollback();
//            }
//            catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null)
//                    connection.close();
//            }
//            catch (Exception e) {
//                logger.error("关闭数据库连接失败" + e.getMessage());
//            }
//        }
//
//        return list;
//    }
//
//    public void insertOrderInfo(List<String> orderCodeList, String supplierId) {
//        Statement statement = null;
//        try
//        {
//            String createSql = "create table if not exists jk_order (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,orderCode VARCHAR(200),queryCount INTEGER)";
//            openCon();
//            connection.setAutoCommit(false);
//
//            statement = connection.createStatement();
//            statement.setQueryTimeout(30);
//            statement.executeUpdate(createSql);
//
//            for (String orderCode : orderCodeList) {
//                String inserSql = "insert into jk_order(orderCode,queryCount) values('" + orderCode + "',0)";
//                statement.executeUpdate(inserSql);
//            }
//            connection.commit();
//        }
//        catch (Exception es)
//        {
//            PopRequest popRequest = new PopRequest();
//            ErpLog erpLog = new ErpLog();
//            erpLog.setClientLog(es.getMessage() + "堆栈信息：" + ExceptionUtils.getFullStackTrace(es));
//            erpLog.setLogApiType(Integer.valueOf(4));
//            erpLog.setLogSourceType(Integer.valueOf(1));
//            erpLog.setLogType(Integer.valueOf(1));
//            erpLog.setSupplierId(supplierId);
//            erpLog.setRemark("订单下发错误记录");
//            popRequest.addErpLog(erpLog);
//            try
//            {
//                connection.rollback();
//            }
//            catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null)
//                    connection.close();
//            }
//            catch (Exception e) {
//                logger.error("关闭数据库连接失败" + e.getMessage());
//            }
//        }
//    }
//
//    public static void main(String[] args)
//    {
//        try
//        {
//            Class.forName("org.sqlite.JDBC");
//            int i = 0;
//            while (true) {
//                i++;
//                String temp = "";//ErpCommon.filePath;
//                Connection connection1 = DriverManager.getConnection("jdbc:sqlite://" + temp + "/orderIssued.db");
//                if ((connection1 != null) || (i > 10))
//                    break;
//            }
//        }
//        catch (Exception e) {
//            System.out.print(e.getMessage());
//        }
//    }
}
