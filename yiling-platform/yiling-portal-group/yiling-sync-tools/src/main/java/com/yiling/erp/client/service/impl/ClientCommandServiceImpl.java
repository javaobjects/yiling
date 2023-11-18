package com.yiling.erp.client.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;

import com.yiling.erp.client.dao.PushConfigDao;
import com.yiling.open.erp.dto.PushConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.common.Constants;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.quartz.JobTaskService;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.erp.client.util.CommonConstants;
import com.yiling.erp.client.util.DataBaseConnection;
import com.yiling.erp.client.util.DateUtil;
import com.yiling.erp.client.util.ErpException;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.erp.client.util.KillServer;
import com.yiling.erp.client.util.ReadProperties;
import com.yiling.erp.client.util.StringUtil;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ClientRequestDTO;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.CommandStatusEnum;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.erp.client.util.PopRequest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Slf4j
@Service("clientCommandService")
public class ClientCommandServiceImpl extends InitErpSysData implements SyncTaskService {

    @Autowired
    private InitErpConfig  initErpConfig;
    @Autowired
    public  InitErpSysData initErpSysData;
    @Autowired
    private TaskConfigDao  taskConfigDao;
    @Autowired
    private PushConfigDao  pushConfigDao;
    @Autowired
    private JobTaskService jobTaskService;

