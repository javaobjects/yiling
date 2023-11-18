package com.yiling.settlement.yee.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class YeeSettleSyncRecordDTO extends BaseDTO {

    /**
     * 发起方商编
     */
    private String parentMerchantNo;

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 结算订单号
     */
    private String summaryNo;

    /**
     * 应结金额
     */
    private BigDecimal settleAmount;

    /**
     * 结算手续费
     */
    private BigDecimal realFee;

    /**
     * 结算到账金额
     */
    private BigDecimal realAmount;

    /**
     * 结算订单状态： 1-结算成功 2-失败 3-待处理 4-待出款 5-结算异常 6-银行处理中
     */
    private Integer status;

    /**
     * 结算订单状态描述
     */
    private String statusDesc;

    /**
     * 结算产品
     */
    private String settleType;

    /**
     * 同步时间
     */
    private Date syncTime;

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
}
