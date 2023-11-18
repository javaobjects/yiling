package com.yiling.hmc.wechat.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 保单交费记录DTO
 * </p>
 *
 * @author fan.shen
 * @date 2022/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceRecordPayDTO extends BaseDTO {

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
    private Long amount;

    /**
     * 签约编号 月交的方案，会有值
     */
    private String signPkSubId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付状态 1-支付成功；2-支付失败
     */
    private Integer payStatus;

    /**
     * 安全验证
     */
    private String token;

    /**
     * 最小期次
     */
    private String minSequence;

    /**
     * 最大期次
     */
    private String maxSequence;

    /**
     * 缴费期次列表
     */
    private String premiumPlanList;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
