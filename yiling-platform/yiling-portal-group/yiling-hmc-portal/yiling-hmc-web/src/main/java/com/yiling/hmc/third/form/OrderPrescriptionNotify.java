package com.yiling.hmc.third.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderPrescriptionNotify {

    /**
     * 保单号
     */
    @JsonProperty("policy_no")
    private String policyNo;

    /**
     * 订单号
     */
    @JsonProperty("order_no")
    private String orderNo;

    /**
     * 开方日期
     */
    @JsonProperty("receipt_date")
    private String receiptDate;

    /**
     * 医生
     */
    @JsonProperty("doctor")
    private String doctor;

    /**
     * 处方编号
     */
    @JsonProperty("prescription_no")
    private String prescriptionNo;

    /**
     * 症状描述
     */
    @JsonProperty("symptoms_desc")
    private String symptomsDesc;

    /**
     * 患者姓名
     */
    @JsonProperty("patients_name")
    private String patientsName;

    /**
     * 患者性别
     */
    @JsonProperty("patients_gender")
    private String patientsGender;

    /**
     * 患者年龄
     */
    @JsonProperty("patients_age")
    private Integer patientsAge;

    /**
     * 问诊科室
     */
    @JsonProperty("interrogation_room")
    private String interrogationRoom;

    /**
     * 问诊结果
     */
    @JsonProperty("interrogation_result")
    private String interrogationResult;

    /**
     * 处方快照url
     */
    @JsonProperty("prescription_snapshot_url")
    private String prescriptionSnapshotUrl;

    /**
     * 结算金额
     */
    @JsonProperty("insurance_settlement_amount")
    private BigDecimal insuranceSettlementAmount;

    /**
     * 药品列表
     */
    @JsonProperty("medicineList")
    private List<MedicineListDTO> medicineList;

    @NoArgsConstructor
    @Data
    public static class MedicineListDTO {

        /**
         * 商品名称
         */
        @JsonProperty("goods_name")
        private String goodsName;

        /**
         * 规格
         */
        @JsonProperty("specifications")
        private String specifications;

        /**
         * 数量
         */
        @JsonProperty("goods_quantity")
        private String goodsQuantity;

        /**
         * 用法用量
         */
        @JsonProperty("usage_info")
        private String usageInfo;

        /**
         * 价格
         */
        @JsonProperty("goods_price")
        private Integer goodsPrice;
    }
}
