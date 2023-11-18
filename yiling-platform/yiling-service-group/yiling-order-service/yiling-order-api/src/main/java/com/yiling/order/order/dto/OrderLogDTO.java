package com.yiling.order.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderLogDTO extends BaseDTO {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 日志内容
     */
    private String logContent;

    /**
     * 日志时间
     */
    private Date logTime;
}

