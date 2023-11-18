package com.yiling.dataflow.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: houjie.sun
 * @date: 2022/6/28
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "dataflow")
public class FlowSettlementEnterpriseTagConfig {

    /**
     * 流向报表商业标签
     */
    private List<String> enterpriseTag;

    public List<String> getFlowSettlementEnterpriseTagList(){
        if(CollUtil.isEmpty(enterpriseTag)){
            return ListUtil.empty();
        }
        return enterpriseTag;
    }

}
