package com.yiling.admin.hmc.settlement.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/5/13
 */
@Data
public class InsuranceSettlementPageResultVO implements Serializable {

    @ApiModelProperty("保司结账明细账单列表")
    private Page<InsuranceSettlementPageVO> page;

    @ApiModelProperty("总比数")
    private Long totalCount;

    @ApiModelProperty("合计结算金额")
    private BigDecimal totalAmount;
}
