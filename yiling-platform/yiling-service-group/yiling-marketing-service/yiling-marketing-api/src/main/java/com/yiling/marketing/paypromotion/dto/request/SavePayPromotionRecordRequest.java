package com.yiling.marketing.paypromotion.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/4/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePayPromotionRecordRequest extends BaseRequest {

    /**
     * 买家企业ID
     */
    private Long eid;

    /**
     * 买家企业名称
     */
    private String ename;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 批次号(一个批次号多个订单)
     */
    private String batchNo;

    /**
     * 商家支付促销活动Id
     */
    private Long shopActivityId;

    /**
     * 支付促销计算规则id
     */
    private Long shopRuleId;

    /**
     * 平台支付促销活动Id
     */
    private Long platformActivityId;

    /**
     * 支付促销计算规则id
     */
    private Long platformRuleId;
}
