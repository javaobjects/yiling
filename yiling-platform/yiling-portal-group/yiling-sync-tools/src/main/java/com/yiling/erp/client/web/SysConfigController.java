package com.yiling.erp.client.web;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.erp.client.dao.SysConfigDao;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.quartz.JobTaskService;
import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.DateUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.PopRequest;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.HeartParamDTO;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping({ "/system" })
public class SysConfigController {


    @Autowired
    public SysConfigDao sysConfigDao;

    @Autowired
    public TaskConfigDao taskConfigDao;

    @Autowired
    public InitErpConfig erpCommon;

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    private InitErpConfig initErpConfig;

    public static final String saveDB_ignore_ssl = "saveDB_ignore_ssl";

    @ResponseBody
    @RequestMapping(value = { "/getSystemConfig.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public String getSystemConfig(HttpServletRequest request, HttpServletResponse response) {
        SysConfig sysConfig = null;
        try {
            List<SysConfig> sysConfigList = sysConfigDao.executeQuerySysConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from sys_config");
            if (!CollectionUtils.isEmpty(sysConfigList)) {
                sysConfig = sysConfigList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(sysConfig, SerializerFeature.WriteNullStringAsEmpty);
    }

    @ResponseBody
    @RequestMapping(value = { "/testDB.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public String testDB(HttpServletRequest request, HttpServletResponse response) {
        SysConfig sysConfig = new SysConfig();
        String dbName = request.getParameter("dbName");
        String dbType = request.getParameter("dbType");
        String dbOracleType = request.getParameter("dbOracleType");
        String dbOracleSid = request.getParameter("dbOracleSid");
        String dbCharacter = request.getParameter("dbCharacter");
        String dbLoginName = request.getParameter("dbLoginName");
        String dbLoginPW = request.getParameter("dbLoginPW");
        String dbIP = request.getParameter("dbIP");
        String dbPort = request.getParameter("dbPort");
        String dbPath = request.getParameter("dbPath");

        sysConfig.setDbName(dbName);
        sysConfig.setDbLoginName(dbLoginName);
        sysConfig.setDbLoginPW(dbLoginPW);
        sysConfig.setDbPort(dbPort);
        sysConfig.setDbIp(dbIP);
        sysConfig.setDbCharset(dbCharacter);
        sysConfig.setUrlPath(dbPath);
        sysConfig.setDbType(dbType);
        sysConfig.setOracleType(dbOracleType);
        sysConfig.setOracleSid(dbOracleSid);

        DataBaseConnection baseConnection = DataBaseConnection.getInstance();
        Connection connect = null;
        try {
            connect = baseConnection.openCon(sysConfig);
            if (connect == null) {
                log.error("SysConfigController testDB, connect is null");
                return "db_false";
            }
        } catch (Exception e) {
            log.error("SysConfigController testDB, exception -> {}", e);
            return "db_false";
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = { "/saveDB.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public String saveDB(HttpServletRequest request, HttpServletResponse response) {
        SysConfig sysConfig = new SysConfig();
        String dbName = request.getParameter("dbName");
        String dbType = request.getParameter("dbType");
        String dbOracleType = request.getParameter("dbOracleType");
        String dbOracleSid = request.getParameter("dbOracleSid");
        String dbCharacter = request.getParameter("dbCharacter");
        String dbLoginName = request.getParameter("dbLoginName");
        String dbLoginPW = request.getParameter("dbLoginPW");
        String dbIP = request.getParameter("dbIP");
        String dbPort = request.getParameter("dbPort");
        String dbPath = request.getParameter("dbPath");
        String dbSecret = request.getParameter("dbSecret");
        String dbKey = request.getParameter("dbKey");

        log.info("version:" + InitErpConfig.VERSION + "dbName: " + dbName + "--dbType:" + dbType + "--dbOracleType:" + dbOracleType + "--dbCharacter:" + dbCharacter + "--dbLoginName:" + dbLoginName + "--dbLoginPW:" + dbLoginPW + "--dbIP:" + dbIP + "--dbPort:" + dbPort + "--dbPath:" + dbPath + "--dbSecret:" + dbSecret + "--dbKey:" + dbKey);

        sysConfig.setDbName(dbName);
        sysConfig.setDbLoginName(dbLoginName);
        sysConfig.setDbLoginPW(dbLoginPW);
        sysConfig.setDbPort(dbPort);
        sysConfig.setDbIp(dbIP);
        sysConfig.setDbCharset(dbCharacter);
        sysConfig.setUrlPath(dbPath);
        sysConfig.setDbType(dbType);
        sysConfig.setOracleType(dbOracleType);
        sysConfig.setOracleSid(dbOracleSid);
        sysConfig.setKey(dbKey);
        sysConfig.setSecret(dbSecret);
        sysConfig.setVersion(InitErpConfig.VERSION);
        sysConfig.setCreateTime(DateUtil.convertDate2String(new Date()));
        //验证数据库连接正常
        DataBaseConnection baseConnection = DataBaseConnection.getInstance();
        Connection connect = null;
        try {
            connect = baseConnection.openCon(sysConfig);
            if (connect == null) {
                log.error("SysConfigController saveDB, connect is null");
                return "db_false";
            }
        } catch (Exception e) {
            log.error("SysConfigController saveDB, connect error, exception -> {}", e);
            return "db_false";
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                log.error("关闭连接失败",e);
            }
        }

        //然后判断key是否合法
        PopRequest pr = new PopRequest();
        Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.ErpHeart.getMethod(), dbKey);
        String json = pr.addSysHeartLog("", InitErpConfig.PATH, "", "", "", "");
        // 设置SSL验证忽略的key
//        CacheTaskUtil.getInstance().addCacheData(saveDB_ignore_ssl);
        String returnValue = pr.getPost(dbPath, json, headMap, dbSecret);
        if ((returnValue != null) && (!returnValue.equals(""))) {
            Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
            Integer code = apiResponse.getCode();
            if (200 != code) {
                log.error("SysConfigController saveDB, apiResponse -> {}", returnValue);
                return "ddx_false";
            }
            // name|suId
            String result = String.valueOf(apiResponse.getData());
            String[] resultArray = result.split(",");
            sysConfig.setName(resultArray[0]);
            sysConfig.setSuId(Long.parseLong(resultArray[1]));
            sysConfig.setEnvName(resultArray[2]);
            HeartParamDTO heartParamDTO = JSON.parseObject(Base64.decodeStr(resultArray[3]), HeartParamDTO.class);
            if (heartParamDTO != null) {
                initErpConfig.setHeartParamDTO(heartParamDTO);
            }else{
                log.error("HeartParamDTO 对象数据为null");
            }
        }

        log.info("保存配置成功{}",returnValue);

        //保存本地
        try {
            if (!sysConfig.getKey().equals(dbKey)) {//key发生变化，清空缓存
                List<TaskConfig> taskConfigList = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config");
                if (!CollectionUtils.isEmpty(taskConfigList)) {
                    for (TaskConfig taskConfig : taskConfigList) {
                        //先停止服务判断是否任务是否执行完毕
                        jobTaskService.pauseJob(taskConfig);
                        //                        taskConfigDao.deleteSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, ErpTopicName.getFromCode(taskConfig.getTaskNo()).getTopicName());
                    }
                    List<TaskConfig> list = null;
                    do {
                        list = jobTaskService.getRunningJob();
                        Thread.sleep(1000);
                    } while (!CollectionUtils.isEmpty(list));
                    for (TaskConfig taskConfig : taskConfigList) {
                        jobTaskService.addJob(taskConfig);
                    }
                }
            }
            sysConfigDao.saveSysConfig(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, sysConfig);
        } catch (Exception e) {
            log.error("SysConfigController saveDB, saveSysConfig error, exception -> {}", e);
            return "db_false";
        }
        //赋值到内存
        erpCommon.setSysConfig(sysConfig);
        return "success";
    }
}

