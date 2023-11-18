package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HMC创建问诊订单VO
 *
 * @author: fan.shen
 * @data: 2023/05/12
 */
@Data
public class HmcSubmitDiagnosisOrderVO {

    @ApiModelProperty(value = "状态 1有正在进行的问诊单,diagnosisRecordId是正在进行的问诊单id 2所选号段已无号源的校验 3正常下单")
    private Integer status;

    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    @ApiModelProperty("支付票据")
    private String payTicket;

    @ApiModelProperty("会话id")
    private String conversationID;

    private Integer docId;

    private String docName;
}
