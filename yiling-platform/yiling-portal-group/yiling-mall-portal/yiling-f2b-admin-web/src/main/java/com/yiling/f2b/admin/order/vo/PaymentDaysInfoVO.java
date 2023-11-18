package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户账期账户表
 * </p>
 *
 * @author gxl
 * @date 2021-05-21
 */
@Data
@Accessors(chain = true)
public class PaymentDaysInfoVO extends BaseVO {



    /**
     * 账期额度
     */
    @ApiModelProperty(value = "账期额度")
    private BigDecimal totalAmount;

    /**
     * 临时额度
     */
    @ApiModelProperty(value = "临时额度")
    private BigDecimal temporaryAmount;

    /**
     * 已使用额度
     */
    @ApiModelProperty(value = "已使用额度")
    private BigDecimal usedAmount;

    /**
     * 已还款额度
     */
    @ApiModelProperty(value = "已还款额度")
    private BigDecimal repaymentAmount;

    /**
     * 可用额度
     */
    @ApiModelProperty(value = "可用额度")
    private BigDecimal availableAmount;

    /**
     * 还款周期（天）
     */
    @ApiModelProperty(value = "还款周期（天）")
    private Integer period;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

    /**
     * 计算可用额度
     *
     * @return
     */
    public BigDecimal getAvailableAmount() {
        return NumberUtil.add(this.totalAmount, this.temporaryAmount, this.repaymentAmount).subtract(this.usedAmount);
    }
}
