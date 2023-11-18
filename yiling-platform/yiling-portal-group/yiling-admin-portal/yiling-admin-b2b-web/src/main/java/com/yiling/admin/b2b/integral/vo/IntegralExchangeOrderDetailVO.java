package com.yiling.admin.b2b.integral.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单兑换详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralExchangeOrderDetailVO extends BaseVO {

    /**
     * 兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @ApiModelProperty("兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券")
    private Integer goodsType;

    /**
     * 收货信息（真实物品）
     */
    @ApiModelProperty("收货信息（真实物品）")
    private ReceiptInfoVO receiptInfo;

    /**
     * 卡密信息
     */
    @ApiModelProperty("卡密信息（虚拟物品）")
    private List<CardInfo> cardInfoList;

    /**
     * 优惠券ID
     */
    @ApiModelProperty("优惠券ID（商品优惠券/会员优惠券）")
    private Long couponId;

    @Data
    public static class CardInfo implements Serializable {

        /**
         * 卡号
         */
        private String cardNo;

        /**
         * 密码
         */
        private String password;

    }

    @Data
    public static class ReceiptInfoVO implements Serializable {

        /**
         * 收货人
         */
        @ApiModelProperty("收货人")
        private String contactor;

        /**
         * 联系电话
         */
        @ApiModelProperty("联系电话")
        private String contactorPhone;

        /**
         * 所属省份名称
         */
        @ApiModelProperty("所属省份名称")
        private String provinceName;

        /**
         * 所属省份编码
         */
        @ApiModelProperty("所属省份编码")
        private String provinceCode;

        /**
         * 所属城市名称
         */
        @ApiModelProperty("所属城市名称")
        private String cityName;

        /**
         * 所属城市编码
         */
        @ApiModelProperty("所属城市编码")
        private String cityCode;

        /**
         * 所属区域名称
         */
        @ApiModelProperty("所属区域名称")
        private String regionName;

        /**
         * 所属区域编码
         */
        @ApiModelProperty("所属区域编码")
        private String regionCode;

        /**
         * 详细地址
         */
        @ApiModelProperty("详细地址")
        private String address;

        /**
         * 快递公司（见字典）
         */
        @ApiModelProperty("快递公司（见字典）")
        private String expressCompany;

        /**
         * 快递单号
         */
        @ApiModelProperty("快递单号")
        private String expressOrderNo;

    }



}
