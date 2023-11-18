package com.yiling.hmc.third.form;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.hmc.wechat.enums.InsuranceNotifyErrorCode;
import com.yiling.marketing.common.enums.PromotionErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 出单信息回传对象
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/28
 */
@NoArgsConstructor
@Data
@Slf4j
public class InsuranceNotify {

    @JsonProperty("requestTime")
    private String requestTime;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("requestData")
    private RequestDataDTO requestData;

    @NoArgsConstructor
    @Data
    public static class RequestDataDTO {

        /**
         * 投保人姓名
         */
        @JsonProperty("holderName")
        private String holderName;

        /**
         * 被保人名称
         */
        @JsonProperty("issueName")
        private String issueName;

        /**
         * 定额方案名称
         */
        @JsonProperty("comboName")
        private String comboName;

        /**
         * 出单时间/支付时间
         */
        @JsonProperty("issueTime")
        private Date issueTime;

        /**
         * 保单生效时间
         */
        @JsonProperty("effectiveTime")
        private Date effectiveTime;

        /**
         * 投保人联系方式
         */
        @JsonProperty("holderPhone")
        private String holderPhone;

        /**
         * 保单号
         */
        @JsonProperty("policyNo")
        private String policyNo;

        /**
         * 首期应收保费(年缴不传)
         */
        @JsonProperty("firstPeriod")
        private Long firstPeriod;

        /**
         * 投保人证件类型 01-居民身份证 02-护照 03-军人证 04-驾驶证 05-港澳台同胞证 99-其他
         */
        @JsonProperty("holderCredentialType")
        private String holderCredentialType;

        /**
         * 保单终止时间
         */
        @JsonProperty("expiredTime")
        private Date expiredTime;

        /**
         * 投保单号
         */
        @JsonProperty("proposalNo")
        private String proposalNo;

        /**
         * 电子保单下载地址
         */
        @JsonProperty("policyUrl")
        private String policyUrl;

        /**
         * 应收保费
         */
        @JsonProperty("premium")
        private Long premium;

        /**
         * 被保人证件类型
         */
        @JsonProperty("issueCredentialType")
        private String issueCredentialType;

        /**
         * 投保时间
         */
        @JsonProperty("proposalTime")
        private Date proposalTime;

        /**
         * 被保人证件号
         */
        @JsonProperty("issueCredentialNo")
        private String issueCredentialNo;

        /**
         * 被保人联系方式
         */
        @JsonProperty("issuePhone")
        private String issuePhone;

        /**
         * 投保人证件号
         */
        @JsonProperty("holderCredentialNo")
        private String holderCredentialNo;

        /**
         * 投被保人关系 1：本人 2：配偶 3：子女 4：父母 9：其他
         */
        @JsonProperty("relationType")
        private Integer relationType;

        /**
         * 动态参数
         */
        @JsonProperty("parameterMap")
        private ParameterMapDTO parameterMap;

        /**
         * 动态参数map
         */
        @NoArgsConstructor
        @Data
        public static class ParameterMapDTO {

            /**
             * 参保lUrl
             */
            @JsonProperty("proposalUrl")
            private String proposalUrl;

            /**
             * 保险id
             */
            @JsonProperty("insuranceId")
            private Long insuranceId;

            /**
             * C端用户id
             */
            @JsonProperty("userId")
            private Long userId;

            /**
             * 销售所属企业id
             */
            @JsonProperty("sellerEid")
            private Long sellerEid;

            /**
             * 销售或店员id
             */
            @JsonProperty("sellerUserId")
            private Long sellerUserId;

            /**
             * 店铺id
             */
            @JsonProperty("eId")
            private Long eId;

            /**
             * C端goodsId
             */
            @JsonProperty("hmcGoodsId")
            private Long hmcGoodsId;

            /**
             * C端保司id
             */
            @JsonProperty("insuranceCompanyId")
            private Long insuranceCompanyId;

        }

        /**
         * 缴费计划
         */
        @JsonProperty("premiumPlanList")
        private List<PremiumPlanListDTO> premiumPlanList;

        @NoArgsConstructor
        @Data
        public static class PremiumPlanListDTO {

            /**
             * 费用
             */
            @JsonProperty("premium")
            private Long premium;

            /**
             * 保单号
             */
            @JsonProperty("policyNo")
            private String policyNo;

            /**
             * 开始时间
             */
            @JsonProperty("startTime")
            private Date startTime;

            /**
             * 结束时间
             */
            @JsonProperty("endTime")
            private Date endTime;

            /**
             * 期次
             */
            @JsonProperty("paySequence")
            private Integer paySequence;
        }
    }

    /**
     * 参数校验
     */
    public void check() {

        if (Objects.isNull(this.requestData.parameterMap)) {
            log.error("动态参数map为空");
            throw new BusinessException(InsuranceNotifyErrorCode.PARAM_INSURANCE_MAP_NULL);
        }

        log.info("动态参数map：{}", this.requestData.parameterMap);

        if (Objects.isNull(this.requestData.parameterMap.proposalUrl)) {
            log.error("proposalUrl为空");
            return;
        }


        String param = StrUtil.subAfter(this.requestData.parameterMap.proposalUrl, "?", true);
        Map<String, String> paramMap = YlStrUtils.parseParam(param);

        if (CollUtil.isEmpty(paramMap)) {
            log.error("动态参数map为空");
            return;
        }

        if (!paramMap.containsKey("insuranceId")) {
            log.error("保险id为空");
            throw new BusinessException(InsuranceNotifyErrorCode.PARAM_INSURANCE_ID_NULL);
        }

        this.requestData.parameterMap.insuranceId = Long.valueOf(paramMap.get("insuranceId"));

        if (!paramMap.containsKey("userId")) {
            log.error("userId为空");
            throw new BusinessException(InsuranceNotifyErrorCode.PARAM_USER_ID_NULL);
        }

        this.requestData.parameterMap.userId = Long.valueOf(paramMap.get("userId"));

        if (!paramMap.containsKey("eId")) {
            log.error("eId为空");
            throw new BusinessException(InsuranceNotifyErrorCode.PARAM_E_ID_NULL);
        }

        this.requestData.parameterMap.eId = Long.valueOf(paramMap.get("eId"));

        if (!paramMap.containsKey("insuranceCompanyId")) {
            log.error("insuranceCompanyId为空");
            throw new BusinessException(InsuranceNotifyErrorCode.PARAM_INSURANCE_COMPANY_ID_NULL);
        }

        if (paramMap.containsKey("sellerEid")) {
            this.requestData.parameterMap.sellerEid = Long.valueOf(paramMap.get("sellerEid"));
        }

        if (paramMap.containsKey("sellerUserId")) {
            this.requestData.parameterMap.sellerUserId = Long.valueOf(paramMap.get("sellerUserId"));
        }

        if (paramMap.containsKey("hmcGoodsId")) {
            this.requestData.parameterMap.hmcGoodsId = Long.valueOf(paramMap.get("hmcGoodsId"));
        }

        if (paramMap.containsKey("insuranceCompanyId")) {
            this.requestData.parameterMap.insuranceCompanyId = Long.valueOf(paramMap.get("insuranceCompanyId"));
        }

    }
}
