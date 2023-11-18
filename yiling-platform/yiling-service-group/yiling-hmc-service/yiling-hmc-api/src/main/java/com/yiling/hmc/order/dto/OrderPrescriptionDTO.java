package com.yiling.hmc.order.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 订单处方
 *
 * @author fan.shen
 * @date 2022/4/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPrescriptionDTO extends BaseDTO {

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

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;
}
