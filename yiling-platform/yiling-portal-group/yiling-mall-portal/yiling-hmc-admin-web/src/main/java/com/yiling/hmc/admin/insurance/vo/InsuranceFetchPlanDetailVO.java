package com.yiling.hmc.admin.insurance.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 拿药计划详情 VO
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceFetchPlanDetailVO extends BaseVO {

    /**
     * goodsId
     */
    @ApiModelProperty("goodsId")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 每月拿药量
     */
    @ApiModelProperty("每月拿药量")
    private Integer perMonthCount;


    /**
     * 规格信息
     */
    @ApiModelProperty("规格信息")
    private String specificInfo;

    /**
     * 参保价
     */
    @ApiModelProperty("参保价")
    private BigDecimal insurancePrice;

    /**
     * 以岭跟终端结算单价
     */
    @ApiModelProperty("以岭跟终端结算单价")
    private BigDecimal terminalSettlePrice;

    @ApiModelProperty("小计")
    private BigDecimal subTotal;


}
