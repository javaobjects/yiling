package com.yiling.admin.data.center.enterprise.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账期临时额度列表专用dto
 */
@Data
@Accessors(chain = true)
public class QuotaOrderVO extends BaseDTO {

    private static final long serialVersionUID = -1874940983299963004L;

    /**
     * orderNo
     */
    @ApiModelProperty("orderNo")
    private String orderNo;

    /**
     * 采购商名称
     */
    @ApiModelProperty("采购商名称")
    private String customerName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 应还款日期
     */
    @ApiModelProperty("应还款日期")
    private Date expirationTime;

    /**
     * 已使用金额
     */
    @ApiModelProperty("已使用金额")
    private BigDecimal usedAmount;
    /**
     * 订单金额=订单总金额-(订单上的现折总金额+订单上的票折总金额+驳回退货单的退款金额)
     */
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    /**
     * 退款金额
     */
    @ApiModelProperty("退款金额")
    private BigDecimal returnAmount;

    /**
     * 还款状态
     */
    @ApiModelProperty("还款状态")
    private Integer repaymentStatus;

    /**
     * 已还款金额
     */
    @ApiModelProperty("已还款金额")
    private BigDecimal repaymentAmount;

    /**
     * 未还款金额
     */
    @ApiModelProperty("未还款金额")
    private BigDecimal unRepaymentAmount;

}
