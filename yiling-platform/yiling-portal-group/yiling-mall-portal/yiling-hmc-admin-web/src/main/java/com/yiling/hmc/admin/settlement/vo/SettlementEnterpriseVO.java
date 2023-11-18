package com.yiling.hmc.admin.settlement.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 药品结算账单结算金额统计
 *
 * @author: yong.zhang
 * @date: 2022/6/29
 */
@Data
public class SettlementEnterpriseVO extends BaseVO {

    @ApiModelProperty("商家id")
    private Long eid;

    @ApiModelProperty("待结账总额")
    private BigDecimal unPayAmount;

    @ApiModelProperty("累积打款总额")
    private BigDecimal enPayAmount;
}
