package com.yiling.payment.pay.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.pay.dto.RepeatPayOrderDTO;
import com.yiling.payment.pay.dto.request.RepeatOrderPageRequest;
import com.yiling.payment.pay.dto.request.UpdateRepeatOrderRequest;

/** 重复退款
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.api
 * @date: 2021/11/1
 */
public interface RepeatOrderApi {

    /**
     * 分页查询支付重复的订单
     * @param request
     * @return
     */
    Page<RepeatPayOrderDTO> selectPageRepeatOrderList(RepeatOrderPageRequest request);


    /**
     * 处理重复退款逻辑
     * @param updateRepeatOrderRequest
     * @return
     */
    Result<Void> processRepeatOrder(UpdateRepeatOrderRequest updateRepeatOrderRequest);




}
