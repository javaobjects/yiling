package com.yiling.erp.client.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.dao.PushConfigDao;
import com.yiling.erp.client.dao.SysConfigDao;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.erp.client.util.PopRequest;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Service("clientConfigService")
@Slf4j
public class ClientConfigServiceImpl implements SyncTaskService {

    @Autowired
    private SysConfigDao  sysConfigDao;
    @Autowired
    private PushConfigDao pushConfigDao;
    @Autowired
    private TaskConfigDao taskConfigDao;
    @Autowired
    private InitErpConfig initErpConfig;

    @Override
    public void syncData() {
        Map<String, String> map = new HashMap<>();
        //同步系统配置信息
        try {
            if(initErpConfig.getSysConfig()==null){
                return;
            }
            if(StrUtil.isEmpty(initErpConfig.getSysConfig().getKey())){
                return;
            }
            List<SysConfig> sysConfigList = sysConfigDao.executeQuerySysConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from sys_config where syncStatus=0");
            SysConfig sysConfig = null;
            if (!CollectionUtils.isEmpty(sysConfigList)) {
                sysConfig = sysConfigList.get(0);
                map.put("sys_config", JSON.toJSONString(sysConfig));
            }
            List<PushConfig> pushConfigList = pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from push_config where syncStatus=0");
            PushConfig pushConfig = null;
            if (!CollectionUtils.isEmpty(pushConfigList)) {
                pushConfig = pushConfigList.get(0);
                map.put("push_config", JSON.toJSONString(pushConfig));
            }
            List<TaskConfig> taskConfigList = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where syncStatus=0");
            if (!CollectionUtils.isEmpty(taskConfigList)) {
                map.put("task_config", JSONArray.toJSONString(taskConfigList));
            }
            //获取需要推送的订单接口
            if (map.size() <= 0) {
                return;
            }
            PopRequest pr = new PopRequest();
            Map<String, String> headParams = pr.generateHeadMap(ErpTopicName.SyncConfig.getMethod(), initErpConfig.getSysConfig().getKey());
            String returnValue = pr.getPost(initErpConfig.getSysConfig().getUrlPath(), JSON.toJSONString(map), headParams, initErpConfig.getSysConfig().getSecret());
            //插入到erp系统里
            if ((returnValue != null) && (!returnValue.equals(""))) {
                Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
                Integer code = apiResponse.getCode();
                if (200 != code) {
                    return;
                }
                if (sysConfig != null) {
                    sysConfigDao.updateSysConfigSyncStatus(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "1");
                }
                if (pushConfig != null) {
                    pushConfigDao.updatePushConfigSyncStatus(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "1");
                }
                if (!CollectionUtils.isEmpty(taskConfigList)) {
                    for (TaskConfig taskConfig : taskConfigList) {
                        taskConfigDao.updateTaskConfigSyncStatus(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, taskConfig.getTaskNo(), "1");
                    }
                }
            }
        } catch (Exception e) {
            log.error("客户端配置信息同步到服务器报错", e);
        }
    }
}
