package com.yiling.erp.client.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.quartz.JobTaskService;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.ComputerInfoUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.StringUtil;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.HeartParamDTO;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.erp.client.util.PopRequest;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service("clientHeartService")
public class ClientHeartServiceImpl implements SyncTaskService {

    @Autowired
    public InitErpConfig erpCommon;
    @Autowired
    public JobTaskService jobTaskService;

    @Override
    public void syncData() {
        try {
            //然后判断key是否合法
            if(erpCommon.getSysConfig()==null){
                return;
            }
            if(StrUtil.isEmpty(erpCommon.getSysConfig().getKey())){
                return;
            }
            SysConfig sysConfig = erpCommon.getSysConfig();
            String returnValue =this.getHeartData(sysConfig);
            if ((returnValue != null) && (!returnValue.equals(""))) {
                Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
                Integer code = apiResponse.getCode();
                if (200 != code) {
                    log.error("SysConfigController saveDB, apiResponse -> {}", returnValue);
                }
                // name|suId
                String result = String.valueOf(apiResponse.getData());
                String[] resultArray = result.split(",");
                sysConfig.setName(resultArray[0]);
                sysConfig.setSuId(Long.parseLong(resultArray[1]));
                sysConfig.setEnvName(resultArray[2]);
                HeartParamDTO heartParamDTO = JSON.parseObject(Base64.decodeStr(resultArray[3]), HeartParamDTO.class);
                if (heartParamDTO != null) {
                    erpCommon.setHeartParamDTO(heartParamDTO);
                }else{
                    log.error("HeartParamDTO 对象数据为null");
                }
            }
        } catch (Exception e) {
            log.error("心跳接口请求错误", e);
        }
    }

    public String getHeartData(SysConfig sysConfig) throws IOException {
        if (sysConfig == null) {
            return null;
        }
        if (StringUtils.isEmpty(sysConfig.getKey())) {
            return null;
        }
        if (StringUtils.isEmpty(sysConfig.getSecret())) {
            return null;
        }
        if (StringUtils.isEmpty(sysConfig.getUrlPath())) {
            return null;
        }
        PopRequest pr = new PopRequest();
        Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.ErpHeart.getMethod(), sysConfig.getKey());

        String port = ComputerInfoUtil.getPort();
        String processId = StringUtil.clearAllSpace(ComputerInfoUtil.getProcessId(port));
        String runTaskNos = getRunTaskNos();
        String mac = ComputerInfoUtil.getMacAddress();
        String ip = ComputerInfoUtil.getIpAddr();
        log.debug(">>>>> clientHeartService, PORT -> {}, PROCESS_ID -> {}, runTaskNos -> {}, mac -> {}, ip -> {}", port, processId, runTaskNos, mac, ip);

        String json = pr.addSysHeartLog(processId, InitErpConfig.PATH, runTaskNos, InitErpConfig.VERSION, mac, ip);
        String returnValue = pr.getPost(sysConfig.getUrlPath(), json, headMap, sysConfig.getSecret());
        return returnValue;
    }

    private String getRunTaskNos() {
        try {
            List<TaskConfig> taskConfigList = jobTaskService.getAllJob();
            if (CollUtil.isEmpty(taskConfigList)) {
                return "";
            }
            StringJoiner sj = new StringJoiner(",");
            taskConfigList.forEach(t -> {
                sj.add(t.getTaskNo());
            });
            if (ObjectUtil.isNotNull(sj) && sj.length() > 0) {
                return sj.toString();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "";
    }
}
