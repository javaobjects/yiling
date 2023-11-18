package com.yiling.erp.client.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.erp.client.dao.SQLiteHelper;
import com.yiling.erp.client.dao.SysConfigDao;

import cn.hutool.core.date.DateUtil;

/**
 * 更新服务类
 * @author: shuang.zhang
 * @date: 2021/12/17
 */
@Component
public class UpdateSqliteTable {

    @Autowired
    private SQLiteHelper sqliteHelper;
    @Autowired
    private SysConfigDao sysConfigDao;

    public void updateSqliteTableByVersion201(String dbPath, String dbName) throws Exception {
        Date date = new Date();
        String currentTime = DateUtil.format(date, "yyyyMMddHHmmss");

        String sql="ALTER TABLE \"main\".\"sys_config\" RENAME TO \"_sys_config_old_"+currentTime+"\";\n" +
                "\n" +
                "CREATE TABLE \"main\".\"sys_config\" (\n" +
                "  \"suId\" text(32) DEFAULT '',\n" +
                "  \"name\" TEXT(32) NOT NULL,\n" +
                "  \"key\" TEXT(32) NOT NULL,\n" +
                "  \"secret\" TEXT(32) NOT NULL,\n" +
                "  \"urlPath\" TEXT(64) NOT NULL,\n" +
                "  \"address\" TEXT(64) NOT NULL,\n" +
                "  \"dbName\" TEXT(32) NOT NULL,\n" +
                "  \"dbType\" TEXT(32) NOT NULL,\n" +
                "  \"oracleType\" TEXT(32),\n" +
                "  \"dbCharset\" TEXT(32) NOT NULL,\n" +
                "  \"oracleSid\" text(64),\n" +
                "  \"dbLoginName\" TEXT(64),\n" +
                "  \"dbLoginPW\" TEXT(64),\n" +
                "  \"dbIp\" TEXT(32),\n" +
                "  \"dbPort\" TEXT(5),\n" +
                "  \"tabPane\" TEXT(64),\n" +
                "  \"version\" TEXT(32),\n" +
                "  \"envName\" TEXT(32) DEFAULT prd,\n" +
                "  \"syncStatus\" TEXT(32),\n" +
                "  \"updateTime\" REAL,\n" +
                "  \"createTime\" REAL NOT NULL\n" +
                ");\n" +
                "\n" +
                "INSERT INTO \"main\".\"sys_config\" (\"suId\", \"name\", \"key\", \"secret\", \"urlPath\", \"address\", \"dbName\", \"dbType\", \"oracleType\", \"dbCharset\", \"oracleSid\", \"dbLoginName\", \"dbLoginPW\", \"dbIp\", \"dbPort\", \"tabPane\", \"version\", \"syncStatus\", \"updateTime\", \"createTime\") SELECT \"suId\", \"name\", \"key\", \"secret\", \"urlPath\", \"address\", \"dbName\", \"dbType\", \"oracleType\", \"dbCharset\", \"oracleSid\", \"dbLoginName\", \"dbLoginPW\", \"dbIp\", \"dbPort\", \"tabPane\", \"version\", \"syncStatus\", \"updateTime\", \"createTime\" FROM \"main\".\"_sys_config_old_"+currentTime+"\";";

        sqliteHelper.executeUpdate(dbPath,dbName,sql);

        String sqltask="ALTER TABLE \"main\".\"task_config\" RENAME TO \"_task_config_old_"+currentTime+"\";\n" +
                "\n" +
                "CREATE TABLE \"main\".\"task_config\" (\n" +
                "  \"taskNo\" TEXT(32) NOT NULL,\n" +
                "  \"taskName\" TEXT(32) NOT NULL,\n" +
                "  \"taskInterval\" TEXT(32) NOT NULL,\n" +
                "  \"taskSQL\" TEXT(1024) NOT NULL,\n" +
                "  \"taskStatus\" TEXT(1) NOT NULL,\n" +
                "  \"taskKey\" TEXT(64) NOT NULL,\n" +
                "  \"springId\" TEXT(32) NOT NULL,\n" +
                "  \"methodName\" TEXT(32) NOT NULL,\n" +
                "  \"syncStatus\" TEXT(32) NOT NULL DEFAULT 0,\n" +
                "  \"flowDateCount\" TEXT(32),\n" +
                "  \"updateTime\" REAL,\n" +
                "  \"createTime\" REAL NOT NULL\n" +
                ");\n" +
                "\n" +
                "INSERT INTO \"main\".\"task_config\" (\"taskNo\", \"taskName\", \"taskInterval\", \"taskSQL\", \"taskStatus\", \"taskKey\", \"springId\", \"methodName\", \"syncStatus\", \"flowDateCount\", \"updateTime\", \"createTime\") SELECT \"taskNo\", \"taskName\", \"taskInterval\", \"taskSQL\", \"taskStatus\", \"taskKey\", \"springId\", \"methodName\", \"syncStatus\", \"flowDateCount\", \"updateTime\", \"createTime\" FROM \"main\".\"_task_config_old_"+currentTime+"\";";

        sqliteHelper.executeUpdate(dbPath,dbName,sqltask);

        String sqlpush="ALTER TABLE \"main\".\"push_config\" RENAME TO \"_push_config_old_"+currentTime+"\";\n" +
                "\n" +
                "CREATE TABLE \"main\".\"push_config\" (\n" +
                "  \"dbName\" TEXT(32) NOT NULL,\n" +
                "  \"status\" TEXT(1) NOT NULL,\n" +
                "  \"dbType\" TEXT(32) NOT NULL,\n" +
                "  \"oracleType\" TEXT(32),\n" +
                "  \"oracleSid\" text(64) DEFAULT '',\n" +
                "  \"dbCharset\" TEXT(32) NOT NULL,\n" +
                "  \"dbLoginName\" TEXT(64),\n" +
                "  \"dbLoginPW\" TEXT(64),\n" +
                "  \"dbIp\" TEXT(32),\n" +
                "  \"dbPort\" TEXT(5),\n" +
                "  \"orderSql\" TEXT(128),\n" +
                "  \"orderDetailSql\" TEXT(128),\n" +
                "  \"syncStatus\" TEXT(32),\n" +
                "  \"updateTime\" REAL,\n" +
                "  \"createTime\" REAL NOT NULL\n" +
                ");\n" +
                "\n" +
                "INSERT INTO \"main\".\"push_config\" (\"dbName\", \"status\", \"dbType\", \"oracleType\", \"oracleSid\", \"dbCharset\", \"dbLoginName\", \"dbLoginPW\", \"dbIp\", \"dbPort\", \"orderSql\", \"orderDetailSql\", \"syncStatus\", \"updateTime\", \"createTime\") SELECT \"dbName\", \"status\", \"dbType\", \"oracleType\", \"oracleSid\", \"dbCharset\", \"dbLoginName\", \"dbLoginPW\", \"dbIp\", \"dbPort\", \"orderSql\", \"orderDetailSql\", \"syncStatus\", \"updateTime\", \"createTime\" FROM \"main\".\"_push_config_old_"+currentTime+"\";";
        sqliteHelper.executeUpdate(dbPath,dbName,sqlpush);
    }

    public void updateSqliteTableByVersion204(String dbPath, String dbName) throws Exception {
        String sql = "UPDATE \"main\".\"sys_config\" SET" +
                "  \"oracleType\" = \"sidName\"," +
                "  \"oracleSid\" = \"oracleSid\"" +
                "  WHERE \"dbType\" != \"Oracle\"" +
                ";";
        System.out.println("sql sys_config:" + sql);
        sqliteHelper.executeUpdate(dbPath,dbName,sql);

        String sqlpush = "UPDATE \"main\".\"push_config\" SET" +
                "  \"oracleType\" = \"sidName\"," +
                "  \"oracleSid\" = \"oracleSid\"" +
                "  WHERE \"dbType\" != \"Oracle\"" +
                ";";
        System.out.println("sql push_config:" + sqlpush);
        sqliteHelper.executeUpdate(dbPath,dbName,sqlpush);
    }
}
