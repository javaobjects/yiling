package com.yiling.sales.assistant.app.enterprise.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 客户商品总计信息 VO
 *
 * @author: lun.yu
 * @date: 2021/10/8
 */
@Builder
@Data
@ApiModel("客户商品总计信息")
public class UserProductCountVO {

    /**
     * 购买商品件数
     */
    @ApiModelProperty("购买商品件数")
    private Long orderNumberType;

    /**
     * 收货总金额
     */
    @ApiModelProperty("收货总金额")
    private BigDecimal receiveTotalAmount;

    /**
     * 收货总数量
     */
    @ApiModelProperty("收货总数量")
    private Integer receiveTotalQuantity;

    /**
     * 发货总数量
     */
    @ApiModelProperty("发货总数量")
    private Integer deliveryTotalQuantity;

    /**
     * 发货总金额
     */
    @ApiModelProperty("发货总金额")
    private BigDecimal deliveryTotalAmount;

}
