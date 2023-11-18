package com.yiling.mall.order.bo;

import java.util.List;

import com.yiling.order.order.dto.request.CreateOrderRequest;

import lombok.Builder;
import lombok.Data;

/** 拆单返回结果集
 * @author zhigang.guo
 * @date: 2022/4/26
 */
@Data
@Builder
public class SplitOrderResultBO {

    /**
     * 创建订单信息
     */
    private List<CreateOrderRequest> createOrderRequestList;
}
