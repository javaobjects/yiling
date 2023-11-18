package com.yiling.erp.client.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.util.CommonConstants;
import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.ErpException;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.open.erp.dto.SysConfig;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping({"/erp"})
public class ClientSelectSqlController {

    @Autowired
    public InitErpConfig erpCommon;

    @Autowired
    public InitErpSysData initErpSysData;

    @RequestMapping(value = {"/selectSql.htm"}, produces = {"text/html;charset=UTF-8"})
    public String selectSql(HttpServletRequest request, HttpServletResponse response) {
        return "selectSql";
    }

    @ResponseBody
    @RequestMapping(value = {"/getSqlData.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public JSONObject getSqlData(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String sqlContext = request.getParameter("sqlContext");
            String sqlCount = request.getParameter("sqlCount");
            SysConfig sysConfig = erpCommon.getSysConfig();
           String sql= getSearchSql(sqlContext,sysConfig.getDbType(),Integer.parseInt(sqlCount));
            List<Map<Object,Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig,sql);
            for(Map<Object, Object> map:result){
                for(Map.Entry<Object,Object> entry:map.entrySet()) {
                    map.put(entry.getKey(), CommonConstants.getDateForQuery(entry.getValue(),"yyyy-MM-dd"));
                }
            }
            json.put("code", 200);//成功
            json.put("rows", result);
            json.put("total", result.size());
            result=null;
        } catch (Exception e) {
            json.put("code", 1);//失败
            json.put("rows", e.getMessage());
            e.printStackTrace();
        }
        return json;
    }


    public static String getSearchSql(String sql, String type, int limit) throws ErpException {
        String dataSql = "";

        if ((type == null) || (type.trim().equals(""))) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }
        if (type.trim().equals("Mysql")) {
            dataSql = "SELECT * FROM (" + sql + ") as lol limit " + limit;
        } else if (type.trim().equals("SQL Server")||type.trim().equals("SQL Server2000")) {
            dataSql = "SELECT TOP " + limit + " * FROM (" + sql + ") lol";
        } else if (type.trim().equals("Oracle")) {
            dataSql = "SELECT * FROM(" + sql + ") lol where rownum < "+limit;
        } else if (type.trim().equals("ODBC")) {
            dataSql = "SELECT TOP " + limit + " * FROM (" + sql + ") lol";
        } else if (type.trim().equals("ODBC-DBF")) {
            dataSql = sql;
        } else {
            throw new ErpException("数据库类型有误，请选择正确的数据类型");
        }
        return dataSql;
    }

}
