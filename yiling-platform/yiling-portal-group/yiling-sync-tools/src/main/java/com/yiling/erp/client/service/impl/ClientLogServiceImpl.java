package com.yiling.erp.client.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.erp.client.util.PopRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service("clientLogService")
public class ClientLogServiceImpl {

    @Autowired
    public InitErpConfig erpCommon;

    public void sendLog(String msg) {
        try {
            //然后判断key是否合法
            SysConfig sysConfig = erpCommon.getSysConfig();
            if (sysConfig == null) {
                return;
            }
            if (StringUtils.isEmpty(sysConfig.getKey())) {
                return;
            }
            if (StringUtils.isEmpty(sysConfig.getSecret())) {
                return;
            }
            if (StringUtils.isEmpty(sysConfig.getUrlPath())) {
                return;
            }

            PopRequest pr = new PopRequest();
            Map<String, String> headMap = pr.generateHeadMap(ErpTopicName.ErpLog.getMethod(), sysConfig.getKey());
            String json = msg;
            pr.getPost(sysConfig.getUrlPath(), json, headMap, sysConfig.getSecret());
        }catch (Exception e){
            log.error("客户端日志发送报错",e);
        }
    }
}
