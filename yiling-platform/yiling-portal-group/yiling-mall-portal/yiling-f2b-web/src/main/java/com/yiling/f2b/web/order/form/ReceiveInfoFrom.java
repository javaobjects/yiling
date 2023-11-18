package com.yiling.f2b.web.order.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收货批次明细from
 * @author:wei.wang
 * @date:2021/6/25
 */
@Data
public class ReceiveInfoFrom  implements java.io.Serializable {

    /**
     * 收货信息
     */
    @ApiModelProperty(value = "收货订单id",required = true)
    private Long id;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量",required = true)
    private Integer receiveQuantity;
}