    @Override
    public void syncData() {
        Long startTime=System.currentTimeMillis();
        try {
            if(initErpConfig.getSysConfig()==null){
                return;
            }
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.RedisCommand.getTopicName());
            if(StrUtil.isEmpty(initErpConfig.getSysConfig().getKey())){
                return;
            }
            PopRequest pr = new PopRequest();
            Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.RedisCommandGet.getMethod(), initErpConfig.getSysConfig().getKey());
            String returnValue = pr.getPost(initErpConfig.getSysConfig().getUrlPath(), "{}", headMap, initErpConfig.getSysConfig().getSecret());
            //插入到erp系统里
            if (StrUtil.isEmpty(returnValue)) {
                return;
            }

            Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
            Integer code = apiResponse.getCode();
            if (200 != code) {
                return;
            }
            ClientRequestDTO clientRequestDTO = JSON.parseObject((String) apiResponse.getData(), ClientRequestDTO.class);
            if(clientRequestDTO.getStatus()!=null&&clientRequestDTO.getStatus()==0){
                Thread.sleep(1000*60*5);
                return;
            }
            String message = "";
            switch (ErpTopicName.getFromCode(clientRequestDTO.getTaskNo())) {
                //执行sql语句
                case SqlSelect:
                    message = this.sqlSelect(clientRequestDTO.getMessage());
                    break;
                //保存任务
                case SqlSaveTask:
                    message = this.saveSqlTask(clientRequestDTO.getMessage());
                    break;
                //查询任务
                case SqlSelectTask:
                    message = this.sqlSelectTask(clientRequestDTO.getMessage());
                    break;
                //删除缓存
                case deleteCache:
                    message = this.deleteCache(clientRequestDTO.getMessage());
                    break;
                //执行查询
                case SqlExecute:
                    message = this.sqlExecute(clientRequestDTO.getMessage());
                    break;
                //升级
                case upgradeCommand:
                    message = this.upgradeCommand(clientRequestDTO.getMessage());
                    break;
                default:
                    break;
            }
            //数据消息
            if (StrUtil.isNotEmpty(message)) {
                Map<String, String> returnMap = new HashMap<>();
                returnMap.put(OpenConstants.status_flag, CommandStatusEnum.client_complete.getCode());
                returnMap.put(OpenConstants.task_flag, message);
                Map<String, String> returnHeadMap = pr.generateHeadMap(ErpTopicName.RedisCommandUpdate.getMethod(), initErpConfig.getSysConfig().getKey());
                pr.getPost(initErpConfig.getSysConfig().getUrlPath(), JSON.toJSONString(returnMap), returnHeadMap, initErpConfig.getSysConfig().getSecret());
            }
        } catch (Exception e) {

            log.error(MessageFormat.format(Constants.SYNC_EXCEPTION_NAME,System.currentTimeMillis()-startTime, "redis命令信息", e));
//            PopRequest popRequest = new PopRequest();
//            SysConfig sysConfig = initErpConfig.getSysConfig();
//            ClientToolLogDTO erpLog = new ClientToolLogDTO();
//            erpLog.setClientLog(e.getMessage() + "堆栈信息：" + ExceptionUtils.getFullStackTrace(e));
//            erpLog.setMothedNo(ErpTopicName.RedisCommand.getMethod());
//            erpLog.setLogType(Integer.valueOf(3));
//            popRequest.addErpLog(erpLog, sysConfig);
        } finally {
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.RedisCommand.getTopicName());
        }
    }

    //缓存清理
    public String deleteCache(String task) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jsonObject = JSON.parseObject(task);
        String taskNo = jsonObject.getString("taskNo");
        ErpTopicName erpTopicName = ErpTopicName.getFromCode(taskNo);
        try {
            //先判断任务是否还在执行
            TaskConfig taskConfig = null;
            List<TaskConfig> lists = null;
            try {
                lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + erpTopicName.getMethod());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (CollectionUtils.isEmpty(lists)) {
                return null;
            }
            taskConfig = lists.get(0);
            String taskStatus = taskConfig.getTaskStatus();
            if (taskStatus.equals("1")) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "请先关闭同步任务");
                return jSONObject.toJSONString();
            }

            //判断任务是否还在执行
            List<TaskConfig> taskConfigList = jobTaskService.getRunningJob();
            if (!CollectionUtils.isEmpty(taskConfigList)) {
                for (TaskConfig runTaskConfig : taskConfigList) {
                    if (runTaskConfig.getTaskNo().equals(erpTopicName.getMethod())) {
                        jSONObject.put("code", 100);
                        jSONObject.put("message", "程序还在运行中，请稍后再试");
                        return jSONObject.toJSONString();
                    }
                }
            }
            initErpSysData.eleteCache(initErpConfig.getSysConfig(), erpTopicName);
        } catch (Exception e) {
            jSONObject.put("code", 100);
            jSONObject.put("message", e.getMessage());
            e.printStackTrace();
        }
        jSONObject.put("code", 200);
        jSONObject.put("message", "删除缓存数据成功");
        return jSONObject.toJSONString();
    }

    //保存任务
    public String saveSqlTask(String task) {
        TaskConfig taskConfig = new TaskConfig();
        JSONObject jsonObject = JSON.parseObject(task);
        JSONObject returnJSONObject = new JSONObject();

        String openDock = jsonObject.getString("taskStatus");
        String frequencyValue = jsonObject.getString("taskInterval");
        String sqlContext = jsonObject.getString("taskSql");
        String taskNo = jsonObject.getString("taskNo");
        String springId = jsonObject.getString("springId");
        String syncData = jsonObject.getString("methodName");
        String flowDateCount = jsonObject.getString("flowDateCount");

        if (Integer.parseInt(frequencyValue) > 60 * 24) {
            returnJSONObject.put("code", 100);
            returnJSONObject.put("message", "同步频率不能大于1440分钟(24小时)");
            return returnJSONObject.toJSONString();
        }

        if (1 == CacheTaskUtil.getInstance().getCacheData(taskNo)) {
            returnJSONObject.put("code", 100);
            returnJSONObject.put("message", "任务正在执行请稍后在保存");
            return returnJSONObject.toJSONString();
        }
        ErpTopicName erpTopicName = ErpTopicName.getFromCode(taskNo);

        //保存信息到本地信息
        taskConfig.setCreateTime(DateUtil.convertDate2String(new Date(), DateUtil.DATE_SPLIT_FORMAT));
        taskConfig.setTaskInterval(frequencyValue);
        taskConfig.setTaskKey(erpTopicName.getErpKey());
        taskConfig.setTaskName(erpTopicName.getTopicNameDesc());
        taskConfig.setTaskNo(erpTopicName.getMethod());
        taskConfig.setTaskSQL(sqlContext);
        if (StringUtil.isEmpty(openDock)) {
            taskConfig.setTaskStatus(StringUtil.isEmpty(openDock) ? "0" : "1");
        } else {
            taskConfig.setTaskStatus(openDock);
        }
        taskConfig.setSpringId(springId);
        taskConfig.setMethodName(syncData);
        taskConfig.setFlowDateCount(flowDateCount);

        try {
            //校验sql
            if(erpTopicName.getMethod().startsWith("2")) {
                //先判断是否已经配置推送连接配置
                if(taskConfig.getTaskStatus().equals("1")) {
                    try {
                        List<PushConfig> pushConfigList=pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,"select * from push_config");
                        if(org.springframework.util.CollectionUtils.isEmpty(pushConfigList)){
                            returnJSONObject.put("code", 100);
                            returnJSONObject.put("message", "请先配置推送数据源");
                            return returnJSONObject.toJSONString();
                        }
                    } catch (Exception e) {
                        returnJSONObject.put("code", 100);
                        returnJSONObject.put("message", "推送数据源获取失败");
                        return returnJSONObject.toJSONString();
                    }
                }
            }else{
                List<BaseErpEntity> dataList = initErpSysData.findDataByMethodNoAndSql(erpTopicName.getMethod(), sqlContext, 0);
                Map<String, BaseErpEntity> map = initErpSysData.isRepeat(dataList);
                if (map == null) {
                    returnJSONObject.put("code", 100);
                    returnJSONObject.put("message", "ERP系统信息主键有重复数据,请检查" + erpTopicName.getErpKey() + "字段");
                    return returnJSONObject.toJSONString();
                }
            }

            //保存本地
            if (!taskConfigDao.saveTaskConfig(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, taskConfig)) {
                returnJSONObject.put("code", 100);
                returnJSONObject.put("message", "配置信息保存本地失败");
                return returnJSONObject.toJSONString();
            }

            if (taskConfig.getTaskStatus().equals("1")) {
                String interval = taskConfig.getTaskInterval();
                taskConfig.setTaskInterval(interval);
                jobTaskService.addJob(taskConfig);
            } else {
                jobTaskService.deleteJob(taskConfig);
            }
            returnJSONObject.put("code", 200);
        } catch (Exception e) {
            returnJSONObject.put("code", 100);
            returnJSONObject.put("message", e.getMessage());
            e.printStackTrace();
        }
        return returnJSONObject.toJSONString();
    }

    //保存任务
    public String sqlSelectTask(String task) {
        TaskConfig taskConfig = null;
        List<TaskConfig> lists = null;
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObject = JSON.parseObject(task);
            String taskNo = jsonObject.getString("taskNo");
            lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + taskNo);
            if (CollectionUtils.isEmpty(lists)) {
                json.put("code", 100);//成功
                json.put("message", "还没有配置任务信息");
                return JSONObject.toJSONString(json);
            }
            taskConfig = lists.get(0);
            if (taskConfig != null) {
                json.put("code", 200);//成功
                json.put("message", JSON.toJSONString(taskConfig));
            } else {
                json.put("code", 100);//成功
                json.put("message", "还没有配置任务信息");
            }
        } catch (Exception e) {
            json.put("code", 100);//失败
            json.put("message", e.getMessage());
            e.printStackTrace();
        }
        return JSONObject.toJSONString(json);
    }

    //查询sql语句
    public String sqlSelect(String task) {
        JSONObject jsonObject = JSON.parseObject(task);
        JSONObject json = new JSONObject();
        try {
            String sqlContext = jsonObject.getString("sqlContext");
            String sqlCount = jsonObject.getString("sqlCount");
            SysConfig sysConfig = initErpConfig.getSysConfig();
            String sql = getSearchSql(sqlContext, sysConfig.getDbType(), Integer.parseInt(sqlCount));
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            for(Map<Object, Object> map:result){
                for(Map.Entry<Object,Object> entry:map.entrySet()) {
                    map.put(entry.getKey(),CommonConstants.getDateForQuery(entry.getValue(),"yyyy-MM-dd"));
                }
            }
            json.put("code", 200);//成功
            json.put("rows", result);
            json.put("total", result.size());
        } catch (Exception e) {
            json.put("code", 100);//失败
            json.put("rows", e.getMessage());
            e.printStackTrace();
        }
        return json.toJSONString();
    }

    //查询sql语句
    public String upgradeCommand(String task) {
        JSONObject jsonObject = JSON.parseObject(task);
        JSONObject json = new JSONObject();
        try {
            String version = jsonObject.getString("version");
            log.info("clientWatchService, 版本更新任务开始，当前版本为 -> {}", InitErpConfig.VERSION);
            this.addRedisLog("clientWatchService, 版本更新任务开始，当前版本为 -> " + InitErpConfig.VERSION);
            if ((version != null) && (!version.equals(""))) {
                log.info("clientWatchService, 远程获取版本信息为 -> {}", version);
                this.addRedisLog("clientWatchService, 远程获取版本信息为 -> " + version);
                if (!version.equals(InitErpConfig.VERSION)) {
                    String downUrl = jsonObject.get("packageUrl").toString();
                    //先判断文件是否存在，如果存在就删除，不存在就创建
                    File file = new File(InitErpConfig.PATH + "/download/");
                    //创建文件夹
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    //删除原文件信息
                    File toolsFile = new File(file.getPath() + File.separator + "yiling-client-web.war");
                    if (toolsFile.exists()) {
                        toolsFile.delete();
                    }
                    //创建文件信息
                    File createFile = new File(file.getPath() + File.separator + "yiling-client-web.war");
                    if (!createFile.exists()) {
                        createFile.createNewFile();
                        //设置可读权限
                        createFile.setReadable(true,false);
                        //设置可写权限
                        createFile.setWritable(true,false);
                    }

                    InputStream inputStream = PopRequest.downloadFile(downUrl);
                    OutputStream out = new FileOutputStream(createFile);
                    IOUtils.copy(inputStream, out);
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(out);
                    execute(initErpConfig.getSysConfig(), 2, jsonObject.getString("guardUrl"));
                }else{
                    log.info("守护进程退出-任务执行完成");
                    this.addRedisLog("守护进程退出-任务执行完成");
                }
            }
            json.put("code", 200);//成功
        } catch (Exception e) {
            json.put("code", 1);//失败
            e.printStackTrace();
        }
        return json.toJSONString();
    }

    //查询sql语句
    public String sqlExecute(String task) {
        JSONObject jsonObject = JSON.parseObject(task);
        JSONObject json = new JSONObject();
        try {
            String sqlContext = jsonObject.getString("taskSql");
            String type = jsonObject.getString("taskNo");
            String flowDateCount = jsonObject.getString("flowDateCount");
            Integer count = 0;
            if (StrUtil.isNotEmpty(flowDateCount)) {
                count = Integer.parseInt(flowDateCount);
            }
            List<BaseErpEntity> list = initErpSysData.findDataByMethodNoAndSql(type, sqlContext, count);
            json.put("code", 200);//成功
            json.put("rows", list);
            json.put("total", list.size());
        } catch (Exception e) {
            json.put("code", 100);//失败
            json.put("rows", e.getMessage());
            e.printStackTrace();
        }
        return json.toJSONString();
    }

    public static String getSearchSql(String sql, String type, int limit) throws ErpException {
        String dataSql = "";

        if ((type == null) || (type.trim().equals(""))) {
            throw new ErpException("数据库配置有误，请填写正确数据库配置");
        }
        if (type.trim().equals("Mysql")) {
            dataSql = "SELECT * FROM (" + sql + ") as lol limit " + limit;
        } else if (type.trim().equals("SQL Server") || type.trim().equals("SQL Server2000")) {
            dataSql = "SELECT TOP " + limit + " * FROM (" + sql + ") lol";
        } else if (type.trim().equals("Oracle")) {
            dataSql = "SELECT * FROM(" + sql + ") lol where rownum < " + limit;
        } else if (type.trim().equals("ODBC")) {
            dataSql = "SELECT TOP " + limit + " * FROM (" + sql + ") lol";
        }  else if (type.trim().equals("ODBC-DBF")) {
            dataSql = sql;
        } else {
            throw new ErpException("数据库类型有误，请选择正确的数据类型");
        }
        return dataSql;
    }

    public void execute(SysConfig sysConfig, Integer type, String guardUrl) throws IOException, MalformedObjectNameException {
        File file = new File(InitErpConfig.PATH + "/download/");
        //创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        File uploadFile = new File(file.getPath() + File.separator + "erp-guard-tools.jar");
        if (uploadFile.exists()) {
            uploadFile.delete();
        }
        File createUploadFile = new File(file.getPath() + File.separator + "erp-guard-tools.jar");
        if (!createUploadFile.exists()) {
            createUploadFile.createNewFile();
            //设置可读权限
            createUploadFile.setReadable(true,false);
            //设置可写权限
            createUploadFile.setWritable(true,false);
        }
        createUploadFile = HttpUtil.downloadFileFromUrl(guardUrl, createUploadFile);
        String basePath = InitErpConfig.PATH;
        if (basePath.startsWith("/")) {
            basePath = basePath.substring(1);
        }

        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String port = ((ObjectName) objectNames.iterator().next()).getKeyProperty("port");
        String jarPort = ReadProperties.getProperties("jarPort");
        KillServer killServer = new KillServer(Integer.parseInt(jarPort));
        killServer.start(Integer.parseInt(jarPort));
        String cmd = new StringBuilder().append("cmd /c start /b ").append(basePath).append("\\jdk8\\bin\\java -jar ").append(createUploadFile.getPath()).append(" ").append(port).append(" ").append(basePath).append(" ").append(sysConfig.getKey()).append(" ").append(type).append(" ").append(jarPort).append(" ").append(sysConfig.getSecret()).append(" ").append(sysConfig.getUrlPath()).toString();
        log.info(new StringBuilder().append("clientWatchService, cmd命令：").append(cmd).toString());
        this.addRedisLog(new StringBuilder().append("clientWatchService, cmd命令：").append(cmd).toString());
        Runtime.getRuntime().exec(cmd);
        log.info(new StringBuilder().append("clientWatchService, cmd命令完成：").append(cmd).toString());
        this.addRedisLog(new StringBuilder().append("clientWatchService, cmd命令完成：").append(cmd).toString());
    }

    public void addRedisLog(String log) {
        PopRequest pr = new PopRequest();
        Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.upgradeLog.getMethod(), initErpConfig.getSysConfig().getKey());
        pr.getPost(initErpConfig.getSysConfig().getUrlPath(), log, headMap, initErpConfig.getSysConfig().getSecret());
    }


}
