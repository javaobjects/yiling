package com.yiling.b2b.app.order.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnLogQueryVO {
    @ApiModelProperty(value = "实退金额", required = true)
    private String logContent;

    @ApiModelProperty(value = "实退金额", required = true)
    private Date logTime;
}
