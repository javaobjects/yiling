package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HMC创建处方订单VO
 *
 * @author: fan.shen
 * @data: 2023/05/11
 */
@Data
public class HmcCreatePrescriptionOrderVO {

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    @ApiModelProperty("订单编号")
    private String prescriptionOrderNo;
}
