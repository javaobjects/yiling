package com.yiling.mall.order.bo;

import java.io.Serializable;
import java.util.List;

import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.order.order.dto.OrderDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 订单提交
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.bo
 * @date: 2021/10/29
 */
@Data
@Accessors(chain = true)
public class OrderSubmitBO implements Serializable {

    /**
     * 订单信息
     */
    private List<OrderDTO> orderDTOList;

    /**
     * 订单发送消息集合
     */
    private  List<MqMessageBO> mqMessageBOList;

    /**
     * 在线支付交易ID
     */
    private String payId;

}
