package com.yiling.sales.assistant.app.order.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 去支付结果
 * @author zhigang.guo
 * @date: 2022/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPayVO extends BaseVO {
    /**
     * 支付Id
     */
    @ApiModelProperty(value = "支付Id")
    private String payId;

}
