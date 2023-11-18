package com.yiling.erp.client.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.erp.client.common.MidTableTemplateSql;
import com.yiling.erp.client.dao.PushConfigDao;
import com.yiling.erp.client.dao.SysConfigDao;
import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.DateUtil;
import com.yiling.erp.client.util.ErpClientMidTable;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.StringUtil;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.SysConfig;

@Controller
@RequestMapping({"/erp"})
public class MidTableController {

    @Autowired
    private PushConfigDao pushConfigDao;

    @Autowired
    private SysConfigDao sysConfigDao;

    @RequestMapping(value = {"/createOrderTable.htm"}, produces = {"text/html;charset=UTF-8"})
    public String createOrderTable(HttpServletRequest request, HttpServletResponse response) {
        return "midTableConfig";
    }

    @ResponseBody
    @RequestMapping(value = {"/getSql.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String getOrderIssuedInfo(HttpServletRequest request, HttpServletResponse response) {
        PushConfig pushConfig = null;
        try {
            List<PushConfig> pushConfigList = pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from push_config");
            if (!CollectionUtils.isEmpty(pushConfigList)) {
                pushConfig = pushConfigList.get(0);
            } else {
                List<SysConfig> sysConfigList = sysConfigDao.executeQuerySysConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from sys_config");
                if (!CollectionUtils.isEmpty(sysConfigList)) {
                    ErpClientMidTable.getInstance();
                    MidTableTemplateSql mtts = ErpClientMidTable.em;
                    SysConfig sysConfig = sysConfigList.get(0);
                    pushConfig = new PushConfig();
                    pushConfig.setStatus("0");
                    pushConfig.setCreateTime(DateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    pushConfig.setDbCharset(sysConfig.getDbCharset());
                    pushConfig.setDbIp(sysConfig.getDbIp());
                    pushConfig.setDbLoginName(sysConfig.getDbLoginName());
                    pushConfig.setDbLoginPW(sysConfig.getDbLoginPW());
                    pushConfig.setDbName(sysConfig.getDbName());
                    pushConfig.setDbPort(sysConfig.getDbPort());
                    pushConfig.setDbType(sysConfig.getDbType());
                    pushConfig.setOracleSid(sysConfig.getOracleSid());
                    Map<String, String> map = mtts.getByType(sysConfig.getDbType());
                    pushConfig.setOrderSql(map.get("orderSql"));
                    pushConfig.setOrderDetailSql(map.get("orderDetailSql"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(pushConfig, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
    }


    @ResponseBody
    @RequestMapping(value = {"/getSqlByType.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String getSqlByType(HttpServletRequest request, HttpServletResponse response) {
        PushConfig pushConfig = null;
        try {
            String dbType = request.getParameter("dbType");
            MidTableTemplateSql mtts = ErpClientMidTable.em;
            pushConfig = new PushConfig();
            Map<String, String> map = mtts.getByType(dbType);
            pushConfig.setOrderSql(map.get("orderSql"));
            pushConfig.setOrderDetailSql(map.get("orderDetailSql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(pushConfig, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
    }

    @ResponseBody
    @RequestMapping(value = {"/saveBaseTable.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String saveBaseTable(HttpServletRequest request, HttpServletResponse response) {
        Map saveandcreateBack = new HashMap();
        String flag = "1";
        List<PushConfig> pushConfigList = null;
        try {
            pushConfigList = pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from push_config");
            PushConfig pushConfig = null;
            if (!CollectionUtils.isEmpty(pushConfigList)) {
                pushConfig = pushConfigList.get(0);
            }else{
                saveandcreateBack.put(DataBaseConnection.RESULT,"请先配置推送数据配置信息");
                saveandcreateBack.put(DataBaseConnection.FLAG, flag);
                return JSONObject.toJSONString(saveandcreateBack, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
            }
            DataBaseConnection baseConnection = DataBaseConnection.getInstance();
            SysConfig sysConfig = InitErpConfig.toSysConfig(pushConfig);
            Map resultOrder = baseConnection.createOrderMidTable(sysConfig, StringUtil.getResult(new StringBuilder().append(pushConfig.getOrderSql()).append("    ").append(pushConfig.getOrderDetailSql()).toString()));
            if (((Boolean) resultOrder.get(DataBaseConnection.FLAG)).booleanValue()) {
                pushConfig.setStatus("1");
//                loger.info("*******************客户端 中间表  自动 建表执行 success");
            } else {
                flag = "0";
//                loger.info("*******************客户端 中间表 自动 建表执行 error");
                saveandcreateBack.put(DataBaseConnection.RESULT,resultOrder.get(DataBaseConnection.RESULT));
            }
            DataBaseConnection.getInstance().initTableInfo(sysConfig, InitErpConfig.tableColumnMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveandcreateBack.put(DataBaseConnection.FLAG, flag);
        return JSONObject.toJSONString(saveandcreateBack, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
    }


    @ResponseBody
    @RequestMapping(value = {"/saveandcreate.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String saveandcreate(HttpServletRequest request, HttpServletResponse response) {
        Map saveandcreateBack = new HashMap();
        String flag = "1";
        StringBuilder errorMessage = new StringBuilder("");
        PushConfig pushConfig = new PushConfig();
        String dbName = request.getParameter("dbName");
        String dbType = request.getParameter("dbType");
        String oracleType = request.getParameter("oracleType");
        String oracleSid = request.getParameter("oracleSid");
        String dbCharacter = request.getParameter("dbCharacter");
        String dbLoginName = request.getParameter("dbLoginName");
        String dbLoginPW = request.getParameter("dbLoginPW");
        String dbIP = request.getParameter("dbIp");
        String dbPort = request.getParameter("dbPort");
        pushConfig.setDbName(dbName);
        pushConfig.setDbLoginPW(dbLoginPW);
        pushConfig.setDbLoginName(dbLoginName);
        pushConfig.setOracleType(oracleType);
        pushConfig.setOracleSid(oracleSid);
        pushConfig.setDbCharset(dbCharacter);
        pushConfig.setDbIp(dbIP);
        pushConfig.setDbPort(dbPort);
        pushConfig.setDbType(dbType);
        pushConfig.setOrderSql(request.getParameter("orderSql"));
        pushConfig.setOrderDetailSql(request.getParameter("orderDetailSql"));
        pushConfig.setCreateTime(DateUtil.convertDate2String(new Date()));
        pushConfig.setUpdateTime(DateUtil.convertDate2String(new Date()));
        try {
            // 获取中间库表字段信息
            SysConfig sysConfig = PojoUtils.map(pushConfig, SysConfig.class);
            DataBaseConnection.getInstance().initTableInfo(sysConfig, InitErpConfig.tableColumnMap);
            pushConfigDao.executeSavePushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, pushConfig);
        } catch (Exception e) {
            flag = "0";
            errorMessage.append(e.getMessage());
        }
        saveandcreateBack.put(DataBaseConnection.FLAG, flag);
        saveandcreateBack.put(DataBaseConnection.RESULT, errorMessage.toString().length() < 200 ? errorMessage.toString() : errorMessage.toString().substring(0, 200));
        return JSONObject.toJSONString(saveandcreateBack, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
    }
}