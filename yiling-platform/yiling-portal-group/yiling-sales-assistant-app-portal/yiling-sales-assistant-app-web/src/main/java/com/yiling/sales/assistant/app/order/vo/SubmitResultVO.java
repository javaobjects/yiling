package com.yiling.sales.assistant.app.order.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.b2b.app.order.vo
 * @date: 2021/10/26
 */
@Data
@Builder
@Accessors(chain = true)
public class SubmitResultVO implements Serializable {

    @ApiModelProperty(value = "订单号", required = true)
    private List<String> orderCodeList;

    @ApiModelProperty(value = "客户Eid", required = true)
    private Long customerEid;

    @ApiModelProperty(value = "客户名称", required = true)
    private String customerName;

}
