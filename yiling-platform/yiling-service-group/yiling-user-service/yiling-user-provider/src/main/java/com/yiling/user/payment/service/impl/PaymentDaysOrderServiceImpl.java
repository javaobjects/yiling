package com.yiling.user.payment.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.payment.dao.PaymentDaysAccountLogMapper;
import com.yiling.user.payment.dao.PaymentDaysAccountMapper;
import com.yiling.user.payment.dao.PaymentDaysCompanyMapper;
import com.yiling.user.payment.dao.PaymentDaysOrderMapper;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.dto.request.UpdatePaymentOrderAmountRequest;
import com.yiling.user.payment.entity.PaymentDaysAccountDO;
import com.yiling.user.payment.entity.PaymentDaysAccountLogDO;
import com.yiling.user.payment.entity.PaymentDaysOrderDO;
import com.yiling.user.payment.enums.PaymentAccountTypeEnum;
import com.yiling.user.payment.enums.PaymentOrderRepaymentStatusEnum;
import com.yiling.user.payment.service.PaymentDaysOrderService;
import com.yiling.user.system.enums.PaymentDaysLogTypeEnum;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 账期使用订单信息 服务实现类
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Service
public class PaymentDaysOrderServiceImpl extends BaseServiceImpl<PaymentDaysOrderMapper, PaymentDaysOrderDO> implements PaymentDaysOrderService {

    @Autowired
    PaymentDaysOrderMapper paymentDaysOrderMapper;
    @Autowired
    PaymentDaysAccountMapper paymentDaysAccountMapper;
    @Autowired
    PaymentDaysAccountLogMapper paymentDaysAccountLogMapper;
    @Autowired
    PaymentDaysCompanyMapper paymentDaysCompanyMapper;

    @Override
    public boolean updateReturnAmountByOrderId(Long orderId, BigDecimal returnAmount, Long opUserId) {
        PaymentDaysOrderDO paymentDaysOrderDo = this.getPaymentDaysOrderDo(orderId);

        QueryWrapper<PaymentDaysOrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PaymentDaysOrderDO::getOrderId, orderId);

        PaymentDaysOrderDO entity = new PaymentDaysOrderDO();
        entity.setReturnAmount(paymentDaysOrderDo.getReturnAmount().add(returnAmount));
        entity.setOpUserId(opUserId);

