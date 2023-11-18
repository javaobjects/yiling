package com.yiling.payment.channel.service.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * 易宝账号需求
 * @author zhigang.guo
 * @date: 2022/9/13
 */
@Data
public class TradeContentJsonDTO extends BaseDTO {
    /**
     * 易宝交易商户号码
     */
    private String merchantNo;
}
