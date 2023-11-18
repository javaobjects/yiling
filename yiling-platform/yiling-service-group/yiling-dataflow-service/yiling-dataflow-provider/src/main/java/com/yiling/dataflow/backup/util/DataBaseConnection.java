package com.yiling.dataflow.backup.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.yiling.dataflow.config.DatasourceConfig;
import com.yiling.framework.common.exception.ServiceException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库操作工具类
 *
 * @author: houjie.sun
 * @date: 2022/7/12
 */
@Slf4j
@Component
public class DataBaseConnection {

    public static String url;
    public static String driverName;
    public static String dbName;
    public static String passWord;
    public static String userName;
    public static String FLAG = "flag";
    public static String RESULT = "result";

    /**
     * 数据库操作
     */
    private static final String SELECT_SQL = "SELECT * FROM ";

    private static class LazyHolder {
        private static final DataBaseConnection INSTANCE = new DataBaseConnection();
    }

    public static final DataBaseConnection getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Connection openMysqlCon(DatasourceConfig datasourceConfig) {
        driverName = "com.mysql.jdbc.Driver";
        userName = datasourceConfig.getUsername();
        passWord = datasourceConfig.getPassword();
        url = datasourceConfig.getUrl();
        try {
            Class.forName(driverName).newInstance();
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            throw new ServiceException("[ERP流向数据备份], 连接数据库出错", e);
        }
    }