        return this.update(entity, queryWrapper);
    }

    @Override
    public boolean updateOrderTicketDiscountAmount(Long orderId, BigDecimal ticketDiscountAmount, Long opUserId) {
        PaymentDaysOrderDO paymentDaysOrderDo = this.getPaymentDaysOrderDo(orderId);

        QueryWrapper<PaymentDaysOrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PaymentDaysOrderDO::getOrderId, orderId);

        PaymentDaysOrderDO entity = new PaymentDaysOrderDO();
        entity.setUsedAmount(paymentDaysOrderDo.getUsedAmount().subtract(ticketDiscountAmount));
        entity.setOpUserId(opUserId);

        return this.update(entity, queryWrapper);
    }

    @Override
    public Page<PaymentDaysOrderDO> pageList(QueryExpireDayOrderRequest request) {
        if(Objects.nonNull(request.getStartTime())){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(Objects.nonNull(request.getEndTime())){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }

        return paymentDaysOrderMapper.pageList(request.getPage(),request);
    }

    @Override
    public List<PaymentDaysOrderDO> expireOrderAmountCount(QueryExpireDayOrderRequest request) {
        if(Objects.nonNull(request.getStartTime())){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(Objects.nonNull(request.getEndTime())){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }

        return paymentDaysOrderMapper.pageList(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePaymentOrderAmount(UpdatePaymentOrderAmountRequest request) {
        //根据订单id获取账期订单信息
        PaymentDaysOrderDO paymentDaysOrderDO = this.getPaymentDaysOrderDo(request.getOrderId());

        //账期订单还款状态幂等性校验
        if(PaymentOrderRepaymentStatusEnum.NO_REPAYMENT.getCode().compareTo(paymentDaysOrderDO.getRepaymentStatus()) != 0){
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_STATUS_ERROR);
        }

        PaymentDaysAccountDO paymentDaysAccountDO = Optional.ofNullable(paymentDaysAccountMapper.selectById(paymentDaysOrderDO.getAccountId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS));

        //更新账期订单：还款状态为全部还款、更新已还款金额
        BigDecimal repaymentAmount = paymentDaysOrderDO.getUsedAmount().subtract(paymentDaysOrderDO.getReturnAmount());
        PaymentDaysOrderDO daysOrderDO = new PaymentDaysOrderDO();
        daysOrderDO.setId(paymentDaysOrderDO.getId());
        daysOrderDO.setRepaymentAmount(repaymentAmount);
        daysOrderDO.setOpUserId(request.getOpUserId());
        daysOrderDO.setRepaymentStatus(PaymentOrderRepaymentStatusEnum.ALL_REPAYMENT.getCode());
        daysOrderDO.setRepaymentTime(new Date());
        this.updateById(daysOrderDO);

        //更新账期账户：减已使用额度(无需更新)、加已还款额度、加可使用额度
        PaymentDaysAccountDO daysAccountDO = new PaymentDaysAccountDO();
        daysAccountDO.setId(paymentDaysAccountDO.getId());
        daysAccountDO.setRepaymentAmount(repaymentAmount);
        daysAccountDO.setAvailableAmount(repaymentAmount);
        daysAccountDO.setUpdateUser(request.getOpUserId());
        daysAccountDO.setUpdateTime(new Date());
        paymentDaysAccountMapper.updateRepayment(daysAccountDO);

        if (PaymentAccountTypeEnum.getByCode(paymentDaysAccountDO.getType()) == PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT) {
            //更新集团账期
            paymentDaysCompanyMapper.repayment(repaymentAmount);
        }

        //更新账期账户日志
        PaymentDaysAccountLogDO paymentDaysAccountLogDO = new PaymentDaysAccountLogDO();
        paymentDaysAccountLogDO.setAccountId(paymentDaysAccountDO.getId());
        paymentDaysAccountLogDO.setBusinessNo(paymentDaysOrderDO.getOrderNo());
        paymentDaysAccountLogDO.setBusinessType(PaymentDaysLogTypeEnum.REPAYMENT.getCode());
        paymentDaysAccountLogDO.setChangedAmount(repaymentAmount);
        paymentDaysAccountLogDO.setCreateUser(request.getOpUserId());
        paymentDaysAccountLogDO.setRemark("账期订单还款，使用金额："+paymentDaysOrderDO.getUsedAmount()
                +" 退款金额："+paymentDaysOrderDO.getReturnAmount()+" 还款金额："+repaymentAmount);
        paymentDaysAccountLogMapper.insert(paymentDaysAccountLogDO);

        return true;
    }

    @Override
    public PaymentDaysOrderDO getPaymentDaysOrderDo(Long orderId) {
        LambdaQueryWrapper<PaymentDaysOrderDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PaymentDaysOrderDO::getOrderId, orderId);
        return Optional.ofNullable(this.getOne(lambdaQueryWrapper))
                .orElseThrow(() -> new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_NOT_EXISTS));
    }

    /**
     * 根据订单id获取账期订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public PaymentDaysOrderDO getPaymentDaysOrderOne(Long orderId) {
        LambdaQueryWrapper<PaymentDaysOrderDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PaymentDaysOrderDO::getOrderId, orderId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<PaymentDaysOrderDTO> getUnRepaymentOrderByCustomerEid(Long customerEid) {
        return PojoUtils.map(paymentDaysOrderMapper.getUnRepaymentOrderByCustomerEid(customerEid),PaymentDaysOrderDTO.class);
    }

    @Override
    public Page<PaymentDaysOrderDO> getQuotaOrderPage(QueryQuotaOrderRequest request) {
        return paymentDaysOrderMapper.getQuotaOrderPage(request.getPage(),request);
    }

    @Override
    public List<PaymentDaysOrderDO> getQuotaOrderList(QueryQuotaOrderRequest request) {
        return paymentDaysOrderMapper.getQuotaOrderPage(request);
    }
}
