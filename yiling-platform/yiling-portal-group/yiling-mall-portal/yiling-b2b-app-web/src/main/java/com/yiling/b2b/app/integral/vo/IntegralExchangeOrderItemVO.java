package com.yiling.b2b.app.integral.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单分页列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-30
 */
@Data
@Accessors(chain = true)
public class IntegralExchangeOrderItemVO extends BaseVO {

    /**
     * 兑换订单号
     */
    @ApiModelProperty("兑换订单号")
    private String orderNo;

    /**
     * 兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @ApiModelProperty("兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券")
    private Integer goodsType;

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
     * 物品图片地址
     */
    @ApiModelProperty("物品图片地址")
    private String goodsPictureUrl;

    /**
     * 兑换数量
     */
    @ApiModelProperty("兑换数量")
    private Integer exchangeNum;

    /**
     * 订单积分值
     */
    @ApiModelProperty("订单积分值")
    private Integer orderIntegral;

    /**
     * 兑换状态：1-未兑换 2-已兑换
     */
    @ApiModelProperty("兑换状态：1-未兑换 2-已兑换")
    private Integer status;

    /**
     * 订单兑付时间
     */
    @ApiModelProperty("订单兑付时间")
    private Date exchangeTime;

    /**
     * 兑换提交时间
     */
    @ApiModelProperty("兑换提交时间")
    private Date submitTime;

}
