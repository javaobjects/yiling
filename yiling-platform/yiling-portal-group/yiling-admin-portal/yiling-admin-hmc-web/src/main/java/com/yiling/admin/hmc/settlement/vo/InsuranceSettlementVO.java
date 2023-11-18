package com.yiling.admin.hmc.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 保司结账表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Data
public class InsuranceSettlementVO extends BaseVO {

    @ApiModelProperty("客户姓名(被保人名称)")
    private String issueName;

    @ApiModelProperty("保险提供服务商id")
    private Long insuranceCompanyId;

    @ApiModelProperty("保险提供服务商名称")
    private String insuranceCompanyName;

    @ApiModelProperty("保险记录id")
    private Long insuranceRecordId;

    @ApiModelProperty("对应保单号")
    private String policyNo;

    @ApiModelProperty("第三方打款单号")
    private String thirdPayNo;

    @ApiModelProperty("保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结")
    private Integer insuranceSettleStatus;

    @ApiModelProperty("收款账号")
    private String accountNo;

    @ApiModelProperty("打款时间")
    private Date payTime;

    @ApiModelProperty("打款金额")
    private BigDecimal payAmount;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;


}
