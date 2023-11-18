package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 订单处方 Request
 *
 * @author: fan.shen
 * @date: 2022/4/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPrescriptionGoodsRequest extends BaseRequest {

    /**
     * 兑付订单id
     */
    private Long orderId;

    /**
     * 兑付订单处方id
     */
    private Long orderPrescriptionId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 数量
     */
    private Integer goodsQuantity;

    /**
     * 用法用量
     */
    private String usageInfo;

    /**
     * 商品价格
     */
    private String goodsPrice;

}
