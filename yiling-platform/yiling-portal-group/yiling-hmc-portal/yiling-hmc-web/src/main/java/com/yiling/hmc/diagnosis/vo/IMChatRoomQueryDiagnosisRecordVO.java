package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 通过聊天室id查询-> 当前用户和问诊医生之间有没有进行中的问诊单、处方开方状态
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "诊后评价对象")
public class IMChatRoomQueryDiagnosisRecordVO {

    private static final long serialVersionUID = -333710312121608L;

    @ApiModelProperty(value = "状态 1-有未开始的问诊单，这个时候有距离开始时间；2-有进行中的问诊单，这个时候有距离结束时间；3-没有进行中或者未开始的问诊单")
    private Integer status;

    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    @ApiModelProperty(value = "问诊开始时间")
    private Date diagnosisRecordStartDate;

    @ApiModelProperty(value = "问诊结束时间")
    private Date diagnosisRecordEndDate;

    @ApiModelProperty("处方id")
    private Integer prescriptionId;

    @ApiModelProperty(value = "最新处方失效时间")
    private Date prescriptionExpireDate;

    @ApiModelProperty(value = "问诊单类型 0图文 1音频 2视频 3医生赠送0元图文问诊单 4急速问诊 5快速开方")
    private Integer diagnosisRecordType;

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "患者id")
    private Integer patientId;

}
