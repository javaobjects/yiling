package com.yiling.payment.pay.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.pay.dao.PaymentTradeMapper;
import com.yiling.payment.pay.entity.PaymentTradeDO;
import com.yiling.payment.pay.service.PaymentTradeService;
import com.yiling.payment.pay.util.NoUtil;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 交易订单记录表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Service
@Slf4j
public class PaymentTradeServiceImpl extends BaseServiceImpl<PaymentTradeMapper, PaymentTradeDO> implements PaymentTradeService {
    @Value("${spring.profiles.active}")
    private String                  env;
    @Autowired
    private NoUtil                  noUtil;

    @Override
    public Result<String> insertPaymentTrade(PaymentTradeDO paymentTradeDo) {

        // 交易流水号
        String payNo = noUtil.buildPayNo();

        paymentTradeDo.setPayNo(payNo);

        int result = this.baseMapper.insert(paymentTradeDo);

        if (result > 0) {

            return Result.success(payNo);
        }

        return Result.failed("创建交易流水数据失败!");
    }


    @Override
    public boolean updatePaymentTradeStatus(PaymentTradeDO paymentTradeDo) {

        PaymentTradeDO updatePayTradeDo = new PaymentTradeDO();
        if (StringUtils.isNotBlank(paymentTradeDo.getThirdTradeNo())) {
            updatePayTradeDo.setThirdTradeNo(paymentTradeDo.getThirdTradeNo());
        }
        if (paymentTradeDo.getLimitTime() != null) {
            updatePayTradeDo.setLimitTime(paymentTradeDo.getLimitTime());
        }
        if (paymentTradeDo.getThirdTradeNo() != null) {
            updatePayTradeDo.setThirdTradeNo(paymentTradeDo.getThirdTradeNo());
        }
        if (StringUtils.isNotBlank(paymentTradeDo.getContent())) {
            updatePayTradeDo.setContent(paymentTradeDo.getContent());
        }
        if (StringUtils.isNotBlank(paymentTradeDo.getBank())) {
            updatePayTradeDo.setBank(paymentTradeDo.getBank());
        }
        updatePayTradeDo.setTradeStatus(paymentTradeDo.getTradeStatus());
        updatePayTradeDo.setErrorMessage(paymentTradeDo.getErrorMessage());

        QueryWrapper<PaymentTradeDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentTradeDO::getPayNo,paymentTradeDo.getPayNo());

        return this.update(updatePayTradeDo,wrapper);
    }


    @Override
    public PaymentTradeDO selectPaymentTradeByPayNo(String payNo) {
        QueryWrapper<PaymentTradeDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentTradeDO::getPayNo,payNo);

        return this.baseMapper.selectOne(wrapper);
    }


    @Override
    public List<PaymentTradeDO> selectWaitPaymentTrades(Integer limit) {
        QueryWrapper<PaymentTradeDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PaymentTradeDO::getTradeStatus, ListUtil.toList(TradeStatusEnum.WAIT_PAY.getCode(),TradeStatusEnum.BANK_ING.getCode()));
        wrapper.lambda().le(PaymentTradeDO::getLimitTime,new Date()).last("limit " + limit);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentTradeDO> selectFinishPaymentTradeByPayId(String payId) {
        QueryWrapper<PaymentTradeDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentTradeDO::getTradeStatus, TradeStatusEnum.SUCCESS.getCode());
        wrapper.lambda().eq(PaymentTradeDO::getPayId,payId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<PaymentTradeDO> selectPayIngTradeByPayId(String payId) {
        QueryWrapper<PaymentTradeDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PaymentTradeDO::getTradeStatus, ListUtil.toList(TradeStatusEnum.WAIT_PAY.getCode(),TradeStatusEnum.BANK_ING.getCode()));
        wrapper.lambda().eq(PaymentTradeDO::getPayId,payId);
        return this.baseMapper.selectList(wrapper);
    }


    @Override
    public PaymentTradeDO selectPaymentTradeByBank(String bankOrderId) {
        QueryWrapper<PaymentTradeDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentTradeDO::getBank,bankOrderId);

        return this.baseMapper.selectOne(wrapper);
    }
}
