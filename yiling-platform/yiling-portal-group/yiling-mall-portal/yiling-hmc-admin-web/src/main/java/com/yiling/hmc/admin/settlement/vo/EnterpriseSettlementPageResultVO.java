package com.yiling.hmc.admin.settlement.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 终端结算账单列表返回数据
 *
 * @author: yong.zhang
 * @date: 2022/6/29
 */
@Data
public class EnterpriseSettlementPageResultVO implements Serializable {

    @ApiModelProperty("终端结算账单列表")
    private Page<EnterpriseSettlementPageVO> page;

    @ApiModelProperty("总比数")
    private Long totalCount;

    @ApiModelProperty("合计结算金额")
    private BigDecimal totalAmount;
}