    /**
     * 获取数据库下的所有表名
     *
     * @param datasourceConfig 数据库连接信息
     * @return
     * @throws Exception
     */
    public List<String> getTableNames(DatasourceConfig datasourceConfig, String tableNamePrefix) {
        List<String> tableNames = new ArrayList<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement prsts = null;
        String tableSql = "";
        DatabaseMetaData meta = null;
        dbName = datasourceConfig.getUsername();
        try {
            // 数据库连接
            connection = openMysqlCon(datasourceConfig);
            stmt = connection.createStatement();
            meta = connection.getMetaData();
            // 获取数据库的元数据, 从元数据中获取到所有的用户表名（过滤掉系统表）
            log.info("[ERP流向数据备份]，获取数据库表名, 数据库名称：" + dbName);
            tableSql = "TABLE";
            // rs = meta.getTables(null, null,  tablename, null );
            rs = meta.getTables(null, null, null, new String[]{ tableSql });
            while (rs.next()) {
                String tableName = rs.getString(3);
                if(tableName.startsWith(tableNamePrefix)){
                    tableNames.add(tableName);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new ServiceException("[ERP流向数据备份], 获取数据库表名sql异常", se);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("[ERP流向数据备份], 获取数据库表名异常", e);
        } finally {
            closeConAll(connection, prsts, rs);
        }
        return tableNames;
    }

    /**
     * 创建表
     *
     * @param sqls
     * @return
     */
    public Boolean createTable(List<String> sqls, DatasourceConfig datasourceConfig) {
        boolean flag = true;
        String result = "";
        Statement stmt = null;
        Connection connection = null;
        boolean createBack = true;
        try {
            connection = openMysqlCon(datasourceConfig);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            for (String sql : sqls) {
                log.info("[ERP流向数据备份], 创建备份表执行sql:{}" + sql);
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            connection.commit();
        } catch (Exception e) {
            createBack = false;
            log.error("[ERP流向数据备份], 创建备份表出错:{}" + e);
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException sqlException) {
                log.error("[ERP流向数据备份], 创建备份表, 回滚失败:{}" + sqlException);
                sqlException.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                new ServiceException("[ERP流向数据备份], 创建备份表, 关闭数据库连接失败", e);
            }
        }
        return createBack;
    }

    public List<String> getSqlResult(String sql) {
        List results = new ArrayList();
        String sql1 = sql.replace("CREATE", "  @CREATE  ").replace("ALTER", "  @ALTER  ");
        String[] sqls = sql1.split("@");
        for (String s : sqls) {
            if (StrUtil.isBlank(s)) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * 获取表名称、字段名称
     *
     * @param tableColumnMap 表信息数据：Map<表名, Map<字段名, 字段类型>>
     * @return
     */
    public void initBackupTableInfo(List<String> tableNames, Map<String, Map<String, String>> tableColumnMap, DatasourceConfig datasourceConfig) {
        if (CollUtil.isEmpty(tableNames)) {
            return;
        }
        for (String tableName : tableNames) {
            Map<String, String> columnNameTypeMap = null;
            try {
                columnNameTypeMap = getInstance().getColumnNameAndType(tableName, datasourceConfig);
            } catch (Exception e) {
                new ServiceException("[ERP流向数据备份], 取数据库表字段名失败, tableName:" + tableName, e);
            }
            tableColumnMap.put(tableName, columnNameTypeMap);
        }
    }

    /**
     * 获取表中所有字段名称、字段类型
     *
     * @param tableName 表名
     * @return
     * @throws Exception
     */
    public Map<String, String> getColumnNameAndType(String tableName, DatasourceConfig datasourceConfig) throws Exception {
        Map<String, String> columnNameTypeMap = new HashMap<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement prsts = null;
        String tableSql = "";
        try {
            // 数据库连接
            connection = openMysqlCon(datasourceConfig);
            tableSql = SELECT_SQL + tableName;
            prsts = connection.prepareStatement(tableSql);
            // 结果集元数据
            ResultSetMetaData rsm = prsts.getMetaData();
            // 表列数
            int size = rsm.getColumnCount();
            for (int i = 0; i < size; i++) {
                // key-字段名称;  value-字段类型
                columnNameTypeMap.put(rsm.getColumnName(i + 1), rsm.getColumnTypeName(i + 1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("[ERP流向数据备份], 获取数据库表字段名称和类型异常, tableName -> {}", tableName, e);
        } finally {
            closeConAll(connection, prsts, rs);
            return columnNameTypeMap;
        }
    }

    public void closeConAll(Connection conn, PreparedStatement prsts, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prsts != null) {
                prsts.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            new ServiceException("[ERP流向数据备份], 关闭数据库连接失败", e);
        }
    }

    public String insertBackupTableInfo(String repeatProcessSql, String insertSql, DatasourceConfig datasourceConfig) {
        String id = null;
        Statement stmt = null;
        Connection connection = null;
        ResultSet rs = null;
        String executeSql = "";
        try {
            connection = openMysqlCon(datasourceConfig);
            connection.setAutoCommit(false);
            stmt = connection.createStatement();

            if (StrUtil.isBlank(insertSql)) {
                return "";
            }

            executeSql = repeatProcessSql;
            rs = stmt.executeQuery(executeSql);
            if (rs.next()) {
                id = String.valueOf(rs.getObject(1));
                if (StrUtil.isNotBlank(id)) {
                    log.warn("[ERP流向数据备份]，此条数据已备份:" + repeatProcessSql);
                    return id;
                }
            }

            log.info("[ERP流向数据备份], 备份表插入数据sql===" + insertSql);
            if ((insertSql != null) && (!"".equals(insertSql.trim()))) {
                executeSql = insertSql;
                stmt.executeUpdate(executeSql, Statement.RETURN_GENERATED_KEYS);
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = String.valueOf(rs.getObject(1));
                }
            }
            connection.commit();
        } catch (Exception e) {
            id = "";
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException sqlException) {
                log.error("[ERP流向数据备份]，sqlException:" + sqlException.getMessage());
            }
            log.error("[ERP流向数据备份], 备份表插入数据失败，repeatProcessSql:{}, insertSql:{}, exception:{}", repeatProcessSql, insertSql, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                new ServiceException("[ERP流向数据备份], 备份表插入数据关闭数据库连接失败", e);
            }
        }
        return id;
    }

}
