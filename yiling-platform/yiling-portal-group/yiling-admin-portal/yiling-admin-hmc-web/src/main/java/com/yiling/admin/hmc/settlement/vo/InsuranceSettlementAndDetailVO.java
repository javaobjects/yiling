package com.yiling.admin.hmc.settlement.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/5/13
 */
@Data
public class InsuranceSettlementAndDetailVO extends InsuranceSettlementVO {

    @ApiModelProperty("保司结账明细")
    private List<InsuranceSettlementDetailVO> insuranceSettlementDetailList;

}
