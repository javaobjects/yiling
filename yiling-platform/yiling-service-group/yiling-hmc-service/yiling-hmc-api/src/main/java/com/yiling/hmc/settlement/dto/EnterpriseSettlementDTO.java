package com.yiling.hmc.settlement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商家结账表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSettlementDTO extends BaseDTO {

    /**
     * 药品服务终端id
     */
    private Long eid;

    /**
     * 药品服务终端名称
     */
    private String ename;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单明细id
     */
    private Long detailId;

    /**
     * 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
     */
    private Integer terminalSettleStatus;

    /**
     * 对账执行时间
     */
    private Date executionTime;

    /**
     * 结算完成时间
     */
    private Date settleTime;

    /**
     * 合计金额
     */
    private BigDecimal goodsAmount;

    /**
     * 结账金额
     */
    private BigDecimal settleAmount;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
