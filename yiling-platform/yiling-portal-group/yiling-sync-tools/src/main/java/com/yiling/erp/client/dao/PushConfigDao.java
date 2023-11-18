package com.yiling.erp.client.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.open.erp.dto.PushConfig;

/**
 * @author shuan
 */
@Service
public class PushConfigDao {
    @Autowired
    private SQLiteHelper sqliteHelper;

    /**
     * 执行select查询，返回结果列表
     *
     * @param sql sql select 语句
     * @return
     * @throws Exception
     */
    public List<PushConfig> executeQueryPushConfigList(String dbPath, String database, String sql) throws Exception {
        List<PushConfig> rsList = new ArrayList<>();
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                PushConfig pushConfig = new PushConfig();
                pushConfig.setStatus(resultSet.getString("status"));
                pushConfig.setDbName(resultSet.getString("dbName"));
                pushConfig.setDbType(resultSet.getString("dbType"));
                pushConfig.setOracleType(resultSet.getString("oracleType"));
                pushConfig.setOracleSid(resultSet.getString("oracleSid"));
                pushConfig.setDbCharset(resultSet.getString("dbCharset"));
                pushConfig.setDbLoginName(resultSet.getString("dbLoginName"));
                pushConfig.setDbLoginPW(resultSet.getString("dbLoginPW"));
                pushConfig.setDbIp(resultSet.getString("dbIp"));
                pushConfig.setDbPort(resultSet.getString("dbPort"));
                pushConfig.setOrderSql(resultSet.getString("orderSql"));
                pushConfig.setOrderDetailSql(resultSet.getString("orderDetailSql"));
                pushConfig.setUpdateTime(resultSet.getString("updateTime"));
                pushConfig.setCreateTime(resultSet.getString("createTime"));
                rsList.add(pushConfig);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return rsList;
    }


    /**
     * 执行select查询，返回结果列表
     *
     * @param dbPath
     * @param database
     * @param pushConfig
     * @return
     * @throws Exception
     */
    public boolean executeSavePushConfigList(String dbPath, String database, PushConfig pushConfig) throws Exception {
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            StringBuffer buf = new StringBuffer();
            buf.append("INSERT INTO \"push_config\"(\"dbName\",\"status\", \"dbType\", \"oracleType\", \"oracleSid\", \"dbCharset\", \"dbLoginName\", \"dbLoginPW\", \"dbIp\", \"dbPort\",\"orderSql\",\"orderDetailSql\",\"syncStatus\", \"updateTime\", \"createTime\") VALUES ('");
            buf.append(pushConfig.getDbName()).append("','");
            buf.append(pushConfig.getStatus()).append("','");
            buf.append(pushConfig.getDbType()).append("','");
            buf.append(pushConfig.getOracleType()).append("','");
            buf.append(pushConfig.getOracleSid()).append("','");
            buf.append(pushConfig.getDbCharset()).append("','");
            buf.append(pushConfig.getDbLoginName()).append("','");
            buf.append(pushConfig.getDbLoginPW()).append("','");
            buf.append(pushConfig.getDbIp()).append("','");
            buf.append(pushConfig.getDbPort()).append("','");
            buf.append(pushConfig.getOrderSql()).append("','");
            buf.append(pushConfig.getOrderDetailSql()).append("','");
            buf.append(0).append("','");
            buf.append(pushConfig.getUpdateTime()).append("','");
            buf.append(pushConfig.getCreateTime()).append("');");
            connection = sqliteHelper.getConnection(dbPath, database);
            connection.setAutoCommit(false); // 设置手动提交
            statement = connection.createStatement();
            String sb = "delete from push_config";
            statement.executeUpdate(sb);

            Integer i = statement.executeUpdate(buf.toString());
            connection.commit();  // 提交
            if (i > 0) {
                return true;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    public boolean updatePushConfigSyncStatus(String dbPath, String database, String syncStatus) throws Exception {
        Statement statement = null;
        Connection connection = null;

        StringBuffer buf = new StringBuffer();
        buf.append("update push_config set syncStatus='").append(syncStatus).append("'");
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            Integer i = statement.executeUpdate(buf.toString());
            if (i > 0) {
                return true;
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }
}
