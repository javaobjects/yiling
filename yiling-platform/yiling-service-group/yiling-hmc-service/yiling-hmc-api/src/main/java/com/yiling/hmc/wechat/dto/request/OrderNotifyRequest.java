package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 兑付回调 Request
 *
 * @author: fan.shen
 * @date: 2022/4/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderNotifyRequest extends BaseRequest {

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 开方日期
     */
    private String receiptDate;

    /**
     * 医生
     */
    private String doctor;

    /**
     * 处方编号
     */
    private String prescriptionNo;

    /**
     * 症状描述
     */
    private String symptomsDesc;

    /**
     * 患者姓名
     */
    private String patientsName;

    /**
     * 患者性别
     */
    private String patientsGender;

    /**
     * 患者年龄
     */
    private Integer patientsAge;

    /**
     * 问诊科室
     */
    private String interrogationRoom;

    /**
     * 问诊结果
     */
    private String interrogationResult;

    /**
     * 处方快照url
     */
    private String prescriptionSnapshotUrl;

    /**
     * 结算金额
     */
    private BigDecimal insuranceSettlementAmount;

    /**
     * 药品列表
     */
    private List<MedicineDetail> medicineDetailList;

    @NoArgsConstructor
    @Data
    public static class MedicineDetail extends BaseRequest {

        /**
         * 商品名称
         */
        private String goodsName;

        /**
         * 规格
         */
        private String specifications;

        /**
         * 数量
         */
        private String goodsQuantity;

        /**
         * 用法用量
         */
        private String usageInfo;

        /**
         * 价格
         */
        private Integer goodsPrice;
    }
}
