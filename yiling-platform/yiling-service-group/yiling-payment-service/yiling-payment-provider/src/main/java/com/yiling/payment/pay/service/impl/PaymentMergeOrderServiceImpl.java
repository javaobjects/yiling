package com.yiling.payment.pay.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.pay.dao.PaymentMergeOrderMapper;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.service.PaymentMergeOrderService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 合并支付订单表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Service
@Slf4j
public class PaymentMergeOrderServiceImpl extends BaseServiceImpl<PaymentMergeOrderMapper, PaymentMergeOrderDO> implements PaymentMergeOrderService {

    @Override
    public Result<String> insertPaymentMergeOrder(List<PaymentMergeOrderDO> paymentMergeOrderDOS) {

        String payId = IdUtil.fastSimpleUUID();

        paymentMergeOrderDOS.forEach( e -> e.setPayId(payId));

        boolean result =  this.saveOrUpdateBatch(paymentMergeOrderDOS);

        if (result) {

            return Result.success(payId);
        }

        // 生成合并订单数据失败
        return Result.failed("生成合并订单数据失败");
    }

    @Override
    public List<PaymentMergeOrderDO> selectMergerOrderByOrderIdList(String orderPlatform,Integer tradeType,List<Long> appOrderIds){
        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();

        if (appOrderIds.size() > 1) {
            wrapper.lambda().in(PaymentMergeOrderDO::getAppOrderId,appOrderIds);
        } else {
            wrapper.lambda().eq(PaymentMergeOrderDO::getAppOrderId,appOrderIds.get(0));
        }

        List<PaymentMergeOrderDO> resultList =  this.baseMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(resultList)) {

            return Collections.emptyList();
        }

        return resultList.stream()
                .filter(t -> ObjectUtil.equal(tradeType,t.getTradeType()))
                .filter(t -> ObjectUtil.equal(orderPlatform,t.getOrderPlatform()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentMergeOrderDO> selectMergerOrderByPayNo(String payNo) {

        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentMergeOrderDO::getPayNo,payNo);
        wrapper.lambda().orderByAsc(PaymentMergeOrderDO::getPayDate);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentMergeOrderDO> selectMergerOrderByOrderNoList(String appOrderNo) {
        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentMergeOrderDO::getAppOrderNo,appOrderNo);
        wrapper.lambda().orderByAsc(PaymentMergeOrderDO::getPayDate);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentMergeOrderDO> selectMergerOrderByOrderNoList(String orderPlatform, String appOrderNo) {

        List<PaymentMergeOrderDO> paymentMergeOrderDOList = this.selectMergerOrderByOrderNoList(appOrderNo);

        if (CollectionUtil.isEmpty(paymentMergeOrderDOList)) {

            return Collections.emptyList();
        }

        return paymentMergeOrderDOList.stream().filter(t -> orderPlatform.equals(t.getOrderPlatform())).collect(Collectors.toList());
    }

    @Override
    public List<PaymentMergeOrderDO> selectMergerOrderByOrderNoList(String orderPlatform,Integer tradeType,List<String> orderNos) {
        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();

        if (orderNos.size() > 1) {
            wrapper.lambda().in(PaymentMergeOrderDO::getAppOrderNo,orderNos);
        } else {
            wrapper.lambda().eq(PaymentMergeOrderDO::getAppOrderNo,orderNos.get(0));
        }

        List<PaymentMergeOrderDO> resultList =  this.baseMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(resultList)) {

            return Collections.emptyList();
        }

        return resultList.stream()
                .filter(t -> ObjectUtil.equal(tradeType,t.getTradeType()))
                .filter(t -> ObjectUtil.equal(orderPlatform,t.getOrderPlatform()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentMergeOrderDO> selectFinishMergeOrderList(Long appOrderId, Integer tradeType) {
        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentMergeOrderDO::getAppOrderId,appOrderId);
        wrapper.lambda().in(PaymentMergeOrderDO::getTradeType,tradeType);
        wrapper.lambda().eq(PaymentMergeOrderDO::getAppOrderStatus,AppOrderStatusEnum.SUCCESS.getCode());
        wrapper.lambda().orderByAsc(PaymentMergeOrderDO::getPayDate);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public Boolean updateMergeOrderFundAmount(Long mergeId, BigDecimal refundAmount) {
        PaymentMergeOrderDO paymentMergeOrderDO = this.baseMapper.selectById(mergeId);
        if (paymentMergeOrderDO == null) {
            return false;
        }

        BigDecimal waitRefundMoney = NumberUtil.sub(paymentMergeOrderDO.getPayAmount(),paymentMergeOrderDO.getRefundAmount());
        PaymentMergeOrderDO updateMergeOrder = new PaymentMergeOrderDO();
        updateMergeOrder.setId(mergeId);
        updateMergeOrder.setRefundAmount(NumberUtil.add(paymentMergeOrderDO.getRefundAmount(),refundAmount));

        if (CompareUtil.compare(refundAmount,waitRefundMoney) > 0 ) {
            throw new BusinessException(PaymentErrorCode.REFUND_MONEY_ERROR);
        } else if (CompareUtil.compare(refundAmount,waitRefundMoney) == 0) {
            updateMergeOrder.setRefundState(3);
        } else {
            updateMergeOrder.setRefundState(2);
        }
        return this.updateById(updateMergeOrder);
    }

    @Override
    public List<PaymentMergeOrderDO> selectMergerOrderByPayId(String payId) {
        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentMergeOrderDO::getPayId,payId);
        wrapper.lambda().orderByAsc(PaymentMergeOrderDO::getCreateTime);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void updateMergeOrderAppOrderStatus(String payId, Integer appOrderStatus,Date limitTime,String payNo) {

        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentMergeOrderDO::getPayId,payId);
        PaymentMergeOrderDO updateMergeOrder = new PaymentMergeOrderDO();
        updateMergeOrder.setAppOrderStatus(appOrderStatus);
        // 支付成功，设置支付成功时间
        if (AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(appOrderStatus)) {
            updateMergeOrder.setPayDate(new Date());
        }
        if (limitTime != null) {
            updateMergeOrder.setLimitTime(limitTime);
        }
        if (StringUtils.isNotBlank(payNo)) {
            updateMergeOrder.setPayNo(payNo);
        }
        this.update(updateMergeOrder,wrapper);
    }


    @Override
    public void updateMergeOrderByPayId(PaymentMergeOrderDO paymentMergeOrderDO) {

        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentMergeOrderDO::getPayId,paymentMergeOrderDO.getPayId());

        this.update(paymentMergeOrderDO,wrapper);
    }

    @Override
    public List<PaymentMergeOrderDO> selectWaitPayOrderList() {
        QueryWrapper<PaymentMergeOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentMergeOrderDO::getAppOrderStatus,AppOrderStatusEnum.WAIT_PAY.getCode());
        wrapper.lambda().le(PaymentMergeOrderDO::getLimitTime, DateUtil.offsetMinute(new Date(),5));

        return this.baseMapper.selectList(wrapper);
    }
}
