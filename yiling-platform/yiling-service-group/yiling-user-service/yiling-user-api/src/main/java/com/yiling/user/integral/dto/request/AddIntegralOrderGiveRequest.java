package com.yiling.user.integral.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单签收送积分 Request
 *
 * @author: lun.yu
 * @date: 2023-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralOrderGiveRequest extends BaseRequest {

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手（枚举：IntegralRulePlatformEnum）
     */
    @NotNull
    private Integer platform;

    /**
     * uid（B2B为企业ID）
     */
    @NotNull
    private Long uid;

    /**
     * 订单ID
     */
    @NotNull
    private Long orderId;

    /**
     * 订单号
     */
    @NotEmpty
    private String orderNo;

    /**
     * 商家企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 订单金额
     */
    @NotNull
    private BigDecimal orderAmount;

    /**
     * 该笔订单支付方式：1-线上支付 2-线下支付 3-账期支付
     */
    @NotNull
    private Integer paymentMethod;

    /**
     * 商品信息
     */
    @NotEmpty
    private List<GoodsInfo> goodsInfoList;

    @Data
    public static class GoodsInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 商品ID
         */
        private Long goodsId;

        /**
         * 标准库ID
         */
        private Long standardId;

        /**
         * 规格ID
         */
        private Long sellSpecificationsId;

        /**
         * 商品数量
         */
        private Integer goodsNum;

        /**
         * 商品金额 = 商品数量 * 单价
         */
        private BigDecimal goodsAmount;

    }

}
