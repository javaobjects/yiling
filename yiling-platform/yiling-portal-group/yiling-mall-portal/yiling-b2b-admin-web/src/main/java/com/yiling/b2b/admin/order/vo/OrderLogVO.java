package com.yiling.b2b.admin.order.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单日志记录
 *
 * @author: wei.wang
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderLogVO extends BaseVO {

    @ApiModelProperty(value = "日志内容")
    private String logContent;

    @ApiModelProperty(value = "日志时间")
    private Date logTime;
}
