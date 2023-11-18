package com.yiling.f2b.admin.payment.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付方式
 *
 * @author yuecheng.chen
 * @date 2021-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentMethodVO extends BaseVO {

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 还款周期
     */
    @ApiModelProperty("还款周期")
    private Integer period;

    /**
     * 账期额度
     */
    @ApiModelProperty("账期额度")
    private BigDecimal totalAmount;
}
