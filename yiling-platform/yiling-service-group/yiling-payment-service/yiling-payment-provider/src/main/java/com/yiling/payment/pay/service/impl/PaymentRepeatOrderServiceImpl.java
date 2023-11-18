package com.yiling.payment.pay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.pay.dao.PaymentRepeatOrderMapper;
import com.yiling.payment.pay.dto.RepeatPayOrderDTO;
import com.yiling.payment.pay.dto.request.RepeatOrderPageRequest;
import com.yiling.payment.pay.entity.PaymentRepeatOrderDO;
import com.yiling.payment.pay.service.PaymentRepeatOrderService;
import com.yiling.payment.pay.util.NoUtil;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 订单支付重复表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Service
public class PaymentRepeatOrderServiceImpl extends BaseServiceImpl<PaymentRepeatOrderMapper, PaymentRepeatOrderDO> implements PaymentRepeatOrderService {
    @Autowired
    private NoUtil noUtil;

    @Override
    public List<PaymentRepeatOrderDO> selectRepeatOrderByPayNo(String payNo,String appOrderNo) {

        QueryWrapper<PaymentRepeatOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentRepeatOrderDO::getPayNo,payNo);
        wrapper.lambda().eq(PaymentRepeatOrderDO::getAppOrderNo,appOrderNo);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public Boolean insertRepeatOrder(PaymentRepeatOrderDO orderDO) {

        String refundNo = noUtil.buildRefundNo();

        orderDO.setRefundNo(refundNo);
        orderDO.setRefundState(RefundStateEnum.WAIT_REFUND.getCode());
        Boolean result = this.save(orderDO);
        return result;
    }

    @Override
    public Page<RepeatPayOrderDTO> selectPageRepeatOrderList(RepeatOrderPageRequest request) {

        // 支付方式，过滤0
        if ("0".equals(request.getPayWay())) {

            request.setPayWay(null);
        }
        if (request.getStartPayTime() != null) {
            request.setStartPayTime(DateUtil.beginOfDay(request.getStartPayTime()));
        }
        if (request.getEndPayTime() != null) {
            request.setEndPayTime(DateUtil.endOfDay(request.getEndPayTime()));
        }
        return this.baseMapper.selectPageRepeatOrderList(request.getPage(),request);
    }
}
