package com.yiling.hmc.admin.settlement.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 财务中心药品结算账单请求参数
 *
 * @author: yong.zhang
 * @date: 2022/6/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSettlementPageForm extends QueryPageListForm {

    @ApiModelProperty("药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单")
    private Integer terminalSettleStatus;

    @ApiModelProperty("创建开始时间")
    private Date startTime;

    @ApiModelProperty("创建截止时间")
    private Date stopTime;
}
