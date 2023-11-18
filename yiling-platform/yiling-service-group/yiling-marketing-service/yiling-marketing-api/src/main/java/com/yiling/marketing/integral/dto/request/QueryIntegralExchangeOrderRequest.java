package com.yiling.marketing.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询积分兑换订单 Request
 *
 * @author: lun.yu
 * @date: 2023-02-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralExchangeOrderRequest extends BaseRequest {

    /**
     * 积分兑换商品表的ID
     */
    @Eq
    private Long exchangeGoodsId;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    @Eq
    private Integer platform;

    /**
     * 用户ID
     */
    @Eq
    private Long uid;

    /**
     * 物品ID
     */
    @Eq
    private Long goodsId;

    /**
     * 兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @Eq
    private Integer goodsType;

    /**
     * 兑换订单号
     */
    @Eq
    private String orderNo;

}
