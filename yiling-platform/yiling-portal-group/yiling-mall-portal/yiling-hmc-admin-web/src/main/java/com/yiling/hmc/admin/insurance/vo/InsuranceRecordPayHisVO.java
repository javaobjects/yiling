package com.yiling.hmc.admin.insurance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保单交费明细VO
 * @author fan.shen
 * @date 2022/4/21
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceRecordPayHisVO extends BaseVO {


    /**
     * 参保记录id
     */
    private Long insuranceRecordId;

    /**
     * 交易单号
     */
    private String orderNo;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 支付方式
     */
    private String payWayId;

    /**
     * 泰康支付订单号
     */
    private String billNo;

    /**
     * 支付流水号
     */
    private String transactionId;

    /**
     * 主动请求支付编号
     */
    private String activePayId;

    /**
     * 实收金额
     */
    private BigDecimal amount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付状态 1-支付成功；2-支付失败
     */
    private Integer payStatus;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
