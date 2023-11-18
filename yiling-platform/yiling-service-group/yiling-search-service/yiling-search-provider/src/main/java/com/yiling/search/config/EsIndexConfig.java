package com.yiling.search.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * @author shichen
 * @类名 EsIndexConfig
 * @描述
 * @创建时间 2023/2/13
 * @修改人 shichen
 * @修改时间 2023/2/13
 **/
@Component
@RefreshScope
@Getter
public class EsIndexConfig {
    @Value("${elasticsearch.goodsEsIndex}")
    private String goodsEsIndex;

    @Bean
    public String goodsEsIndex(){
        return goodsEsIndex;
    }

    @Value("${elasticsearch.flowSaleEsIndex}")
    private String flowSaleEsIndex;

    @Bean
    public String flowSaleEsIndex(){
        return flowSaleEsIndex;
    }

    @Value("${elasticsearch.flowPurchaseEsIndex}")
    private String flowPurchaseEsIndex;

    @Bean
    public String flowPurchaseEsIndex(){
        return flowPurchaseEsIndex;
    }

    @Value("${elasticsearch.flowEsGoodsBatchDetailIndex}")
    private String flowEsGoodsBatchDetailIndex;

    @Bean
    public String flowEsGoodsBatchDetailIndex(){
        return flowEsGoodsBatchDetailIndex;
    }

    @Value("${elasticsearch.flowEnterpriseGoodsMappingIndex}")
    private String flowEnterpriseGoodsMappingIndex;

    @Bean
    public String flowEnterpriseGoodsMappingIndex(){
        return flowEnterpriseGoodsMappingIndex;
    }


    @Value("${elasticsearch.flowEnterpriseCustomerMappingIndex}")
    private String flowEnterpriseCustomerMappingIndex;

    @Bean
    public String flowEnterpriseCustomerMappingIndex(){
        return flowEnterpriseCustomerMappingIndex;
    }
}
