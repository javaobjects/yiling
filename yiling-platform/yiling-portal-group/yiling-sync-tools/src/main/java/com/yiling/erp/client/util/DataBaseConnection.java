package com.yiling.erp.client.util;

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
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.erp.client.common.DbTypeEnum;
import com.yiling.open.erp.dto.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class DataBaseConnection {
    public static String url;
    public static String driverName;
    public static String dbName;
    public static String passWord;
    public static String userName;
    public static String FLAG   = "flag";
    public static String RESULT = "result";

    private static final String PASSWORD   = "mysql";
    /**
     * 数据库操作
     */
    private static final String SELECT_SQL = "SELECT * FROM ";

    public static final DataBaseConnection getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Connection openCon(SysConfig sysConfig) throws Exception {

        if (sysConfig == null) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }

        if ((sysConfig.getDbType() == null) || (sysConfig.getDbType().trim().equals(""))) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }

        if (sysConfig.getDbType().trim().equals(DbTypeEnum.MYSQL.getCode())) {
            return openMysqlCon(sysConfig);
        }
        if (sysConfig.getDbType().trim().equals(DbTypeEnum.SQL_SERVER.getCode())) {
            return openSqlServerCon(sysConfig);
        }
        if (sysConfig.getDbType().trim().equals(DbTypeEnum.SQL_SERVER_2000.getCode())) {
            return openSqlServerConForVersion2000(sysConfig);
        }
        if (sysConfig.getDbType().trim().equals(DbTypeEnum.ORACLE.getCode())) {
            if ((sysConfig.getOracleType() == null) || (sysConfig.getOracleType().trim().equals(""))) {
                throw new ErpException("数据库配置有误，oracle类型不能为空!");
            }
            return openOracleCon(sysConfig);
        }
        if (sysConfig.getDbType().trim().equals(DbTypeEnum.DB2.getCode())) {
            return openDB2Con(sysConfig);
        }
        if (sysConfig.getDbType().trim().equals(DbTypeEnum.ODBC.getCode())) {
            return openSqlOdbc(sysConfig);
        }
        if (sysConfig.getDbType().trim().equals(DbTypeEnum.ODBC_DBF.getCode())) {
            return openSqlOdbcDbf(sysConfig);
        }
        throw new ErpException("数据库类型有误，请选择正确的数据类型");
    }

    public Connection openSqlOdbcDbf(SysConfig sysConfig) throws Exception {
        driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
        dbName = sysConfig.getDbName();
        url = "jdbc:odbc:" + dbName.toUpperCase();
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url, "", "");
        } catch (Exception e) {
            throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
        }
    }

    public Connection openSqlOdbc(SysConfig sysConfig) throws Exception {
        driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
        dbName = sysConfig.getDbName();
        userName = sysConfig.getDbLoginName();
        passWord = StrUtil.isEmpty(sysConfig.getDbLoginPW()) ? null : sysConfig.getDbLoginPW();
        url = "jdbc:odbc:" + dbName.toUpperCase();
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
        }
    }

    public Connection openOracleCon(SysConfig sysConfig) throws Exception {
        String oracleType = sysConfig.getOracleType();

        driverName = "oracle.jdbc.driver.OracleDriver";
        dbName = sysConfig.getDbName();
        userName = sysConfig.getDbLoginName();
        passWord = StrUtil.isEmpty(sysConfig.getDbLoginPW()) ? null : sysConfig.getDbLoginPW();
        url = "jdbc:oracle:thin:@" + sysConfig.getDbIp() + ":" + sysConfig.getDbPort() + ":" + sysConfig.getOracleSid();

        if (oracleType.equals("serviceName")) {
            url = "jdbc:oracle:thin:@//" + sysConfig.getDbIp() + ":" + sysConfig.getDbPort() + "/" + sysConfig.getOracleSid();
        }
        try {
            Class.forName(driverName).newInstance();
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
        }
    }

    public Connection openMysqlCon(SysConfig sysConfig) throws Exception {
        driverName = "com.mysql.jdbc.Driver";
        dbName = sysConfig.getDbName();
        userName = sysConfig.getDbLoginName();
        passWord = StrUtil.isEmpty(sysConfig.getDbLoginPW()) ? null : sysConfig.getDbLoginPW();
        url = "jdbc:mysql://" + sysConfig.getDbIp() + ":" + sysConfig.getDbPort() + "/" + sysConfig.getDbName() + "?useUnicode=true&characterEncoding=utf8";
        try {
            Class.forName(driverName).newInstance();
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
        }
    }

    public Connection openSqlServerCon(SysConfig sysConfig) throws Exception {
        driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        dbName = sysConfig.getDbName();
        userName = sysConfig.getDbLoginName();
        passWord = StrUtil.isEmpty(sysConfig.getDbLoginPW()) ? null : sysConfig.getDbLoginPW();
        if (StrUtil.isNotEmpty(sysConfig.getDbPort())) {
            url = "jdbc:sqlserver://" + sysConfig.getDbIp() + ":" + sysConfig.getDbPort() + ";" + "DatabaseName=" + sysConfig.getDbName();
        } else {
            url = "jdbc:sqlserver://" + sysConfig.getDbIp() + ";" + "DatabaseName=" + sysConfig.getDbName();
        }
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            log.error("连接数据库出错", e);
            throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
        }
    }

    public Connection openSqlServerConForVersion2000(SysConfig sysConfig) throws Exception {
        driverName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        dbName = sysConfig.getDbName();
        userName = sysConfig.getDbLoginName();
        passWord = StrUtil.isEmpty(sysConfig.getDbLoginPW()) ? null : sysConfig.getDbLoginPW();
        if (StrUtil.isEmpty(userName) && StrUtil.isEmpty(passWord)) {
            url = "jdbc:microsoft:sqlserver://" + sysConfig.getDbIp() + ";integratedSecurity=true;" + "DatabaseName=" + sysConfig.getDbName();
            try {
                Class.forName(driverName);
                return DriverManager.getConnection(url);
            } catch (Exception e) {
                throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
            }
        } else {
            url = "jdbc:microsoft:sqlserver://" + sysConfig.getDbIp() + ":" + sysConfig.getDbPort() + ";" + "DatabaseName=" + sysConfig.getDbName();
            try {
                Class.forName(driverName);
                return DriverManager.getConnection(url, userName, passWord);
            } catch (Exception e) {
                throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
            }
        }

    }

    public Connection openDB2Con(SysConfig sysConfig) throws Exception {
        driverName = "com.ibm.db2.jcc.DB2Driver";
        dbName = sysConfig.getDbName();
        userName = sysConfig.getDbLoginName();
        passWord = StrUtil.isEmpty(sysConfig.getDbLoginPW()) ? null : sysConfig.getDbLoginPW();
        url = "jdbc:db2://" + sysConfig.getDbIp() + ":" + sysConfig.getDbPort() + "/" + sysConfig.getDbName();
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            throw new ErpException("连接数据库出错，请填写正确的数据库配置", e);
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
            new ErpException("关闭数据库连接失败", e);
        }
    }

    public List<Map<Object, Object>> executeQuery(SysConfig sysConfig, String sql) throws Exception {
        List list = null;
        ResultSet rs = null;
        PreparedStatement prsts = null;
        Connection connection = null;
        Map map = null;
        try {
            connection = openCon(sysConfig);
            prsts = connection.prepareStatement(sql);
            rs = prsts.executeQuery();
            list = new ArrayList<Map<Object, Object>>();
            ResultSetMetaData rsm = rs.getMetaData();
            while (rs.next()) {
                map = new HashMap(rsm.getColumnCount());
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    map.put(rsm.getColumnLabel(i).toLowerCase(), rs.getObject(rsm.getColumnLabel(i)));
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErpException("查询数据库出错：" + sql, e);
        } finally {
            closeConAll(connection, prsts, rs);
        }
        return list;
    }

    public Map<String, Object> createOrderMidTable(SysConfig sysConfig, List<String> sqls) {
        boolean flag = true;
        String result = "";
        Statement stmt = null;
        Connection connection = null;
        Map createBack = new HashMap();
        try {
            connection = openCon(sysConfig);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            for (String sql : sqls) {
                log.debug("订单中间表执行sql:" + sql);
                stmt.addBatch(sql);
            }

            stmt.executeBatch();
            connection.commit();
        } catch (Exception e) {
            flag = false;
            result = "建中间表:" + e.getMessage();
            log.error("error数据库创建订单中间表出错:[" + e.getMessage() + "]");
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException sqlException) {
                log.error("数据库创建订单中间表  回滚失败=" + sqlException.getMessage());
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
                new ErpException("关闭数据库连接失败", e);
            }
        }
        createBack.put(FLAG, Boolean.valueOf(flag));
        createBack.put(RESULT, result);
        return createBack;
    }

    @Deprecated
    private String dealSqlLoseSpace(String sql) {
        sql = sql.replaceAll("from", " from ").replaceAll("FROM", " FROM ").replaceAll("where", " where ").replaceAll("WHERE", " WHERE ")
                .replaceAll("left join", " left join ").replaceAll("left join", " left join ").replaceAll("right join", " right join ")
                .replaceAll("RIGHT JOIN", " RIGHT JOIN ").replaceAll("group by", " group by ").replaceAll("GROUP BY", " GROUP BY ")
                .replaceAll("order by", " order by ").replaceAll("ORDER BY", " ORDER BY ").replaceAll("case when", " case when ")
                .replaceAll("CASE WHEN", " CASE WHEN ");
        return sql;
    }

    public String updateErpSn(SysConfig sysConfig, String sql) {
        String id = null;
        Statement stmt = null;
        Connection connection = null;
        ResultSet rs = null;
        String executeSql = "";
        try {
            connection = openCon(sysConfig);
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            if ((sql != null) && (!"".equals(sql.trim()))) {
                stmt.executeUpdate(sql);
            }
            connection.commit();
        } catch (Exception e) {
            id = "";
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException sqlException) {
                log.error("插入发货单回滚失败=" + sqlException.getMessage());
            }
            log.error("插入发货单下发数据失败:[" + e.getMessage() + "]");
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
                new ErpException("关闭数据库连接失败", e);
            }
        }
        return id;
    }

    public String insertOrderIssuedInfo(SysConfig sysConfig, String repeatProcessSql, String masterOrderSql, String detailOrderSql) {
        String id = null;
        Statement stmt = null;
        Connection connection = null;
        ResultSet rs = null;
        String executeSql = "";
        try {
            connection = openCon(sysConfig);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();

            if (StringUtils.isNotBlank(repeatProcessSql)) {
                executeSql = repeatProcessSql;
                rs = stmt.executeQuery(executeSql);
                if (rs.next()) {
                    id = String.valueOf(rs.getObject(1));
                    if (!StringUtil.isEmpty(id)) {
                        log.error("订单信息已存在，订单重复下发:" + repeatProcessSql);
                        return id;
                    }
                }
            }

            log.info("订单主sql===" + masterOrderSql);
            if ((masterOrderSql != null) && (!"".equals(masterOrderSql.trim()))) {
                executeSql = masterOrderSql;
                if (sysConfig.getDbType().trim().equals(DbTypeEnum.SQL_SERVER_2000.getCode())) {
                    stmt.executeUpdate(executeSql);
                    rs = stmt.executeQuery("select @@IDENTITY as id");
                } else {
                    stmt.executeUpdate(executeSql, Statement.RETURN_GENERATED_KEYS);
                    rs = stmt.getGeneratedKeys();
                }
                if (rs.next()) {
                    id = String.valueOf(rs.getObject(1));
                }
            }

            log.info("订单子sql===" + detailOrderSql);
            if (StrUtil.isNotEmpty(detailOrderSql)) {
                for (String sql : detailOrderSql.split(";")) {
                    executeSql = sql;
                    stmt.executeUpdate(executeSql);
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
                log.error("插入订单回滚失败=" + sqlException.getMessage());
            }
            log.error("插入订单下发数据失败:[" + e.getMessage() + "]", e);
            log.error("插入订单下发数据sysConfig{}repeatProcessSql{}masterOrderSql{}detailOrderSql{}", JSON.toJSONString(sysConfig), repeatProcessSql, masterOrderSql, detailOrderSql);
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
                new ErpException("关闭数据库连接失败", e);
            }
        }
        return id;
    }

    public int getTotalNum(SysConfig sysConfig, String sql) throws Exception {
        int total = 0;

        if ((sql != null) && (!sql.trim().equals(""))) {
            String countSql = "select count(*) NUM from (" + sql + ") lol";
            log.debug("根据sql获取数据：" + countSql);
            List<Map<Object, Object>> countResult = executeQuery(sysConfig, countSql);
            try {
                if ((countResult != null) && (countResult.size() > 0)) {
                    for (Map<Object, Object> map : countResult) {
                        total = Integer.valueOf(map.get("NUM").toString()).intValue();
                    }
                }
            } catch (Exception e) {
                throw new ErpException("获取集合大小失败", e);
            }
        }
        return total;
    }

    private static class LazyHolder {
        private static final DataBaseConnection INSTANCE = new DataBaseConnection();
    }

    /**
     * 获取指定表的所有字段名称
     *
     * @param sysConfig 连接对象
     * @param tableName 表名称
     * @return 该表所有的字段名称
     * @throws SQLException
     */
    public Map<String, String> getColumns(SysConfig sysConfig, String tableName) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();
        try {
            connection = openCon(sysConfig);
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getColumns(connection.getCatalog(), null, tableName, null);
            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME");
                String type = rs.getString("TYPE_NAME");
                map.put(name, type);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return map;
    }

    /**
     * 获取数据库下的所有表名
     *
     * @param sysConfig 系统配置
     * @return
     * @throws Exception
     */
    public List<String> getTableNames(SysConfig sysConfig) throws Exception {
        List<String> tableNames = new ArrayList<>();
        String dbType = sysConfig.getDbType();
        String dbName = sysConfig.getDbName();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement prsts = null;
        String tableSql = "";

        DatabaseMetaData meta = null;
        try {
            // 数据库连接
            connection = openCon(sysConfig);
            stmt = connection.createStatement();
            meta = connection.getMetaData();
            // 获取数据库的元数据
//            prsts = connection.prepareStatement(tableSql);
            // 从元数据中获取到所有的用户表名（过滤掉系统表）
            // sqlserver
            if (Utils.isSqlserver(dbType) || Utils.isOdbc(dbType)) {
                log.info("[配置信息]，数据库类型：" + dbType + ", 数据库名称：" + dbName);
                tableSql = "SELECT name FROM " + sysConfig.getDbName() + "..sysobjects WHERE xtype = 'U' and name like 'yiling%' ORDER BY name";
                rs = stmt.executeQuery(tableSql);
                getTableNamesByDbType(tableNames, rs, "sqlserver");
            }
            // mysql
            if (Utils.isMysql(dbType)) {
                log.info("[配置信息]，数据库类型：" + dbType + ", 数据库名称：" + dbName);
                tableSql = "TABLE";
                rs = meta.getTables(null, null, null, new String[]{tableSql});
                getTableNamesByDbType(tableNames, rs, "mysql");
            }
            // oracle
            if (Utils.isOracle(dbType)) {
                log.info("[配置信息]，数据库类型：" + dbType + ", 数据库名称：" + dbName);
                tableSql = "SELECT TABLE_NAME FROM all_tables WHERE owner = '" + dbName + "'";
                rs = stmt.executeQuery(tableSql);
                getTableNamesByDbType(tableNames, rs, "oracle");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("获取数据库表名异常, dbName -> {}", sysConfig.getDbName(), e);
        } finally {
            closeConAll(connection, prsts, rs);
            return tableNames;
        }
    }

    private void getTableNamesByDbType(List<String> tableNames, ResultSet rs, String dbType) throws SQLException {
        while (rs.next()) {
            String tableName = "";
            if ("mysql".equalsIgnoreCase(dbType)) {
                tableName = rs.getString(3);
            } else if ("sqlserver".equalsIgnoreCase(dbType) || "odbc".equalsIgnoreCase(dbType) || "odbc-dbf".equalsIgnoreCase(dbType)) {
                tableName = rs.getString("name");
            } else if ("oracle".equalsIgnoreCase(dbType)) {
                tableName = rs.getString("TABLE_NAME");
            }
            tableNames.add(tableName);
        }
    }

    /**
     * 获取表中所有字段名称、字段类型
     *
     * @param sysConfig 系统配置
     * @param tableName 表名
     * @return
     * @throws Exception
     */
    public Map<String, String> getColumnNameAndType(SysConfig sysConfig, String tableName) throws Exception {
        Map<String, String> columnNameTypeMap = new HashMap<>();
        String dbType = sysConfig.getDbType();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement prsts = null;
        String tableSql = "";
        try {
            // 数据库连接
            connection = openCon(sysConfig);
            // oracle
            if (Utils.isOracle(dbType)) {
                tableSql = SELECT_SQL + sysConfig.getDbName() + "." + tableName;
            } else {
                // sqlserver, mysql
                tableSql = SELECT_SQL + tableName;
            }
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
            log.error("获取数据库表字段名称和类型异常, tableName -> {}", tableName, e);
        } finally {
            closeConAll(connection, prsts, rs);
            return columnNameTypeMap;
        }
    }

    /**
     * 获取表名称、字段名称
     *
     * @param sysConfig      数据库配置信息
     * @param tableColumnMap 表信息数据：Map<表名, Map<字段名, 字段类型>>
     * @return
     */
    public void initTableInfo(SysConfig sysConfig, Map<String, Map<String, String>> tableColumnMap) throws ErpException {
        try {
            List<String> tableNames = getInstance().getTableNames(sysConfig);
            List<String> yilingTableNames = tableNames.stream().filter(e -> StrUtil.startWith(e.toLowerCase(), InitErpConfig.ERP_TABLE_NAME)).collect(Collectors.toList());
            if (CollUtil.isEmpty(yilingTableNames)) {
                throw new ErpException("没有获取到以岭数据库表名");
            }

            for (String tableName : yilingTableNames) {
                Map<String, String> columnNameTypeMap = getInstance().getColumnNameAndType(sysConfig, tableName);
                tableColumnMap.put(tableName, columnNameTypeMap);
            }
        } catch (Exception e) {
            throw new ErpException("获取数据库表字段名失败", e);
        }
    }

    public boolean columnNameIsEexist(String columnName, PreparedStatement prsts) {
        // 判断列名是否存在
        // 结果集元数据
        ResultSetMetaData rsm = null;
        try {
            rsm = prsts.getMetaData();
            // 表列数
            int size = rsm.getColumnCount();
            for (int i = 0; i < size; i++) {
                // key-字段名称;  value-字段类型
                String name = rsm.getColumnName(i + 1);
                if (ObjectUtil.equal(columnName, name)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("获取数据库表字段名称异常, columnName -> {}", columnName, e);
        }
        return false;
    }
}
