package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportFlowPopPurchaseVO extends BaseVO {

    /**
     * 销售时间
     */
    @ApiModelProperty(value = "时间")
    private Date poTime;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "erp销售数量")
    private Long erpQuantity;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "erp销售金额")
    private BigDecimal erpTotalAmount;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "pop销售数量")
    private Long poQuantity;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "pop销售金额")
    private BigDecimal poTotalAmount;
}
