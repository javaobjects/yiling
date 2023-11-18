package com.yiling.admin.b2b.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class YeeSettlePageListItemVO extends BaseVO {

    /**
     * 结算订单号
     */
    @ApiModelProperty(value = "结算订单号")
    private String summaryNo;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "结算时间")
    private Date createTime;

    /**
     * 应结金额
     */
    @ApiModelProperty(value = "应结金额")
    private BigDecimal settleAmount;

    /**
     * 结算手续费
     */
    @ApiModelProperty(value = "结算手续费")
    private BigDecimal realFee;

    /**
     * 结算到账金额
     */
    @ApiModelProperty(value = "结算到账金额")
    private BigDecimal realAmount;


    /**
     * 结算订单状态： 1-结算成功 2-失败 3-待处理 4-待出款 5-结算异常 6-银行处理中
     */
    @ApiModelProperty(value = "结算订单状态： 1-结算成功 2-失败 3-待处理 4-待出款 5-结算异常 6-银行处理中")
    private Integer status;

}
