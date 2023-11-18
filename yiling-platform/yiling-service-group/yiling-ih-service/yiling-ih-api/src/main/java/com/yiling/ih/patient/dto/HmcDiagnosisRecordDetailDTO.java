package com.yiling.ih.patient.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 问诊订单详情DTO
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcDiagnosisRecordDetailDTO implements java.io.Serializable {

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 	hmc用户ID
     */
    private Integer hmcUserId;

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
     * 职称
     */
    private String professionName;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 问诊类型，0图文1音频2视频
     */
    private Integer type;

    /**
     * 就诊时间
     */
    private Date diagnosisRecordCreateTime;

    /**
     * 开始时间
     */
    private Date diagnosisRecordStartTime;

    /**
     * 结束时间
     */
    private Date diagnosisRecordEndTime;

    /**
     * 状态 4待问诊 5问诊中 7,14,16已退费 8,13,15已取消，退费中 9已取消 11已完成 12已回绝
     */
    private Integer status;

    /**
     * 就诊患者id
     */
    private Integer patientId;

    /**
     * 就诊患者姓名
     */
    private String patientName;

    /**
     * 就诊患者手机号
     */
    private String patientMobile;

    /**
     * 就诊患者性别 0女 1男
     */
    private Integer patientGender;

    /**
     * 就诊患者年龄
     */
    private Integer patientAge;

    /**
     * 就诊患者过往病史
     */
    private String patientHistoryDisease;

    /**
     * 就诊患者家族病史
     */
    private String patientFamilyDisease;

    /**
     * 就诊患者家过敏史
     */
    private String patientAllergyHistory;

    /**
     * 主诉症状集合
     */
    private List<HmcDiagnosisRecordDetailDescribeDTO> diseaseDescribePicture;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单时间
     */
    private Date orderNoCreateTime;

    /**
     * 支付方式
     */
    private String orderNoPayType;

    /**
     * 支付时间
     */
    private Date orderNoPayTime;

    /**
     * 应付金额 单位分
     */
    private Integer totalPrice;

    /**
     * 优惠金额 单位分
     */
    private Integer couponPrice;

    /**
     * 实付金额 单位分
     */
    private Integer realityPayPrice;

    /**
     * 退款金额 单位分
     */
    private Integer refundPrice;

    /**
     * 处方id 问诊中和历史问诊返回
     */
    private Integer prescriptionId;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 评价id 问诊中和历史问诊返回
     */
    private Integer userEvaluationId;

    /**
     * 会话id
     */
    private String conversationID;

    /**
     * 医生是否回复 0未回复 1已回复
     */
    private Integer doctorReplyFlag;

}
