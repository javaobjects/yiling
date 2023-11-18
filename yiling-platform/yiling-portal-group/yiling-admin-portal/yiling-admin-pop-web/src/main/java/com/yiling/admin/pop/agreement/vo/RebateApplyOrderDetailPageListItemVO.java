package com.yiling.admin.pop.agreement.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/8/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateApplyOrderDetailPageListItemVO extends BaseVO {

    /**
     * 协议名称
     */
    @ApiModelProperty(value = "协议名称")
    private String     name;

    /**
     * 协议内容
     */
    @ApiModelProperty(value = "协议内容")
    private String     content;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String     orderCode;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long       goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String     goodsName;

    /**
     * 商品erp内码
     */
    @ApiModelProperty(value = "商品erp内码")
    private String     erpCode;

    /**
     * 单据类型 1-订单 2-退货单
     */
    @ApiModelProperty(value = "单据类型 1-订单 2-退货单")
    private Integer    orderType;

    /**
     * 成交数量
     */
    @ApiModelProperty(value = "成交数量")
    private Long       goodsQuantity;

    /**
     * 销售金额
     */
    @ApiModelProperty(value = "销售金额")
    private BigDecimal price;

    /**
     * 返利金额
     */
    @ApiModelProperty(value = "返利金额")
    private BigDecimal discountAmount;

}
