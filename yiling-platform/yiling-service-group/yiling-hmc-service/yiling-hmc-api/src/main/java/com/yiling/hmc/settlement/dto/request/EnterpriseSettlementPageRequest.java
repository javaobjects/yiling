package com.yiling.hmc.settlement.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSettlementPageRequest extends QueryPageListRequest {

    /**
     * 仅选中导出需要的查询
     */
    private List<Long> idList;

    /**
     * 药品服务终端id
     */
    private Long eid;

    /**
     * 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
     */
    private Integer terminalSettleStatus;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 创建开始时间
     */
    private Date startTime;

    /**
     * 创建截止时间
     */
    private Date stopTime;

    /**
     * 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
     */
    private Integer insuranceSettlementStatus;

    /**
     * 结算起始时间
     */
    private Date payStartTime;

    /**
     * 结算结束时间
     */
    private Date payStopTime;
}
