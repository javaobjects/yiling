package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 处方详情VO
 * @author: fan.shen
 * @date: 2023/05/09
 */
@Data
public class HmcPrescriptionDetailVO {

    @ApiModelProperty(value = "处方id")
    private Integer id;

    @ApiModelProperty(value = "处方状态（0:医生待签名 1:待购买 2:已作废 3:已过有效期 4:已支付(待审核)  5:审方通过 6:审方未通过 ）")
    private Integer status;

    @ApiModelProperty(value = "处方url")
    private String url;

    @ApiModelProperty(value = "医生id")
    private Integer docId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;
}