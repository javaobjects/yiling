package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 订单处方 Request
 *
 * @author: fan.shen
 * @date: 2022/4/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPrescriptionRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 开方日期
     */
    private Date receiptDate;

    /**
     * 开方医生
     */
    private String doctor;

    /**
     * 处方编号
     */
    private String prescriptionNo;

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
     * 症状描述
     */
    private String symptomsDesc;

    /**
     * 诊断结果
     */
    private String interrogationResult;

    /**
     * 处方快照url
     */
    private String prescriptionSnapshotUrl;

    /**
     * 补充描述
     */
    private String addDesc;

}
