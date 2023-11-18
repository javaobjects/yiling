package com.yiling.hmc.settlement.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/27
 */
@Data
public class EnterpriseSettlementPageResultBO implements Serializable {

    /**
     * 终端结算账单列表
     */
    private Page<EnterpriseSettlementPageBO> page;

    /**
     * 总比数
     */
    private Long totalCount;

    /**
     * 合计结算金额
     */
    private BigDecimal totalAmount;
}
