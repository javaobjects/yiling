package com.yiling.admin.hmc.order.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单详情VO
 * </p>
 *
 * @author fan.shen
 * @date 2022/4/12
 */
@Data
public class OrderPrescriptionVO extends BaseVO {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;

    /**
     * 开方日期
     */
    @ApiModelProperty("开方日期")
    private Date receiptDate;

    /**
     * 开方医生
     */
    @ApiModelProperty("开方医生")
    private String doctor;

    /**
     * 处方编号
     */
    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    /**
     * 患者姓名
     */
    @ApiModelProperty("患者姓名")
    private String patientsName;

    /**
     * 患者性别
     */
    @ApiModelProperty("患者性别")
    private String patientsGender;

    /**
     * 患者年龄
     */
    @ApiModelProperty("患者年龄")
    private Integer patientsAge;

    /**
     * 问诊科室
     */
    @ApiModelProperty("问诊科室")
    private String interrogationRoom;

    /**
     * 症状描述
     */
    @ApiModelProperty("症状描述")
    private String symptomsDesc;

    /**
     * 诊断结果
     */
    @ApiModelProperty("诊断结果")
    private String interrogationResult;

    /**
     * 处方快照url
     */
    @ApiModelProperty("处方快照url的key值，逗号隔开")
    private String prescriptionSnapshotUrl;

    @ApiModelProperty("处方快照url")
    private List<String> prescriptionSnapshotUrlList;

    /**
     * 补充描述
     */
    @ApiModelProperty("补充描述")
    private String addDesc;

}
