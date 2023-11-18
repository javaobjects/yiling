package com.yiling.user.payment.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dto.MainPartEnterpriseDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.bo.PaymentDaysAmountBO;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.PaymentDaysCompanyDTO;
import com.yiling.user.payment.dto.PaymentDaysOrderAmountCountDTO;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.PaymentRepaymentOrderDTO;
import com.yiling.user.payment.dto.QuotaOrderDTO;
import com.yiling.user.payment.dto.ShortTimeQuotaDTO;
import com.yiling.user.payment.dto.request.ApplyPaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.CreatePaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.PaymentDaysCompanyRequest;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.dto.request.QueryShortTimeQuotaAccountRequest;
import com.yiling.user.payment.dto.request.ReduceQuotaRequest;
import com.yiling.user.payment.dto.request.RefundPaymentDaysAmountRequest;
import com.yiling.user.payment.dto.request.UpdateInvoiceTicketDiscountRequest;
import com.yiling.user.payment.dto.request.UpdatePaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.UpdatePaymentOrderAmountRequest;
import com.yiling.user.payment.entity.PaymentDaysAccountDO;
import com.yiling.user.payment.entity.PaymentDaysOrderDO;
import com.yiling.user.payment.service.PaymentDaysAccountService;
import com.yiling.user.payment.service.PaymentDaysCompanyService;
import com.yiling.user.payment.service.PaymentDaysOrderService;

/**
 * 账期账户 API 实现
 *
 * @author xuan.zhou
 * @date 2021/7/1
 */
@DubboService
public class PaymentDaysAccountApiImpl implements PaymentDaysAccountApi {

    @Autowired
    private PaymentDaysAccountService paymentDaysAccountService;
    @Autowired
    PaymentDaysCompanyService paymentDaysCompanyService;
    @Autowired
    private PaymentDaysOrderService paymentDaysOrderService;

    @Override
    public Page<PaymentDaysAccountDTO> pageList(QueryPaymentDaysAccountPageListRequest request) {
        Page<PaymentDaysAccountDO> page = paymentDaysAccountService.pageList(request);
        return PojoUtils.map(page, PaymentDaysAccountDTO.class);
    }

    @Override
    public Boolean reduceQuota(ReduceQuotaRequest reduceQuotaRequest) {
        return paymentDaysAccountService.reduceQuota(reduceQuotaRequest);
    }

    @Override
    public boolean refund(RefundPaymentDaysAmountRequest request) {
        return paymentDaysAccountService.refund(request);
    }

    @Override
    public boolean updateTicketDiscountAmount(UpdateInvoiceTicketDiscountRequest request) {
        return paymentDaysAccountService.updateTicketDiscountAmount(request);
    }

    @Override
    public Page<ShortTimeQuotaDTO> shortTimeQuotaPage(QueryShortTimeQuotaAccountRequest request) {
        return paymentDaysAccountService.shortTimeQuotaPage(request);
    }

    @Override
    public Page<QuotaOrderDTO> quotaOrderPage(QueryQuotaOrderRequest request) {
        return paymentDaysAccountService.quotaOrderPage(request);
    }

    @Override
    public PaymentRepaymentOrderDTO getRepaymentOrderAmount(QueryQuotaOrderRequest request) {
        return paymentDaysAccountService.getRepaymentOrderAmount(request);
    }

    @Override
    public Page<PaymentDaysOrderDTO> expireDayOrderPage(QueryExpireDayOrderRequest request) {
        return paymentDaysAccountService.expireDayOrderPage(request);
    }

    @Override
    public List<PaymentDaysOrderDTO> expireOrderAmountCount(QueryExpireDayOrderRequest request) {
        return PojoUtils.map(paymentDaysOrderService.expireOrderAmountCount(request),PaymentDaysOrderDTO.class);
    }

    @Override
    public boolean create(CreatePaymentDaysAccountRequest request) {
        return paymentDaysAccountService.create(request);
    }

    @Override
    public boolean update(UpdatePaymentDaysAccountRequest request) {
        return paymentDaysAccountService.update(request);
    }

