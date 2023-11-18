package com.yiling.admin.hmc.settlement.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/7
 */
@Data
public class SettlementEnterprisePageVO extends BaseVO {

    @ApiModelProperty("商家id")
    private Long eid;

    @ApiModelProperty("商家名称")
    private String ename;

    @ApiModelProperty("待处理订单数量")
    private Integer unPayCount;

    @ApiModelProperty("待处理订单总额")
    private BigDecimal unPayAmount;

    @ApiModelProperty("已完结订单数量")
    private Integer enPayCount;

    @ApiModelProperty("已完结订单总额")
    private BigDecimal enPayAmount;
}
