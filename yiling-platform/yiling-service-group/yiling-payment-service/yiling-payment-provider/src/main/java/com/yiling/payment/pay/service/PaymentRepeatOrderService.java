package com.yiling.payment.pay.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.payment.pay.dto.RepeatPayOrderDTO;
import com.yiling.payment.pay.dto.request.RepeatOrderPageRequest;
import com.yiling.payment.pay.entity.PaymentRepeatOrderDO;

/**
 * <p>
 * 订单支付重复表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
public interface PaymentRepeatOrderService extends BaseService<PaymentRepeatOrderDO> {

    /**
     * 通过支付单号，查询支付记录
     * @param payNo 支付单号
     * @return
     */
    List<PaymentRepeatOrderDO> selectRepeatOrderByPayNo(String payNo,String appOrderNo);

    /**
     * 创建支付重复订单
     * @param orderDO
     * @return
     */
    Boolean insertRepeatOrder(PaymentRepeatOrderDO orderDO);

    /**
     * 支付重复分页查询
     * @param request
     * @return
     */
    Page<RepeatPayOrderDTO> selectPageRepeatOrderList(RepeatOrderPageRequest request);

}
