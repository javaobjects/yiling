package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 创建续费记录
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveInsuranceRecordPayRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;


    /**
     * 参保记录id
     */
    private Long insuranceRecordId;

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
     * 支付状态：1-支付成功；2-支付失败
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
