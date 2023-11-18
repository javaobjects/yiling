package com.yiling.hmc.order.context;

import com.google.common.collect.Lists;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.hmc.order.dto.OrderDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 创建订单上下文
 * @author: fan.shen
 * @date: 2022/4/25
 */
@Data
@Accessors(chain = true)
public class CreateOrderContext implements Serializable {

    private static final long serialVersionUID = -217101627488116759L;

    /**
     * 订单信息
     */
    private OrderDTO orderDTO;

    /**
     * 创建订单mq
     */
    private List<MqMessageBO> mqMessageList = Lists.newArrayList();


}