    @Override
    public PaymentDaysAccountDTO getByCustomerEid(Long eid, Long customerEid) {
        return PojoUtils.map(paymentDaysAccountService.getByCustomerEid(eid, customerEid), PaymentDaysAccountDTO.class);
    }

    @Override
    public BigDecimal getAvailableAmountByCustomerEid(Long eid, Long customerEid) {
        return paymentDaysAccountService.getAvailableAmountByCustomerEid(eid, customerEid,PlatformEnum.POP);
    }

    @Override
    public BigDecimal getB2bAvailableAmountByCustomerEid(Long eid, Long customerEid) {
        return paymentDaysAccountService.getAvailableAmountByCustomerEid(eid, customerEid,PlatformEnum.B2B);
    }

    @Override
    public Boolean applyQuota(ApplyPaymentDaysAccountRequest request) {
        return paymentDaysAccountService.applyQuota(request);
    }

    @Override
    public Boolean checkQuota(ApplyPaymentDaysAccountRequest request) {
        return paymentDaysAccountService.checkQuota(request);
    }

    @Override
    public PaymentDaysCompanyDTO get() {
        return PojoUtils.map(paymentDaysCompanyService.get(),PaymentDaysCompanyDTO.class);
    }

    @Override
    public Boolean saveOrUpdateCompanyDetail(PaymentDaysCompanyRequest request) {
        return paymentDaysAccountService.saveOrUpdateCompanyDetail(request);
    }

    @Override
    public PaymentDaysAccountDTO getPaymentAccountDetailByOrderId(Long orderId) {
        return paymentDaysAccountService.getPaymentAccountDetailByOrderId(orderId);
    }

    @Override
    public PaymentDaysAmountBO countTotalAmount(Long accountId) {
        return paymentDaysAccountService.countTotalAmount(accountId);
    }

    @Override
    public List<PaymentDaysOrderDTO> getPaymentOrderByAccountId(Long accountId) {
        return PojoUtils.map(paymentDaysOrderService.list(
                new LambdaQueryWrapper<PaymentDaysOrderDO>().eq(PaymentDaysOrderDO::getAccountId,accountId)),PaymentDaysOrderDTO.class);
    }

    @Override
    public Boolean modifyAuditRefund(RefundPaymentDaysAmountRequest request) {

        return paymentDaysAccountService.modifyAuditRefund(request);
    }

    @Override
    public Boolean updatePaymentOrderAmount(UpdatePaymentOrderAmountRequest request) {
        return paymentDaysOrderService.updatePaymentOrderAmount(request);
    }

    @Override
    public MainPartEnterpriseDTO listMainPart(Long currentEid, Long currentUserId) {
        return paymentDaysAccountService.listMainPart(currentEid,currentUserId);
    }

    @Override
    public PaymentDaysOrderAmountCountDTO getOrderAmountCount(QueryQuotaOrderRequest request) {
        return paymentDaysAccountService.getOrderAmountCount(request);
    }

    @Override
    public List<PaymentDaysOrderDTO> getUnRepaymentOrderByCustomerEid(Long customerEid) {
        return paymentDaysOrderService.getUnRepaymentOrderByCustomerEid(customerEid);
    }

    @Override
    public PaymentDaysAccountDTO getPaymentDaysAccountById(Long accountId) {
        return paymentDaysAccountService.getPaymentDaysAccountById(accountId);
    }

    @Override
    public List<PaymentDaysAccountDTO> getByCustomerEid(Long customerEid) {
        return paymentDaysAccountService.getByCustomerEid(customerEid);
    }

    /**
     * 根据订单id获取账期订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public PaymentDaysOrderDTO getPaymentDaysOrderOne(Long orderId) {
        return PojoUtils.map(paymentDaysOrderService.getPaymentDaysOrderOne(orderId),PaymentDaysOrderDTO.class);
    }

    @Override
    public List<Long> getPaymentDaysUnRepayment(Long customerEid) {
        return paymentDaysAccountService.getPaymentDaysUnRepayment(customerEid);
    }

    @Override
    public List<Long> getPaymentDaysUnReceive(Long eid) {
        return paymentDaysAccountService.getPaymentDaysUnReceive(eid);
    }
}
