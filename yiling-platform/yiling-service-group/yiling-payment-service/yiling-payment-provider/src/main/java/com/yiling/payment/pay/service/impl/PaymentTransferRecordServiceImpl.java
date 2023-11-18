package com.yiling.payment.pay.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.pay.dao.PaymentTransferRecordMapper;
import com.yiling.payment.pay.entity.PaymentTransferRecordDO;
import com.yiling.payment.pay.service.PaymentTransferRecordService;
import com.yiling.payment.pay.util.NoUtil;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 企业付款记录表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-11-19
 */
@Service
public class PaymentTransferRecordServiceImpl extends BaseServiceImpl<PaymentTransferRecordMapper, PaymentTransferRecordDO> implements PaymentTransferRecordService {
    @Autowired
    private NoUtil noUtil;

    @Override
    public Boolean batchInsertTransferRecord(List<PaymentTransferRecordDO> paymentTransferRecordDos) {
        paymentTransferRecordDos.stream().forEach(t -> {
            t.setPayNo(noUtil.buildPayNo());
        });

        return this.saveBatch(paymentTransferRecordDos);
    }

    @Override
    public boolean batchUpdateTransferRecordByPayNO(List<PaymentTransferRecordDO> paymentTransferRecordDos) {

        for (PaymentTransferRecordDO paymentTransferRecordDO : paymentTransferRecordDos) {
            QueryWrapper<PaymentTransferRecordDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(PaymentTransferRecordDO::getPayNo,paymentTransferRecordDO.getPayNo());
            this.update(paymentTransferRecordDO,wrapper);
        }

        return true;
    }


    @Override
    public PaymentTransferRecordDO selectPaymentTransferRecordByPayNo(String payNo) {
        QueryWrapper<PaymentTransferRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentTransferRecordDO::getPayNo,payNo);

        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public List<PaymentTransferRecordDO> listTransferIngRecords(Integer limit) {
        QueryWrapper<PaymentTransferRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentTransferRecordDO::getTradeStatus, TradeStatusEnum.BANK_ING.getCode());
        wrapper.lambda().le(PaymentTransferRecordDO::getTradeDate, DateUtil.offsetMinute(new Date(), -10));
        wrapper.lambda().last("limit " + limit);

        return this.baseMapper.selectList(wrapper);
    }


    @Override
    public List<PaymentTransferRecordDO> listWaitTransferRecords(Integer limit) {
        QueryWrapper<PaymentTransferRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentTransferRecordDO::getTradeStatus, TradeStatusEnum.WAIT_PAY.getCode());
        wrapper.lambda().le(PaymentTransferRecordDO::getCreateTime, new Date());
        wrapper.lambda().last("limit " + limit);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentTransferRecordDO> listTransferIngByBusinessId(List<Long> businessIds, Integer type) {
        QueryWrapper<PaymentTransferRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PaymentTransferRecordDO::getTradeStatus, ListUtil.toList(TradeStatusEnum.BANK_ING.getCode(),TradeStatusEnum.WAIT_PAY.getCode(),TradeStatusEnum.SUCCESS.getCode()));
        wrapper.lambda().in(PaymentTransferRecordDO::getBusinessId,businessIds);
        wrapper.lambda().eq(PaymentTransferRecordDO::getTradeType,type);

        return this.baseMapper.selectList(wrapper);
    }
}
