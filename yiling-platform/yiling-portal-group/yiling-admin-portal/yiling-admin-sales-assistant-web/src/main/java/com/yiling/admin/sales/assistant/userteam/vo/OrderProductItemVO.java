package com.yiling.admin.sales.assistant.userteam.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-订单对应的商品列表VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/30
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderProductItemVO extends BaseVO {

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String goodsSpecification;

    /**
     * 商品单价
     */
    @ApiModelProperty("商品单价")
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Integer goodsQuantity;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal goodsAmount;




}
