package com.yiling.hmc.settlement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保司结账明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSettlementDetailDTO extends BaseDTO {

    /**
     * 保司结账主表id
     */
    private Long insuranceSettlementId;

    /**
     * 保险id
     */
    private Long insuranceRecordId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 打款金额
     */
    private BigDecimal payAmount;

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
