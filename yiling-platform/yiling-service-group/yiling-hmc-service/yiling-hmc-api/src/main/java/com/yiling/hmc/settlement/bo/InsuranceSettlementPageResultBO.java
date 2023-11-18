package com.yiling.hmc.settlement.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/5/13
 */
@Data
public class InsuranceSettlementPageResultBO implements Serializable {

    /**
     * 保司结账明细账单列表
     */
    private Page<InsuranceSettlementPageBO> page;

    /**
     * 总比数
     */
    private Long totalCount;

    /**
     * 合计结算金额
     */
    private BigDecimal totalAmount;
}
