package com.yiling.ih.patient.dto;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


/**
 * 我的处方 DTO
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcMyPrescriptionDTO implements java.io.Serializable {

    /**
     * 处方id
     */
    private Integer prescriptionId;

    /**
     * 处方状态（0:医生待签名 1:待购买 2:已作废 3:已过有效期 4:已支付(待审核)  5:审方通过 6:审方未通过 ）
     */
    private Integer status;

    /**
     * 患者名称
     */
    private String patientName;


    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 患者性别 男  0 : 女
     */
    private Integer gender;

    /**
     * 诊断结果
     */
    private String diagnosticProof;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医生职称
     */
    private String profession;

    /**
     * 科室名称
     */
    private String departmentName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 药品名称
     */
    private List<String> goodsName;

    /**
     * 处方订单id
     */
    private Integer orderId;

}
