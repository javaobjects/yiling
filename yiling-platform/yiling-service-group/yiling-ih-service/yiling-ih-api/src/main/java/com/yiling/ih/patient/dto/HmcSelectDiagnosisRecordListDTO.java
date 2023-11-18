package com.yiling.ih.patient.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 问诊列表DTO
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcSelectDiagnosisRecordListDTO implements java.io.Serializable{

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 头像
     */
    private String picture;

    /**
     * 医生姓名
     */
    private String name;

    /**
     * 科室名称
     */
    private String departmentName;

    /**
     * 问诊类型，0图文1音频2视频
     */
    private Integer type;

    /**
     * 状态 4待问诊 5问诊中 7,14,16已退费 8,13,15已取消，退费中 9已取消 11已完成 12已回绝
     */
    private Integer status;

    /**
     * 主诉症状
     */
    private String diseaseDescribe;

    /**
     * 就诊患者id
     */
    private Integer patientId;

    /**
     * 就诊患者姓名
     */
    private String patientName;

    /**
     * 就诊患者性别 0女 1男
     */
    private Integer patientGender;

    /**
     * 就诊患者年龄
     */
    private Integer patientAge;

    /**
     * 就诊时间
     */
    private Date diagnosisRecordStartTime;

    /**
     * 下单时间
     */
    private Date diagnosisRecordCreateTime;

    /**
     * 问诊费 单位分
     */
    private Integer diagnosisRecordPrice;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 处方id 问诊中和历史问诊返回
     */
    private Integer prescriptionId;

    /**
     * 评价id 问诊中和历史问诊返回
     */
    private Integer userEvaluationId;

    /**
     * 会话id
     */
    private String conversationID;

}
