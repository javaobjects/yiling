package com.yiling.sales.assistant.message.dto.request;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/1/19
 */
@Data
@Accessors(chain = true)
public class OrderMessageRequest implements Serializable {
    /**
     * 编号：订单-订单编号，客户认证-认证企业编号，发布任务-任务编号
     */
    private String code;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 下单时间
     */
    private Date   createTime;
}
