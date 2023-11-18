package com.yiling.open.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.reflect.TypeToken;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpConfigApi;
import com.yiling.open.erp.dto.ClientPushConfigDTO;
import com.yiling.open.erp.dto.ClientSysConfigDTO;
import com.yiling.open.erp.dto.ClientTaskConfigDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpClientConfigController extends OpenBaseController {

    @DubboReference
    private ErpConfigApi erpConfigApi;

    @PostMapping(path = "/config/backups")
    public Result<String> backups(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(),
                    "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        Map<String, String> paramMap = JSON.parseObject(body, new TypeToken<Map<String, String>>() {
        }.getType());
        Date date = new Date();
        try {
            if (paramMap != null) {
                String sysConfigStr = paramMap.get("sys_config");
                if (!StrUtil.isEmpty(sysConfigStr)) {
                    SysConfig sysConfig = JSON.parseObject(sysConfigStr, SysConfig.class);
                    ClientSysConfigDTO clientSysConfig = new ClientSysConfigDTO();
                    clientSysConfig.setAddress(sysConfig.getAddress());
                    clientSysConfig.setCreateTime(sysConfig.getCreateTime());
                    clientSysConfig.setDbCharset(sysConfig.getDbCharset());
                    clientSysConfig.setDbIp(sysConfig.getDbIp());
                    clientSysConfig.setDbLoginName(sysConfig.getDbLoginName());
                    clientSysConfig.setDbLoginPw(sysConfig.getDbLoginPW());
                    clientSysConfig.setDbPort(sysConfig.getDbPort());
                    clientSysConfig.setDbType(sysConfig.getDbType());
                    clientSysConfig.setKey(sysConfig.getKey());
                    clientSysConfig.setDbName(sysConfig.getDbName());
                    clientSysConfig.setOracleType(sysConfig.getOracleType());
                    clientSysConfig.setName(sysConfig.getName());
                    clientSysConfig.setSecret(sysConfig.getSecret());
                    clientSysConfig.setTabPane(sysConfig.getTabPane());
                    clientSysConfig.setUrlPath(sysConfig.getUrlPath());
                    clientSysConfig.setUpdateTime(sysConfig.getUpdateTime());
                    clientSysConfig.setVersion(sysConfig.getVersion());
                    clientSysConfig.setSuId(suId);
                    clientSysConfig.setOracleSid(sysConfig.getOracleSid());
                    clientSysConfig.setSyncTime(date);
                    erpConfigApi.insertSysConfig(clientSysConfig);
                }
                String taskConfigStr = paramMap.get("task_config");
                if (!StrUtil.isEmpty(taskConfigStr)) {
                    List<TaskConfig> taskConfigList = JSONArray.parseArray(taskConfigStr, TaskConfig.class);
                    for (TaskConfig taskConfig : taskConfigList) {
                        ClientTaskConfigDTO clientTaskConfig = new ClientTaskConfigDTO();
                        clientTaskConfig.setSuId(suId);
                        clientTaskConfig.setCreateTime(taskConfig.getCreateTime());
                        clientTaskConfig.setMethodName(taskConfig.getMethodName());
                        clientTaskConfig.setSpringId(taskConfig.getSpringId());
                        clientTaskConfig.setTaskInterval(taskConfig.getTaskInterval());
                        clientTaskConfig.setTaskKey(taskConfig.getTaskKey());
                        clientTaskConfig.setTaskName(taskConfig.getTaskName());
                        clientTaskConfig.setTaskNo(taskConfig.getTaskNo());
                        clientTaskConfig.setTaskSql(taskConfig.getTaskSQL());
                        clientTaskConfig.setTaskStatus(taskConfig.getTaskStatus());
                        clientTaskConfig.setUpdateTime(taskConfig.getUpdateTime());
                        clientTaskConfig.setSyncStatus(taskConfig.getSyncStatus());
                        clientTaskConfig.setFlowDateCount(transfFlowDateCount(taskConfig.getFlowDateCount()));
                        clientTaskConfig.setSyncTime(date);
                        erpConfigApi.insertTaskConfig(clientTaskConfig);
                    }
                }
                String pushConfigStr = paramMap.get("push_config");
                if (!StrUtil.isEmpty(pushConfigStr)) {
                    PushConfig pushConfig = JSON.parseObject(pushConfigStr, PushConfig.class);
                    if (pushConfig == null) {
                        return Result.failed(OpenErrorCode.Remote_Service_Error.getCode(), "配置信息上传备份为空");
                    }
                    ClientPushConfigDTO clientPushConfig = new ClientPushConfigDTO();
                    clientPushConfig.setStatus(pushConfig.getStatus());
                    clientPushConfig.setCreateTime(pushConfig.getCreateTime());
                    clientPushConfig.setDbCharset(pushConfig.getDbCharset());
                    clientPushConfig.setDbIp(pushConfig.getDbIp());
                    clientPushConfig.setDbLoginName(pushConfig.getDbLoginName());
                    clientPushConfig.setDbLoginPw(pushConfig.getDbLoginPW());
                    clientPushConfig.setDbName(pushConfig.getDbName());
                    clientPushConfig.setDbPort(pushConfig.getDbPort());
                    clientPushConfig.setDbType(pushConfig.getDbType());
                    clientPushConfig.setOracleType(pushConfig.getOracleType());
                    clientPushConfig.setStatus(pushConfig.getStatus());
                    clientPushConfig.setOrderSql(pushConfig.getOrderSql());
                    clientPushConfig.setOrderDetailSql(pushConfig.getOrderDetailSql());
                    clientPushConfig.setUpdateTime(pushConfig.getUpdateTime());
                    clientPushConfig.setSuId(suId);
                    clientPushConfig.setOracleSid(pushConfig.getOracleSid());
                    clientPushConfig.setSyncStatus(pushConfig.getSyncStatus());
                    clientPushConfig.setSyncTime(date);
                    erpConfigApi.insertPushConfig(clientPushConfig);
                }
            }
        } catch (Exception e) {
            log.error("备份客户端配置信息错误", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error.getCode(), e.getMessage());
        }
        return Result.success();
    }


    @PostMapping(path = "/config/get")
    public Result<String> get(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(),
                    "参数" + OpenConstants.METHOD + "不存在");
        }
        Map<String, String> returnMap = new HashMap<>();
        try {
            ClientSysConfigDTO clientSysConfig=erpConfigApi.getSysConfigBySuid(suId);
            returnMap.put("sys_config", JSON.toJSONString(PojoUtils.map(clientSysConfig, SysConfig.class)));
            List<ClientTaskConfigDTO> clientTaskConfigList = erpConfigApi.getTaskConfigListBySuid(suId);
            returnMap.put("task_config", JSONArray.toJSONString(PojoUtils.map(clientTaskConfigList, TaskConfig.class)));
            ClientPushConfigDTO clientPushConfig = erpConfigApi.getPushConfigBySuid(suId);
            returnMap.put("push_config", JSON.toJSONString(PojoUtils.map(clientPushConfig, PushConfig.class)));
        } catch (
                Exception e) {
            log.error("备份客户端配置信息错误", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error.getCode(), e.getMessage());
        }
        return Result.success(JSON.toJSONString(returnMap));
    }

    public String transfFlowDateCount(String flowDateCount){
        if(StrUtil.isBlank(flowDateCount)){
            return "0";
        }
        String regex = "[0-9]*";
        Pattern pattern = Pattern.compile(regex);
        boolean matches = pattern.matcher(flowDateCount).matches();
        if(!matches){
            return "0";
        }
        return flowDateCount;
    }

}
