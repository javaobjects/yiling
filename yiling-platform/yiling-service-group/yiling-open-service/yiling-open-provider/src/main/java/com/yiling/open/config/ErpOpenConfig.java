package com.yiling.open.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.erp.bo.ErpInstallEmployeeInfoDetailBO;
import com.yiling.open.erp.bo.ErpMonitorCountInfoDetailBO;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * erp对接请求次数阈值信息
 * 
 * @author: houjie.sun
 * @date: 2022/3/18
 */
@Getter
@Setter
@Component
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "open")
public class ErpOpenConfig {

    /**
     * erp对接请求次数阈值信息
     */
    private String monitorCount;

    /**
     * erp实施负责人手机号信息
     */
    private String installEmployeeInfo;

    /**
     * 抽取工具版本
     */
    private String version;

    /**
     * erp监控超过阈值提醒短信发送开关
     */
    private Boolean monitorSmsFlag;


    public List<ErpMonitorCountInfoDetailBO> getErpMonitorCountInfoDetail() {
        if (StrUtil.isBlank(monitorCount)) {
            return ListUtil.empty();
        }
        return JSONArray.parseArray(monitorCount, ErpMonitorCountInfoDetailBO.class);
    }

    public List<ErpInstallEmployeeInfoDetailBO> getErpInstallEmployeeInfoDetail() {
        if (StrUtil.isBlank(installEmployeeInfo)) {
            return ListUtil.empty();
        }
        return JSONArray.parseArray(installEmployeeInfo, ErpInstallEmployeeInfoDetailBO.class);
    }

}
