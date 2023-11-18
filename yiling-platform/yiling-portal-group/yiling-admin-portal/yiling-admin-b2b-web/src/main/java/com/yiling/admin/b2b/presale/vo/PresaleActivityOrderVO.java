package com.yiling.admin.b2b.presale.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleActivityOrderVO extends BaseVO {

    @ApiModelProperty("时间")
    private Date createTime;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String name;


    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 创建人名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    @ApiModelProperty("订单号")
    private String orderNo;
    /**
     * 订单id
     */
    @ApiModelProperty("订单编号")
    private Long orderId;

    /**
     * 订单id
     */
    @ApiModelProperty("商品编号")
    private Long goodsId;

    /**
     * 订单id
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;

    /**
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String manufacturer;

    /**
     * 商家-卖家
     */
    @ApiModelProperty("商家")
    private String sellerEname;

    /**
     * 商家-卖家
     */
    @ApiModelProperty("预售价")
    private BigDecimal presalePrice;

    /**
     * 购买数量
     */
    @ApiModelProperty("数量")
    private Integer goodsQuantity;

    /**
     * 预售类型
     */
    @ApiModelProperty("预售类型")
    private String presaleTypeStr;

    /**
     * 定金比例
     */
    @ApiModelProperty("定金比例")
    private BigDecimal depositRatio;
    @ApiModelProperty("促销方式")
    private String goodsPresaleType;
    @ApiModelProperty("定金")
    private BigDecimal depositAmount;
    @ApiModelProperty("尾款")
    private BigDecimal balanceAmount;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;
    @ApiModelProperty("实付总金额")
    private BigDecimal totalAmount;
    /**
     * 商家-卖家
     */
    @ApiModelProperty("状态")
    private String status;
}
