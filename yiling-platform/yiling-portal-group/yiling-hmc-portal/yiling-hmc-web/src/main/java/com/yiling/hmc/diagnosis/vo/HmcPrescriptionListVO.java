package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 问诊单关联处方列表
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcPrescriptionListVO {

    @ApiModelProperty("处方列表")
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
