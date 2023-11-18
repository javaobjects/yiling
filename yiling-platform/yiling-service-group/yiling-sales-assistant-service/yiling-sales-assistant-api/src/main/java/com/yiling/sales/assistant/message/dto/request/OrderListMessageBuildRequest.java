package com.yiling.sales.assistant.message.dto.request;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/1/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderListMessageBuildRequest extends MessageBuildRequest {
    /**
     * 订单消息
     */
    private List<OrderMessageRequest> orderMessageRequestList;
}
