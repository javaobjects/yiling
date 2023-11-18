package com.yiling.erp.client.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.dao.PushConfigDao;
import com.yiling.erp.client.dao.SQLiteHelper;
import com.yiling.erp.client.dao.SysConfigDao;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.quartz.JobTaskService;
import com.yiling.erp.client.service.impl.ClientHeartServiceImpl;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.dto.HeartParamDTO;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 项目启动初始化类
 *
 * @author shuan
 */
@Slf4j
@Service
public class InitErpConfig {

    private       SysConfig                        sysConfig;
    private       Map<String, TaskConfig>          taskConfigMap             = new HashMap<>();
    public static Map<String, Map<String, String>> tableColumnMap            = new HashMap<>();
    public static String                           DB_PATH                   = "";
    public static String                           PATH                      = "";
    public static String                           LOG_PATH                  = "";
    public static String                           VERSION                   = "";
    public static String                           DB_NAME                   = "rk-config.db";
    public static String                           DATA_NAME                 = "rk-data.db";
    public static String                           yiling_flag               = "/rkConfig";
    public static String                           windows_flag              = "/webapps";
    public static String                           default_flag              = "/default";
    public static String                           ERP_TABLE_NAME            = "yiling";
    public static String                           ERP_PURCHASE_FLOW_PATH    = "";
    public static String                           ERP_SALE_FLOW_PATH        = "";
    public static String                           ERP_GOODS_BATCH_FLOW_PATH = "";
    public static String                           ERP_SHOP_SALE_FLOW_PATH   = "";

    public HeartParamDTO heartParamDTO = null;

    public HeartParamDTO getHeartParamDTO() {
        return heartParamDTO;
    }

    public void setHeartParamDTO(HeartParamDTO heartParamDTO) {
        this.heartParamDTO = heartParamDTO;
    }

    @Autowired
    private SQLiteHelper           sqliteHelper;
    @Autowired
    private SysConfigDao           sysConfigDao;
    @Autowired
    private TaskConfigDao          taskConfigDao;
    @Autowired
    private PushConfigDao          pushConfigDao;
    @Autowired
    private UpdateSqliteTable      updateSqliteTable;
    @Autowired
    private JobTaskService         jobTaskService;
    @Autowired
    private ClientHeartServiceImpl clientHeartService;

