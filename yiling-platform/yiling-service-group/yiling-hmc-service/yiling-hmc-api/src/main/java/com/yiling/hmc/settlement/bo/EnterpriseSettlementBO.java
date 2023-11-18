package com.yiling.hmc.settlement.bo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/21
 */
@Data
@Builder
@Accessors(chain = true)
public class EnterpriseSettlementBO implements Serializable {

    /**
     * 1-成功，2-失败
     */
    private Integer type;

    /**
     * 结算订单明细编号
     */
    private Long detailId;

    /**
     * 结果
     */
    private String result;
}
