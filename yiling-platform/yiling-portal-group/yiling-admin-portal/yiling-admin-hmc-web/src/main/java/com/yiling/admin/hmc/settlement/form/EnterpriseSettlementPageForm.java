package com.yiling.admin.hmc.settlement.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSettlementPageForm extends QueryPageListForm {

    @ApiModelProperty("药品服务终端id")
    @NotNull(message = "药品服务终端没有选中")
    private Long eid;

    @ApiModelProperty("药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单")
    private Integer terminalSettleStatus;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("创建开始时间")
    private Date startTime;

    @ApiModelProperty("创建截止时间")
    private Date stopTime;

    @ApiModelProperty("保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结")
    private Integer insuranceSettlementStatus;

    @ApiModelProperty("结算起始时间")
    private Date payStartTime;

    @ApiModelProperty("结算结束时间")
    private Date payStopTime;
}
