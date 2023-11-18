package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 处方VO
 * @author: fan.shen
 * @date: 2023/05/09
 */
@Data
public class HmcPrescriptionVO {

    @ApiModelProperty("处方id")
    private Integer prescriptionId;

    @ApiModelProperty(value = "处方状态（0:医生待签名 1:待购买 2:已作废 3:已过有效期 4:已支付(待审核)  5:审方通过 6:审方未通过 ）")
    private Integer status;

    /**
     * 患者名称
     */
    @ApiModelProperty(value = "患者名称")
    private String patientName;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证")
    private String idCard;

    /**
     * 患者年龄
     */
    @ApiModelProperty(value = "患者年龄")
    private Integer patientAge;

    /**
     * 患者性别
     */
    @ApiModelProperty(value = "性别1 : 男  0 : 女")
    private Integer gender;

    @ApiModelProperty(value = "诊断结果")
    private String diagnosticProof;

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;

    @ApiModelProperty(value = "医生职称")
    private String profession;

    @ApiModelProperty(value = "科室名称")
    private String departmentName;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("药品名称")
    private List<String> goodsName;

    @ApiModelProperty("处方订单id")
    private Long orderId;
}