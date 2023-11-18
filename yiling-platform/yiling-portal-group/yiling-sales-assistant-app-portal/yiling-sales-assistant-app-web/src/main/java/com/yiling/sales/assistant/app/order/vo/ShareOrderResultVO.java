package com.yiling.sales.assistant.app.order.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2022/2/26
 */
@Data
@Builder
@Accessors(chain = true)
public class ShareOrderResultVO implements Serializable {

    @ApiModelProperty(value = "分享订单加密字符串", required = true)
    private String keyStr;

    @ApiModelProperty(value = "买家EID", required = true)
    private Long buyerEid;

    @ApiModelProperty(value = "买家名称", required = true)
    private String buyerName;
}
