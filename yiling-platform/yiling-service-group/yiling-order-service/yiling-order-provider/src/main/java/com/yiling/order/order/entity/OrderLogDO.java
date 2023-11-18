package com.yiling.order.order.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单日志
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_log")
public class OrderLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 日志类型
     */
    private Integer logType;


    /**
     * 日志内容
     */
    private String logContent;

    /**
     * 日志时间
     */
    private Date logTime;


}
