package com.yiling.ih.patient.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * IM 聊天室查询问诊单
 *
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcIMChatRootQueryDiagnosisRecordDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    // @ApiModelProperty(value = "状态 1-有未开始的问诊单，这个时候有距离开始时间；2-有进行中的问诊单，这个时候有距离结束时间；3-没有进行中或者未开始的问诊单")
    private Integer status;

    // @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    // @ApiModelProperty(value = "问诊开始时间")
    private Date diagnosisRecordStartDate;

    // @ApiModelProperty(value = "问诊结束时间")
    private Date diagnosisRecordEndDate;

    // @ApiModelProperty("处方id")
    private Integer prescriptionId;

    // @ApiModelProperty(value = "最新处方失效时间")
    private Date prescriptionExpireDate;

    // 问诊单类型 0图文 1音频 2视频 3医生赠送0元图文问诊单 4急速问诊 5快速开方
    private Integer diagnosisRecordType;

    // @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    // @ApiModelProperty(value = "患者id")
    private Integer patientId;

}
