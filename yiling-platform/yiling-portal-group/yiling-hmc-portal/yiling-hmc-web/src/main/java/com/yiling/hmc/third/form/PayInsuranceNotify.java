package com.yiling.hmc.third.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 主动支付回传对象
 * @Description
 * @Author fan.shen
 * @Date 2022/3/30
 */
@NoArgsConstructor
@Data
public class PayInsuranceNotify {

    /**
     * 主动请求支付编号
     */
    @JsonProperty("activePayId")
    private String activePayId;

    /**
     * 实收金额
     */
    @JsonProperty("amount")
    private String amount;

    /**
     * 泰康支付订单号
     */
    @JsonProperty("billno")
    private String billno;

    /**
     * 最大期次
     */
    @JsonProperty("maxSequence")
    private String maxSequence;

    /**
     * 最小期次
     */
    @JsonProperty("minSequence")
    private String minSequence;

    /**
     * 支付状态 3支付成功；4支付失败
     */
    @JsonProperty("payStatus")
    private Integer payStatus;

    /**
     * 支付时间
     */
    @JsonProperty("payTime")
    private String payTime;

    /**
     * 支付方式
     */
    @JsonProperty("paywayId")
    private String paywayId;

    /**
     * 保单号
     */
    @JsonProperty("policyNo")
    private String policyNo;

    @JsonProperty("premiumPlanList")
    private List<PremiumPlanListDTO> premiumPlanList;

    /**
     * 安全验证
     */
    @JsonProperty("token")
    private String token;

    /**
     * 支付流水号
     */
    @JsonProperty("transactionId")
    private String transactionId;

    /**
     * 缴费期次列表
     */
    @NoArgsConstructor
    @Data
    public static class PremiumPlanListDTO {
        @JsonProperty("paySequence")
        private Integer paySequence;
        @JsonProperty("premium")
        private Integer premium;
    }
}
