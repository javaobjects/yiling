package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流向解封消息主题名称
 *
 * @author: houjie.sun
 * @date: 2022/4/24
 */
@Getter
@AllArgsConstructor
public enum ErpFlowSealedTopicName {

    UN_LOCK_PURCHASE("erp_flow_sealed_unLock_purchase","采购流向解封消息主题名称"),
    UN_LOCK_SALE("erp_flow_sealed_unLock_sale","销售流向解封消息主题名称"),
    UN_LOCK_SHOP_SALE("erp_flow_sealed_unLock_shop_sale","连锁纯销流向解封消息主题名称"),
    ;

    private String topic;
    private String desc;

    public static ErpFlowSealedTopicName getFromCode(String topic) {
        for(ErpFlowSealedTopicName e: ErpFlowSealedTopicName.values()) {
            if(e.getTopic().equals(topic)) {
                return e;
            }
        }
        return null;
    }
}
