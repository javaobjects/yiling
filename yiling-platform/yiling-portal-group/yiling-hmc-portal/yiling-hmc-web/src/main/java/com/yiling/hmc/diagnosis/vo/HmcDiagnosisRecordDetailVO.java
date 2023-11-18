package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 问诊订单详情VO
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcDiagnosisRecordDetailVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty(value = "医生姓名")
    private String name;

    @ApiModelProperty(value = "科室名称")
    private String departmentName;

    @ApiModelProperty(value = "职称")
    private String professionName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "问诊类型，0图文1音频2视频 ")
    private Integer type;

    @ApiModelProperty(value = "就诊时间")
    private Date diagnosisRecordCreateTime;

    @ApiModelProperty(value = "开始时间")
    private Date diagnosisRecordStartTime;

    @ApiModelProperty(value = "结束时间")
    private Date diagnosisRecordEndTime;

    @ApiModelProperty(value = "状态 4待问诊 5问诊中 7,14,16已退费 8,13,15已取消，退费中 9已取消 11已完成 12已回绝 ")
    private Integer status;

    @ApiModelProperty(value = "就诊患者id")
    private Integer patientId;

    @ApiModelProperty(value = "就诊患者姓名")
    private String patientName;

    @ApiModelProperty(value = "就诊患者手机号")
    private String patientMobile;

    @ApiModelProperty(value = "就诊患者性别 0女 1男")
    private Integer patientGender;

    @ApiModelProperty(value = "就诊患者年龄")
    private Integer patientAge;

    @ApiModelProperty(value = "就诊患者过往病史")
    private String patientHistoryDisease;

    @ApiModelProperty(value = "就诊患者家族病史")
    private String patientFamilyDisease;

    @ApiModelProperty(value = "就诊患者家过敏史")
    private String patientAllergyHistory;

    @ApiModelProperty(value = "主诉症状集合")
    private List<HmcDiagnosisRecordDetailDescribeVO> diseaseDescribePicture;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "下单时间")
    private Date orderNoCreateTime;

    @ApiModelProperty(value = "支付方式")
    private String orderNoPayType;

    @ApiModelProperty(value = "支付时间")
    private Date orderNoPayTime;

    @ApiModelProperty(value = "应付金额 单位分")
    private Integer totalPrice;

    @ApiModelProperty(value = "优惠金额 单位分")
    private Integer couponPrice;

    @ApiModelProperty(value = "实付金额 单位分")
    private Integer realityPayPrice;

    @ApiModelProperty(value = "退款金额 单位分")
    private Integer refundPrice;


    @ApiModelProperty(value = "处方id 问诊中和历史问诊返回")
    private Integer prescriptionId;

    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    @ApiModelProperty(value = "评价id 问诊中和历史问诊返回")
    private Integer userEvaluationId;

    /**
     * 会话id
     */
    @ApiModelProperty(value = "会话id")
    private String conversationID;

    /**
     * 医生是否回复 0未回复 1已回复
     */
    @ApiModelProperty(value = "医生是否回复 0未回复 1已回复")
    private Integer doctorReplyFlag;

}
