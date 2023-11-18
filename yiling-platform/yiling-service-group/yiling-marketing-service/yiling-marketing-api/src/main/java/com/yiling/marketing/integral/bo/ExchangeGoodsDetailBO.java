package com.yiling.marketing.integral.bo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换商品详情 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-18
 */
@Data
@Accessors(chain = true)
public class ExchangeGoodsDetailBO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 物品ID
     */
    private Long goodsId;

    /**
     * 物品名称
     */
    private String goodsName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品图片地址
     */
    private String pictureUrl;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    private Integer goodsType;

    /**
     * 兑换所需积分
     */
    private Integer exchangeUseIntegral;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 折扣信息（优惠券时存在该字段）
     */
    private String discountInfo;

    /**
     * 可用周期（优惠券时存在该字段）
     */
    private String usePeriod;

    /**
     * 商品详情（真实物品或虚拟物品时存在该字段）
     */
    private String introduction;

}
