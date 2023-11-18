package com.yiling.erp.client.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.InitErpConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Controller
@Slf4j
@RequestMapping({"/erp"})
public class DataTableController {

    @Autowired
    public InitErpConfig erpCommon;

    @RequestMapping(value = {"/datatable.htm"}, produces = {"text/html;charset=UTF-8"})
    public String datatable(HttpServletRequest request, HttpServletResponse response) {
        return "dataTable";
    }

    @ResponseBody
    @RequestMapping(value = {"/datatableInfo.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String datatableInfo(HttpServletRequest request, HttpServletResponse response) {
        String table_sql = "";
        DataBaseConnection baseConnection = DataBaseConnection.getInstance();
        String DBType = erpCommon.getSysConfig().getDbType();
        String DBName = erpCommon.getSysConfig().getDbName();
        if (DBType.equals("Mysql"))
            table_sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + DBName + "'";
        else if (DBType.equals("Oracle"))
            table_sql = "SELECT TABLE_NAME AS TABLE_NAME,COMMENTS AS TABLE_COMMENT FROM USER_TAB_COMMENTS ORDER BY TABLE_NAME";
        else if (DBType.equals("SQL Server")) {
            table_sql = "SELECT convert(varchar(100), a.name) as TABLE_NAME, (SELECT case when b.colid=1 then convert(varchar(500), VALUE) else null end FROM sys.extended_properties d WHERE d.major_id = a.id AND d.minor_id = '0' ) as TABLE_COMMENT FROM sysobjects a INNER JOIN syscolumns b ON a.id = b.id WHERE a.xtype = 'U' ORDER BY a.name, b.id, b.colid";
        } else if (DBType.equals("DB2"))
            table_sql = "SELECT NAME AS TABLE_NAME,REMARKS AS TABLE_COMMENT FROM SYSIBM.SYSTABLES WHERE CREATOR = '" + DBName.toUpperCase() + "'";
        try {
            List table_list = baseConnection.executeQuery(erpCommon.getSysConfig(),table_sql);
            return JSONObject.toJSONString(table_list, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
        } catch (Exception e) {
            log.error("getDBTableInfo error :" + e);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = {"/columnsInfo/{table}.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String columnsInfo(@PathVariable("table") String table, HttpServletRequest request, HttpServletResponse response) {
        String columns_sql = "";
        DataBaseConnection baseConnection = DataBaseConnection.getInstance();
        String DBType = erpCommon.getSysConfig().getDbType();
        String DBName = erpCommon.getSysConfig().getDbName();
        if (DBType.equals("Mysql")) {
            columns_sql = "SELECT TABLE_NAME,COLUMN_NAME,COLUMN_TYPE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + DBName + "'" + " AND TABLE_NAME = '" + table + "' ;";
        } else if (DBType.equals("Oracle")) {
            columns_sql = "SELECT C.TABLE_NAME,C.COLUMN_NAME,(SELECT DATA_TYPE||'('||DATA_LENGTH||')' FROM USER_TAB_COLUMNS WHERE TABLE_NAME='" + table + "' AND COLUMN_NAME = C.COLUMN_NAME) AS COLUMN_TYPE, " + "C.COMMENTS AS COLUMN_COMMENT FROM USER_COL_COMMENTS C WHERE C.TABLE_NAME='" + table + "' ORDER BY C.COLUMN_NAME";
        } else if (DBType.equals("SQL Server")) {
            columns_sql = "SELECT convert(varchar(100), a.name) as TABLE_NAME, convert(varchar(100), b.name) as COLUMN_NAME, convert(varchar(100), c.VALUE) as COLUMN_COMMENT, convert(varchar(100), d.name) as COLUMN_TYPE FROM sysobjects a INNER JOIN syscolumns b ON a.id = b.id LEFT JOIN sys.extended_properties c ON b.id = c.major_id AND b.colid = c.minor_id LEFT JOIN systypes d on b.xusertype=d.xusertype WHERE a.xtype = 'U' and a.name = '" + table + "' ORDER BY b.id, b.colid";
        } else if (DBType.equals("DB2")) {
            columns_sql = "SELECT TABLE_NAME,COLUMN_NAME,CASE DATA_TYPE WHEN 'CHARACTER VARYING' THEN DATA_TYPE || '(' || CHARACTER_MAXIMUM_LENGTH || ')' WHEN 'DATE' THEN DATA_TYPE || '(' || DATETIME_PRECISION || ')' WHEN 'INTEGER' THEN DATA_TYPE || '(' || NUMERIC_PRECISION_RADIX || ')' WHEN 'TIMSTAMP' THEN  DATA_TYPE || '(' || DATETIME_PRECISION || ')' END AS COLUMN_TYPE, '' AS COLUMN_COMMENT FROM SYSIBM.COLUMNS WHERE TABLE_SCHEMA = '" + DBName.toUpperCase() + "' AND TABLE_NAME = '" + table + "'";
        }
        try {
            List columns_list = baseConnection.executeQuery(erpCommon.getSysConfig(),columns_sql);
            return JSONObject.toJSONString(columns_list, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
        } catch (Exception e) {
            log.error("getDBColumnsInfo error :" + e);
        }
        return null;
    }
}
