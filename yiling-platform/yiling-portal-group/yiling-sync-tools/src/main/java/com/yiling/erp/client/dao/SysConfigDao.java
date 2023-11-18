package com.yiling.erp.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.DateUtil;
import com.yiling.open.erp.dto.SysConfig;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author shuan
 */
@Service
public class SysConfigDao {

    @Autowired
    private SQLiteHelper sqliteHelper;

    /**
     * 执行select查询，返回结果列表
     *
     * @param sql sql select 语句
     * @return
     * @throws Exception
     */
    public List<SysConfig> executeQuerySysConfigList(String dbPath, String database, String sql) throws Exception {
        List<SysConfig> rsList = new ArrayList<SysConfig>();
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement prsts = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            prsts = connection.prepareStatement(sql);
            // 判断列名是否存在
            boolean suIdEexistFlag = DataBaseConnection.getInstance().columnNameIsEexist("suId", prsts);

            while (resultSet.next()) {
                SysConfig sysConfig = new SysConfig();
                if (suIdEexistFlag) {
                    String suId = resultSet.getString("suId");
                    if (StringUtils.isNotBlank(StrUtil.nullToEmpty(suId)) && !ObjectUtil.equal("null", suId)) {
                        sysConfig.setSuId(Long.parseLong(suId));
                    }
                }
                sysConfig.setName(resultSet.getString("name"));
                sysConfig.setKey(resultSet.getString("key"));
                sysConfig.setSecret(resultSet.getString("secret"));
                sysConfig.setUrlPath(resultSet.getString("urlPath"));
                sysConfig.setAddress(resultSet.getString("address"));
                sysConfig.setDbName(resultSet.getString("dbName"));
                sysConfig.setDbType(resultSet.getString("dbType"));
                sysConfig.setOracleType(resultSet.getString("oracleType"));
                sysConfig.setOracleSid(resultSet.getString("oracleSid"));
                sysConfig.setDbCharset(resultSet.getString("dbCharset"));
                sysConfig.setDbLoginName(resultSet.getString("dbLoginName"));
                sysConfig.setDbLoginPW(resultSet.getString("dbLoginPW"));
                sysConfig.setDbIp(resultSet.getString("dbIp"));
                sysConfig.setDbPort(resultSet.getString("dbPort"));
                sysConfig.setVersion(resultSet.getString("version"));
                sysConfig.setEnvName(resultSet.getString("envName"));
                sysConfig.setTabPane(resultSet.getString("tabPane"));
                sysConfig.setUpdateTime(resultSet.getString("updateTime"));
                sysConfig.setCreateTime(resultSet.getString("createTime"));
                rsList.add(sysConfig);
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

    public boolean saveSysConfig(String dbPath, String database, SysConfig sysConfig) throws Exception {
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select count(1) from sys_config");
            int i = 0;
            if (resultSet.next()) {
                i = resultSet.getInt(1);
            }
            if (i <= 0) {
                StringBuffer buf = new StringBuffer();
                buf.append("INSERT INTO \"main\".\"sys_config\" (\"suId\", \"name\", \"key\", \"secret\", \"urlPath\", \"address\", \"dbName\", \"dbType\", \"oracleType\",\"oracleSid\", \"dbCharset\", \"dbLoginName\", \"dbLoginPW\", \"dbIp\", \"dbPort\",\"version\",\"envName\",\"syncStatus\", \"updateTime\", \"createTime\") VALUES ('");
                buf.append(sysConfig.getSuId()).append("','");
                buf.append(sysConfig.getName()).append("','");
                buf.append(sysConfig.getKey()).append("','");
                buf.append(sysConfig.getSecret()).append("','");
                buf.append(sysConfig.getUrlPath()).append("','");
                buf.append(sysConfig.getAddress()).append("','");
                buf.append(sysConfig.getDbName()).append("','");
                buf.append(sysConfig.getDbType()).append("','");
                buf.append(sysConfig.getOracleType()).append("','");
                buf.append(sysConfig.getOracleSid()).append("','");
                buf.append(sysConfig.getDbCharset()).append("','");
                buf.append(sysConfig.getDbLoginName()).append("','");
                buf.append(sysConfig.getDbLoginPW()).append("','");
                buf.append(sysConfig.getDbIp()).append("','");
                buf.append(sysConfig.getDbPort()).append("','");
                buf.append(sysConfig.getVersion()).append("','");
                buf.append(sysConfig.getEnvName()).append("','");
                buf.append(0).append("','");
                buf.append(sysConfig.getUpdateTime()).append("','");
                buf.append(sysConfig.getCreateTime()).append("');");
                statement.executeUpdate(buf.toString());
            } else {
                StringBuffer buf = new StringBuffer();
                buf.append("update sys_config set syncStatus='0',");
                buf.append("suId='").append(sysConfig.getSuId()).append("',");
                buf.append("name='").append(sysConfig.getName()).append("',");
                buf.append("key='").append(sysConfig.getKey()).append("',");
                buf.append("secret='").append(sysConfig.getSecret()).append("',");
                buf.append("urlPath='").append(sysConfig.getUrlPath()).append("',");
                buf.append("address='").append(sysConfig.getAddress()).append("',");
                buf.append("dbName='").append(sysConfig.getDbName()).append("',");
                buf.append("dbType='").append(sysConfig.getDbType()).append("',");
                buf.append("oracleType='").append(sysConfig.getOracleType()).append("',");
                buf.append("oracleSid='").append(sysConfig.getOracleSid()).append("',");
                buf.append("dbCharset='").append(sysConfig.getDbCharset()).append("',");
                buf.append("dbLoginName='").append(sysConfig.getDbLoginName()).append("',");
                buf.append("dbLoginPW='").append(sysConfig.getDbLoginPW()).append("',");
                buf.append("dbIp='").append(sysConfig.getDbIp()).append("',");
                buf.append("dbPort='").append(sysConfig.getDbPort()).append("',");
                buf.append("version='").append(sysConfig.getVersion()).append("',");
                buf.append("envName='").append(sysConfig.getEnvName()).append("',");
                buf.append("updateTime='").append(DateUtil.convertDate2String(new Date())).append("'");
                statement.executeUpdate(buf.toString());
            }
            return true;
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
    }

    public boolean updateSysConfigPane(String dbPath, String database, String tabPane) throws Exception {
        Statement statement = null;
        Connection connection = null;

        StringBuffer buf = new StringBuffer();
        buf.append("update sys_config set syncStatus='0', tabPane='").append(tabPane).append("'");
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

    public boolean updateSysConfigSyncStatus(String dbPath, String database, String syncStatus) throws Exception {
        Statement statement = null;
        Connection connection = null;

        StringBuffer buf = new StringBuffer();
        buf.append("update sys_config set syncStatus='").append(syncStatus).append("'");
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

    public boolean updateSysConfigVersion(String dbPath, String database, String version) throws Exception {
        Statement statement = null;
        Connection connection = null;

        StringBuffer buf = new StringBuffer();
        buf.append("update sys_config set version='").append(version).append("'");
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

    public String getSysConfigVersion(String dbPath, String database) throws Exception {
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;

        StringBuffer buf = new StringBuffer();
        buf.append("select version from sys_config ");
        try {
            connection = sqliteHelper.getConnection(dbPath, database);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(buf.toString());

            while (resultSet.next()) {
                return resultSet.getString("version");
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
        return "";
    }
}
