package com.yiling.admin.hmc.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/5/13
 */
@Data
public class InsuranceSettlementPageVO extends BaseVO {

    @ApiModelProperty("保险提供服务商id")
    private Long insuranceCompanyId;

    @ApiModelProperty("服务商名称")
    private String insuranceCompanyName;

    @ApiModelProperty("交易打款时间")
    private Date payTime;

    @ApiModelProperty("对应第三方打款流水号")
    private String thirdPayNo;

    @ApiModelProperty("数据创建时间")
    private Date createTime;

    @ApiModelProperty("打款金额")
    private BigDecimal payAmount;

    @ApiModelProperty("对应保单ID")
    private Long insuranceRecordId;

    @ApiModelProperty("对应保单号")
    private String policyNo;
}
