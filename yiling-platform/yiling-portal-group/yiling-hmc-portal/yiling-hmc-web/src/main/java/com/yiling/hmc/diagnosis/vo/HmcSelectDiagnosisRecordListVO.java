package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 问诊列表VO
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcSelectDiagnosisRecordListVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty(value = "医生姓名")
    private String name;

    @ApiModelProperty(value = "科室名称")
    private String departmentName;

    @ApiModelProperty(value = "问诊类型，0图文1音频2视频 ")
    private Integer type;

    @ApiModelProperty(value = "状态 4待问诊 5问诊中 7,14,16已退费 8,13,15已取消，退费中 9已取消 11已完成 12已回绝 ")
    private Integer status;

    @ApiModelProperty(value = "主诉症状")
    private String diseaseDescribe;

    @ApiModelProperty(value = "就诊患者id")
    private Integer patientId;

    @ApiModelProperty(value = "就诊患者姓名")
    private String patientName;

    @ApiModelProperty(value = "就诊患者性别 0女 1男")
    private Integer patientGender;

    @ApiModelProperty(value = "就诊患者年龄")
    private Integer patientAge;

    @ApiModelProperty(value = "就诊时间")
    private Date diagnosisRecordStartTime;

    @ApiModelProperty(value = "下单时间")
    private Date diagnosisRecordCreateTime;

    @ApiModelProperty(value = "问诊费 单位分")
    private Integer diagnosisRecordPrice;

    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    @ApiModelProperty(value = "处方id 问诊中和历史问诊返回")
    private Integer prescriptionId;

    @ApiModelProperty(value = "评价id 问诊中和历史问诊返回")
    private Integer userEvaluationId;

    @ApiModelProperty(value = "会话id")
    private String conversationID;

}
