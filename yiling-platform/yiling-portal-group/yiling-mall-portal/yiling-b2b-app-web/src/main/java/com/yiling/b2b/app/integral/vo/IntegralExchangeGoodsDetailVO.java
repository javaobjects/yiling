package com.yiling.b2b.app.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换商品详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-18
 */
@Data
@Accessors(chain = true)
public class IntegralExchangeGoodsDetailVO extends BaseVO {

    /**
     * 物品ID
     */
    @ApiModelProperty("物品ID")
    private Long goodsId;

    /**
     * 物品名称
     */
    @ApiModelProperty("物品名称")
    private String goodsName;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 商品图片地址
     */
    @ApiModelProperty("商品图片地址")
    private String pictureUrl;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @ApiModelProperty("商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券")
    private Integer goodsType;

    /**
     * 兑换所需积分
     */
    @ApiModelProperty("兑换所需积分")
    private Integer exchangeUseIntegral;

    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer inventory;

    /**
     * 折扣信息
     */
    @ApiModelProperty("折扣信息（优惠券时存在该字段）")
    private String discountInfo;

    /**
     * 可用周期
     */
    @ApiModelProperty("可用周期（优惠券时存在该字段）")
    private String usePeriod;

    /**
     * 商品详情
     */
    @ApiModelProperty("商品详情（真实物品或虚拟物品时存在该字段）")
    private String introduction;

}
