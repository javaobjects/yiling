package com.yiling.payment.pay.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.pay.dao.PaymentRefundMapper;
import com.yiling.payment.pay.entity.PaymentRefundDO;
import com.yiling.payment.pay.service.PaymentRefundService;
import com.yiling.payment.pay.util.NoUtil;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 支付退款表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Service
@Slf4j
public class PaymentRefundServiceImpl extends BaseServiceImpl<PaymentRefundMapper, PaymentRefundDO> implements PaymentRefundService {
    @Autowired
    private NoUtil noUtil;

    @Override
    public Result<String> insertPaymentRefundDo(PaymentRefundDO paymentRefundDo) {
        paymentRefundDo.setRefundNo(noUtil.buildRefundNo());
        paymentRefundDo.setRefundState(RefundStateEnum.WAIT_REFUND.getCode());
        Boolean result = this.save(paymentRefundDo);

        if (result) {
            return Result.success(paymentRefundDo.getRefundNo());
        }

        return Result.failed("插入退款单失败!");
    }

    @Override
    public List<PaymentRefundDO> listPaymentRefundsByAppOrderId(Long appOrderId,String payNo) {

        QueryWrapper<PaymentRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentRefundDO::getAppOrderId,appOrderId);
        if (StringUtils.isNotBlank(payNo)) {
            wrapper.lambda().eq(PaymentRefundDO::getPayNo,payNo);
        }
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentRefundDO> listPaymentRefundsByRefundId(Long refundId) {

        QueryWrapper<PaymentRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentRefundDO::getRefundId,refundId);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public boolean updatePaymentRefundStatus(PaymentRefundDO paymentRefundDo) {
        PaymentRefundDO updateRefund = new PaymentRefundDO();
        if (StringUtils.isNotBlank(paymentRefundDo.getThirdFundNo())) {
            updateRefund.setThirdFundNo(paymentRefundDo.getThirdFundNo());
        }
        if (ObjectUtil.isNotNull(paymentRefundDo.getRefundDate())) {
            updateRefund.setRefundDate(paymentRefundDo.getRefundDate());
        }
        updateRefund.setRefundState(paymentRefundDo.getRefundState());
        updateRefund.setErrorMessage(paymentRefundDo.getErrorMessage());
        QueryWrapper<PaymentRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentRefundDO::getRefundNo,paymentRefundDo.getRefundNo());

        return this.update(paymentRefundDo,wrapper);
    }

    @Override
    public boolean batchClosePaymentRefund(List<PaymentRefundDO> paymentRefundDOList) {
        List<PaymentRefundDO>  updateRefundList = paymentRefundDOList.stream().map(paymentRefundDo -> {
            PaymentRefundDO updateRefund = new PaymentRefundDO();
            if (StringUtils.isNotBlank(paymentRefundDo.getThirdFundNo())) {
                updateRefund.setThirdFundNo(paymentRefundDo.getThirdFundNo());
            }
            updateRefund.setRefundState(RefundStateEnum.CLOSE.getCode());
            updateRefund.setUpdateTime(new Date());
            updateRefund.setId(paymentRefundDo.getId());

            return updateRefund;
        }).collect(Collectors.toList());

        return this.updateBatchById(updateRefundList);
    }

    @Override
    public List<PaymentRefundDO> listWaitRefundList(Integer limit) {

        QueryWrapper<PaymentRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentRefundDO::getRefundState, RefundStateEnum.WAIT_REFUND.getCode());
        wrapper.lambda().last("limit " + limit );

        if (limit > 0) {
            wrapper.last("limit " + limit);
        }

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentRefundDO> listRefundIngList(Integer limit) {
        QueryWrapper<PaymentRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentRefundDO::getRefundState, RefundStateEnum.REFUND_ING.getCode());
        wrapper.lambda().le(PaymentRefundDO::getCreateTime, DateUtil.offsetHour(new Date(), -1));
        wrapper.lambda().last("limit " + limit );
        if (limit > 0) {
            wrapper.last("limit " + limit);
        }
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentRefundDO> selectRefundList(String refundNo) {

        QueryWrapper<PaymentRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentRefundDO::getRefundNo,refundNo);

        return this.baseMapper.selectList(wrapper);
    }


    @Override
    public List<PaymentRefundDO> selectTimeOutRefundOrderList() {

        QueryWrapper<PaymentRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PaymentRefundDO::getRefundState, ListUtil.toList(RefundStateEnum.REFUND_ING.getCode(),RefundStateEnum.WAIT_REFUND.getCode()));
        wrapper.lambda().le(PaymentRefundDO::getCreateTime, DateUtil.offsetDay(new Date(), -1));

        return this.baseMapper.selectList(wrapper);
    }
}
