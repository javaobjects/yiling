package com.yiling.hmc.third.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 保单状态同步对象
 * @Description
 * @Author fan.shen
 * @Date 2022/3/30
 */
@NoArgsConstructor
@Data
public class PolicyStatusAsync {

    /**
     * 01
     */
    @JsonProperty("service_id")
    private String serviceId;

    /**
     * 签名
     */
    @JsonProperty("sign")
    private String sign;

    /**
     * 随机数
     */
    @JsonProperty("serial_no")
    private String serialNo;

    /**
     * 保单失效详情
     */
    @JsonProperty("apply_content")
    private ApplyContentDTO applyContent;

    @NoArgsConstructor
    @Data
    @JsonPropertyOrder(alphabetic = true)
    public static class ApplyContentDTO {

        /**
         * 保单号
         */
        @JsonProperty("policy_no")
        private String policyNo;

        /**
         * 分期数量
         */
        @JsonProperty("installments_total")
        private String installmentsTotal;

        /**
         * 保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效
         */
        @JsonProperty("end_policy_type")
        private Integer endPolicyType;

        @JsonProperty("endor_no")
        private String endorNo;

        /**
         * 退保时间 yyyy-MM-dd HH:mm:ss
         */
        @JsonProperty("ret_endtime")
        private String retEndTime;

        /**
         * 退费金额 单位：分
         */
        @JsonProperty("ret_money")
        private Long retMoney;

        /**
         * 承保保费 单位：分
         */
        @JsonProperty("premium")
        private Integer premium;

        /**
         * 身份证号
         */
        @JsonProperty("id_no")
        private String idNo;

        /**
         * 退保时间 yyyy-MM-dd HH:mm:ss
         */
        @JsonProperty("ret_time")
        private Date retTime;

        /**
         * 分期详情
         */
        @JsonProperty("installments_detail")
        private List<InstallmentsDetailDTO> installmentsDetail;

        /**
         * 01 - 身份证
         */
        @JsonProperty("id_type")
        private String idType;

        /**
         * 月缴，第几期数量
         */
        @JsonProperty("installments_num")
        private String installmentsNum;

        /**
         * 定额方案
         */
        @JsonProperty("flowid")
        private String flowid;


        @NoArgsConstructor
        @Data
        public static class InstallmentsDetailDTO {

            /**
             * 期次
             */
            @JsonProperty("num")
            private String num;

            /**
             * 本期退款金额 单位：分
             */
            @JsonProperty("refund_amount")
            private String refundAmount;

        }
    }
}