    @PostConstruct
    public void init() {
        boolean bool;
        do {
            bool = initConfig();
            if (!bool) {
                try {
                    Thread.sleep(1000 * 60 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (!bool);
    }

    private boolean initConfig() {
        try {
            //获取项目路径
            String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String tomcatPath = System.getProperty("catalina.home");
            String classesPath = "";
            if (path.indexOf(default_flag) > 0) {
                //运行环境为linux
                String[] classesPathArray = path.split("/default");
                List<String> classesPathList = Arrays.asList(classesPathArray);
                classesPath = classesPathList.get(0) + yiling_flag;
            } else {
                //运行环境为windows
                if (path.indexOf(windows_flag) > 0) {
                    String[] classesPathArray = path.split("/webapps");
                    List<String> classesPathList = Arrays.asList(classesPathArray);
                    classesPath = classesPathList.get(0);
                } else {
                    String[] classesPathArray = path.split("/");
                    List<String> classesPathList = Arrays.asList(classesPathArray);
                    classesPath = classesPathList.get(1) + yiling_flag;
                }
            }
            System.out.println(classesPath);
            if (StringUtil.isEmpty(classesPath)) {
                log.error("系统加载路径为空");
                return true;
            }
            System.out.println(classesPath);
            DB_PATH = classesPath + File.separator + "db";
            PATH = classesPath;
            LOG_PATH = tomcatPath + "/logs/erpLog.log";
            System.out.println("日志路径" + LOG_PATH);
            File dir = new File(DB_PATH);
            File file = new File(DB_PATH + File.separator + DB_NAME);
            File fileData = new File(DB_PATH + File.separator + DATA_NAME);
            VERSION = ReadProperties.getProperties("version");
            log.info("★☆★☆★☆★☆★☆欢迎使用以岭对接工具" + VERSION + "★☆★☆★☆★☆★☆");
            log.info("项目运行路径:" + PATH);
            System.out.println(DB_PATH);
            System.out.println(DB_PATH + File.separator + DB_NAME);
            // 流向临时文件目录
            ERP_PURCHASE_FLOW_PATH = PATH + File.separator + "erpPurchaseFlow";
            ERP_SALE_FLOW_PATH = PATH + File.separator + "erpSaleFlow";
            ERP_GOODS_BATCH_FLOW_PATH = PATH + File.separator + "goodsBatchFlow";
            ERP_SHOP_SALE_FLOW_PATH = PATH + File.separator + "erpShopSaleFlow";
            File dirPurchaseFlow = new File(ERP_PURCHASE_FLOW_PATH);
            File dirSaleFlow = new File(ERP_SALE_FLOW_PATH);
            File dirShopSaleFlow = new File(ERP_SHOP_SALE_FLOW_PATH);

            //1.初始化数据库
            if (dir.isDirectory()) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                if (!fileData.exists()) {
                    fileData.createNewFile();
                }
            } else {
                dir.mkdirs();
                file.createNewFile();
                fileData.createNewFile();
            }
            //设置可读权限
            file.setReadable(true, false);
            //设置可写权限
            file.setWritable(true, false);
            //设置可读权限
            fileData.setReadable(true, false);
            //设置可写权限
            fileData.setWritable(true, false);

            if (!dirPurchaseFlow.isDirectory()) {
                dirPurchaseFlow.mkdirs();
            }
            if (!dirSaleFlow.isDirectory()) {
                dirSaleFlow.mkdirs();
            }
            if (!dirShopSaleFlow.isDirectory()) {
                dirShopSaleFlow.mkdirs();
            }

            boolean boolSys = sqliteHelper.isExistTable(DB_PATH, DB_NAME, "sys_config");
            if (!boolSys) {
                String insertSysSql = "CREATE TABLE \"sys_config\" (\n" + "\"suId\"  TEXT(32),\n" + "\"name\"  TEXT(32) NOT NULL,\n" + "\"key\"  TEXT(32) NOT NULL,\n" + "\"secret\"  TEXT(32) NOT NULL,\n" + "\"urlPath\"  TEXT(64) NOT NULL,\n" + "\"address\"  TEXT(64) NOT NULL,\n" + "\"dbName\"  TEXT(32) NOT NULL,\n" + "\"dbType\"  TEXT(32) NOT NULL,\n" + "\"oracleType\"  TEXT(32),\n" + "\"oracleSid\"  TEXT(32),\n" + "\"dbCharset\"  TEXT(32) NOT NULL,\n" + "\"dbLoginName\"  TEXT(64),\n" + "\"dbLoginPW\"  TEXT(64),\n" + "\"dbIp\"  TEXT(32),\n" + "\"dbPort\"  TEXT(5),\n" + "\"tabPane\"  TEXT(64),\n" + "\"version\"  TEXT(32),\n" + "\"envName\" TEXT(32) DEFAULT prd,\n" + "\"syncStatus\"  TEXT(32),\n" + "\"updateTime\"  REAL,\n" + "\"createTime\"  REAL NOT NULL\n" + ");";
                sqliteHelper.executeUpdate(DB_PATH, DB_NAME, insertSysSql);
                log.info("初始化sys_config表数据成功");
            }

            boolean boolTask = sqliteHelper.isExistTable(DB_PATH, DB_NAME, "task_config");
            if (!boolTask) {
                String insertTaskSql = "CREATE TABLE \"task_config\" (\n" + "\"taskNo\"  TEXT(32) NOT NULL,\n" + "\"taskName\"  TEXT(32) NOT NULL,\n" + "\"taskInterval\"  TEXT(32) NOT NULL,\n" + "\"taskSQL\"  TEXT(1024) NOT NULL,\n" + "\"taskStatus\"  TEXT(1) NOT NULL,\n" + "\"taskKey\"  TEXT(64) NOT NULL,\n" + "\"springId\"  TEXT(32) NOT NULL,\n" + "\"methodName\"  TEXT(32) NOT NULL,\n" + "\"syncStatus\"  TEXT(32),\n" + "\"flowDateCount\"  TEXT(32) DEFAULT '',\n" + "\"updateTime\"  REAL,\n" + "\"createTime\"  REAL NOT NULL\n" + ");\n";
                sqliteHelper.executeUpdate(DB_PATH, DB_NAME, insertTaskSql);
                log.info("初始化task_config表数据成功");
            }


            boolean boolPush = sqliteHelper.isExistTable(DB_PATH, DB_NAME, "push_config");
            if (!boolPush) {
                String insertPushSql = "CREATE TABLE \"push_config\" (\n" + "\"dbName\"  TEXT(32) NOT NULL,\n" + "\"status\"  TEXT(1) NOT NULL,\n" + "\"dbType\"  TEXT(32) NOT NULL,\n" + "\"oracleType\"  TEXT(32),\n" + "\"oracleSid\" text(64) DEFAULT '',\n" + "\"dbCharset\"  TEXT(32) NOT NULL,\n" + "\"dbLoginName\"  TEXT(64),\n" + "\"dbLoginPW\"  TEXT(64),\n" + "\"dbIp\"  TEXT(32),\n" + "\"dbPort\"  TEXT(5),\n" + "\"orderSql\"  TEXT(128),\n" + "\"orderDetailSql\"  TEXT(128),\n" + "\"syncStatus\"  TEXT(32),\n" + "\"updateTime\"  REAL,\n" + "\"createTime\"  REAL NOT NULL\n" + ");";
                sqliteHelper.executeUpdate(DB_PATH, DB_NAME, insertPushSql);
            }
            log.info("初始化push_config表数据成功");

            //系统运行任务
            {
                TaskConfig redisCommandTask = new TaskConfig();
                redisCommandTask.setTaskInterval("3");
                redisCommandTask.setMethodName("syncData");
                redisCommandTask.setTaskNo(ErpTopicName.RedisCommand.getMethod());
                redisCommandTask.setTaskName(ErpTopicName.RedisCommand.getTopicNameDesc());
                redisCommandTask.setTaskStatus("1");
                redisCommandTask.setSpringId("clientCommandService");
                jobTaskService.addJob(redisCommandTask);

                TaskConfig heartTask = new TaskConfig();
                heartTask.setTaskInterval("10");
                heartTask.setMethodName("syncData");
                heartTask.setTaskNo(ErpTopicName.ErpHeart.getMethod());
                heartTask.setTaskName(ErpTopicName.ErpHeart.getTopicNameDesc());
                heartTask.setTaskStatus("1");
                heartTask.setSpringId("clientHeartService");
                jobTaskService.addJob(heartTask);

                TaskConfig taskConfigTask = new TaskConfig();
                taskConfigTask.setTaskInterval("1");
                taskConfigTask.setMethodName("syncData");
                taskConfigTask.setTaskNo(ErpTopicName.SyncConfig.getMethod());
                taskConfigTask.setTaskName(ErpTopicName.SyncConfig.getTopicNameDesc());
                taskConfigTask.setTaskStatus("1");
                taskConfigTask.setSpringId("clientConfigService");
                jobTaskService.addJob(taskConfigTask);
            }

            String versionDB = sysConfigDao.getSysConfigVersion(DB_PATH, DB_NAME);
            if (StrUtil.isNotEmpty(versionDB) && !versionDB.equals(VERSION)) {
                //工具版本
                updateSqliteTable.updateSqliteTableByVersion201(DB_PATH, DB_NAME);
                updateSqliteTable.updateSqliteTableByVersion204(DB_PATH, DB_NAME);

                sysConfigDao.updateSysConfigVersion(DB_PATH, DB_NAME, VERSION);
            }

            //2.读取系统配置里面的信息
            List<SysConfig> sysConfigList = sysConfigDao.executeQuerySysConfigList(DB_PATH, DB_NAME, "select * from sys_config");
            if (CollectionUtils.isEmpty(sysConfigList)) {
                log.info("还没有配置抓取数据配置，请配置数据库、appKey、密钥信息！！！");
                return true;
            }

            this.setSysConfig(sysConfigList.get(0));

            //3.加载远程配置信息
            String returnValue = clientHeartService.getHeartData(sysConfig);
            if ((returnValue != null) && (!returnValue.equals(""))) {
                Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
                Integer code = apiResponse.getCode();
                if (200 == code) {
                    String result = String.valueOf(apiResponse.getData());
                    String[] resultArray = result.split(",");
                    sysConfig.setName(resultArray[0]);
                    sysConfig.setSuId(Long.parseLong(resultArray[1]));
                    sysConfig.setEnvName(resultArray[2]);
                    HeartParamDTO heartParamDTO = JSON.parseObject(Base64.decodeStr(resultArray[3]), HeartParamDTO.class);
                    if (heartParamDTO != null) {
                        this.setHeartParamDTO(heartParamDTO);
                    } else {
                        log.error("HeartParamDTO 对象数据为null");
                    }
                }
            }

            //4.读取任务配置里面的信息
            List<TaskConfig> taskConfigList = taskConfigDao.executeQueryTaskConfigList(DB_PATH, DB_NAME, "select * from task_config");
            if (CollectionUtils.isEmpty(taskConfigList)) {
                log.error("读取任务配置里面的数据为null");
                return true;
            }

//            List<PushConfig> pushConfigList = pushConfigDao.executeQueryPushConfigList(DB_PATH, DB_NAME, "select * from push_config");
//            if (CollUtil.isNotEmpty(pushConfigList)) {
//                SysConfig sysConfig = PojoUtils.map(pushConfigList.get(0), SysConfig.class);
//                DataBaseConnection.getInstance().initTableInfo(sysConfig, InitErpConfig.tableColumnMap);
//            }

            for (TaskConfig taskConfig : taskConfigList) {
                jobTaskService.addJob(taskConfig);
            }

            //判断是否eas对接,eas对接的标识暂时写死
            if (sysConfig.getKey().equals("e42wjiikv5sfgigbfv12ymrshobpuh02")) {
                TaskConfig erpDeleteDataTaskConfig = new TaskConfig();
                erpDeleteDataTaskConfig.setTaskInterval("1");
                erpDeleteDataTaskConfig.setMethodName("syncData");
                erpDeleteDataTaskConfig.setTaskNo(ErpTopicName.ErpDeleteData.getMethod());
                erpDeleteDataTaskConfig.setTaskName(ErpTopicName.ErpDeleteData.getTopicNameDesc());
                erpDeleteDataTaskConfig.setTaskStatus("1");
                erpDeleteDataTaskConfig.setSpringId("clientDeleteDataService");
                jobTaskService.addJob(erpDeleteDataTaskConfig);
            }

            //判断是否大运河
            if (sysConfig.getKey().equals("dxny1ri0uwi4aok7o6e4wlb6ou03rzag") || sysConfig.getKey().equals("jp2vyk86453vw7hhxco21fe68fzl3xrj")) {
                TaskConfig easOrderTaskConfig = new TaskConfig();
                easOrderTaskConfig.setTaskInterval("1");
                easOrderTaskConfig.setMethodName("syncData");
                easOrderTaskConfig.setTaskNo(ErpTopicName.ErpOrderErpSn.getMethod());
                easOrderTaskConfig.setTaskName(ErpTopicName.ErpOrderErpSn.getTopicNameDesc());
                easOrderTaskConfig.setTaskStatus("1");
                easOrderTaskConfig.setSpringId("clientYuRenOrderService");
                jobTaskService.addJob(easOrderTaskConfig);
            }
            log.info("系统初始化成功");
            return true;
        } catch (Exception e) {
            log.error("系统初始化报错,10分以后系统再次初始", e);
        }
        return false;
    }


    public static SysConfig toSysConfig(PushConfig pushConfig) {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setDbName(pushConfig.getDbName());
        sysConfig.setDbLoginPW(pushConfig.getDbLoginPW());
        sysConfig.setDbLoginName(pushConfig.getDbLoginName());
        sysConfig.setOracleType(pushConfig.getOracleType());
        sysConfig.setOracleSid(pushConfig.getOracleSid());
        sysConfig.setDbCharset(pushConfig.getDbCharset());
        sysConfig.setDbIp(pushConfig.getDbIp());
        sysConfig.setDbPort(pushConfig.getDbPort());
        sysConfig.setDbType(pushConfig.getDbType());
        return sysConfig;
    }

    public SysConfig getSysConfig() {
        return sysConfig;
    }

    public void setSysConfig(SysConfig sysConfig) {
        this.sysConfig = sysConfig;
    }

    public Map<String, TaskConfig> getTaskConfigMap() {
        return taskConfigMap;
    }

    public void setTaskConfigMap(Map<String, TaskConfig> taskConfigMap) {
        this.taskConfigMap = taskConfigMap;
    }
}



