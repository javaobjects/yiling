package com.yiling.mall.order.bo;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.OrderTypeEnum;

import lombok.Data;



/**
 * 计算订单优惠参数
 *
 * @author zhigang.guo
 * @date: 2022/9/22
 */
@Data
public class CalOrderDiscountContextBO<T extends CreateOrderRequest> {

    /**
     * 平台优惠劵Id
     */
    private Long platformCustomerCouponId;

    /**
     * 商家优惠劵Id
     */
    private  Map<Long, Long> shopCustomerCouponIdMap;

    /**
     * 预售活动信息
     */
    private Map<String, PresaleActivityGoodsDTO> preSaleActivityGoodsDTOMap;

    /**
     * 平台支付促销活动ID
     */
    private Long platformPaymentActivityId;

    /**
     * 平台支付促销活动规则Id
     */
    private Long platformActivityRuleIdId;

    /**
     * 商家支付促销活动Id
     */
    private Map<Long, Long> shopPaymentActivityIdMap;

    /**
     * 商家支付促销活动规则Id
     */
    private Map<Long,Long> shopActivityRuleIdIdMap;

    /**
     * 订单是否有赠品
     */
    private Map<Long,Boolean> orderHasGiftMap;

    /**
     * 订单拆单参数
     */
    private List<T> createOrderRequestList;

    /**
     * 下单平台
     */
    private PlatformEnum platformEnum;

    /**
     * 订单类型
     */
    private OrderTypeEnum orderTypeEnum;

    /**
     * 登录企业
     */
    private Long buyerEid;

    /**
     * 操作人用户Id
     */
    private Long opUserId;


}
