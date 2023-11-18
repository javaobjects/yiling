package com.yiling.open.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 流向订单来源类型映射关系
 *
 * @author: houjie.sun
 * @date: 2022/4/25
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "open.flowordersource")
public class ErpFlowOrderSourceConfig {

    /**
     * 库存流向订单来源映射关系
     */
    private String goodsbatchflow;

    /**
     * 采购流向订单来源映射关系
     */
    private String purchaseflow;

    /**
     * 销售流向订单来源映射关系
     */
    private String saleflow;

    /**
     * 库存流向订单来源map
     *
     * @return
     */
    public Map<String,Integer> getGoodsBatchFlowOrderSourceMap(){
        return convertFlowOrderSourceMap(goodsbatchflow);
    }

    /**
     * 采购流向订单来源map
     *
     * @return
     */
    public Map<String,Integer> getPurchaseFlowOrderSourceMap(){
        return convertFlowOrderSourceMap(purchaseflow);
    }

    /**
     * 采购流向订单来源map
     *
     * @return
     */
    public Map<String,Integer> getSaleFlowOrderSourceMap(){
        return convertFlowOrderSourceMap(saleflow);
    }

    /**
     * 订单来源映射关系转换
     *
     * @param config 订单来源映射关系配置
     * @return
     */
    public Map<String,Integer> convertFlowOrderSourceMap(String config){
        if(StrUtil.isBlank(config)){
            return MapUtil.empty();
        }
        Map<Integer, String> orderSourConfigMap = (Map) JSON.parse(config);
        Map<String,Integer> goodsBatchFlowMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : orderSourConfigMap.entrySet()) {
            Integer type = entry.getKey();
            String names = entry.getValue();
            if(ObjectUtil.isNull(type) || StrUtil.isBlank(names)){
                return MapUtil.empty();
            }
            List<String> orderSourceNameList = Convert.toList(String.class, names);
            for (String orderSourceName : orderSourceNameList) {
                goodsBatchFlowMap.put(orderSourceName, type);
            }
        }
        return goodsBatchFlowMap;
    }

}
