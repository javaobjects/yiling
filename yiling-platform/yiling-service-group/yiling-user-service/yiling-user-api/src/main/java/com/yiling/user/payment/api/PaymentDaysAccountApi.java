package com.yiling.user.payment.api;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.MainPartEnterpriseDTO;
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

/**
 * 账期账户 API
 *
 * @author xuan.zhou
 * @date 2021/7/1
 */
public interface PaymentDaysAccountApi {

    /**
     * 分页列表
     *
     * @param request
     * @return
     */
    Page<PaymentDaysAccountDTO> pageList(QueryPaymentDaysAccountPageListRequest request);


    /**
     * 扣减采购商账户额度
     *
     * @param reduceQuotaRequest
     * @return
     */
    Boolean reduceQuota(ReduceQuotaRequest reduceQuotaRequest);

    /**
     * 退还账期额度
     *
     * @param request
     * @return
     */
    boolean refund(RefundPaymentDaysAmountRequest request);

    /**
     * 开票后扣除票折金额
     * @param request
     * @return
     */
    boolean updateTicketDiscountAmount(UpdateInvoiceTicketDiscountRequest request);

    /**
     * 临时额度申请分页列表
     *
     * @param request
     * @return
     */
    Page<ShortTimeQuotaDTO> shortTimeQuotaPage(QueryShortTimeQuotaAccountRequest request);
    /**
     * 账期绑定订单列表
     *
     * @param request
     * @return
     */
    Page<QuotaOrderDTO> quotaOrderPage(QueryQuotaOrderRequest request);
    /**
     * 账期已还款订单金额总计
     *
     * @param request
     * @return
     */
    PaymentRepaymentOrderDTO getRepaymentOrderAmount(QueryQuotaOrderRequest request);
    /**
     * 账期到期提醒列表
     *
     * @param request
     * @return
     */
    Page<PaymentDaysOrderDTO> expireDayOrderPage(QueryExpireDayOrderRequest request);

    /**
     * 账期到期提醒金额
     * @param request
     * @return
     */
    List<PaymentDaysOrderDTO> expireOrderAmountCount(QueryExpireDayOrderRequest request);

    /**
     * 创建账期账户
     *
     * @param request
     * @return
     */
    boolean create(CreatePaymentDaysAccountRequest request);

    /**
     * 修改账期账号
     * @param request
     * @return
     */
    boolean update(UpdatePaymentDaysAccountRequest request);

    /**
     * 获取企业账期账户信息
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    PaymentDaysAccountDTO getByCustomerEid(Long eid, Long customerEid);

    /**
     * 获取POP客户账期可用额度 <br/>
     * 1、未设置账期额度(无账期账户)时，返回0 <br/>
     * 2、账期账户被停用时，返回0 <br/>
     * 3、当前时间不在账期授信时间范围内时，返回0 <br/>
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    BigDecimal getAvailableAmountByCustomerEid(Long eid, Long customerEid);

    /**
     * 获取B2B客户账期可用额度 <br/>
     * 1、未设置账期额度(无账期账户)时，返回0 <br/>
     * 2、账期账户被停用时，返回0 <br/>
     * 3、当前时间不在账期授信时间范围内时，返回0 <br/>
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    BigDecimal getB2bAvailableAmountByCustomerEid(Long eid, Long customerEid);

    /**
     * 以岭采购商申请临时额度
     * @param request
     * @return
     */
    Boolean applyQuota(ApplyPaymentDaysAccountRequest request);

    /**
     * 以岭采购商申请临时额度审核
     * @param request
     * @return
     */
    Boolean checkQuota(ApplyPaymentDaysAccountRequest request);
    /**
     * 获取集团信息
     * @param
     * @return
     */
    PaymentDaysCompanyDTO get();
    /**
     * 新增或者保存集团信息
     * @param request
     * @return
     */
    Boolean saveOrUpdateCompanyDetail(PaymentDaysCompanyRequest request);

    /**
     * 根据订单id查询订单账期信息
     * @param orderId
     * @return
     */
    PaymentDaysAccountDTO getPaymentAccountDetailByOrderId(Long orderId);
    /**
     * 根据账期id查询总退款额度
     * @param accountId
     * @return
     */
    PaymentDaysAmountBO countTotalAmount(Long accountId);

    /**
     * 根据账户id获取账期账户列表
     * @param accountId
     * @return
     */
    List<PaymentDaysOrderDTO> getPaymentOrderByAccountId(Long accountId);

    /**
     * 反审核退款
     * @param request
     * @return
     */
    Boolean modifyAuditRefund(RefundPaymentDaysAmountRequest request);

    /**
     * 更新账期订单的还款金额
     * @param request
     * @return
     */
    Boolean updatePaymentOrderAmount(UpdatePaymentOrderAmountRequest request);

    /**
     * 获取授信主体列表
     * @param currentEid
     * @return
     */
    MainPartEnterpriseDTO listMainPart(Long currentEid, Long currentUserId);

    /**
     * B2B-已使用/已还款/待还款的相关金额总计
     * @param request
     * @return
     */
    PaymentDaysOrderAmountCountDTO getOrderAmountCount(QueryQuotaOrderRequest request);

    /**
     * 查询企业待还款订单
     * @param customerEid
     * @return
     */
    List<PaymentDaysOrderDTO> getUnRepaymentOrderByCustomerEid(Long customerEid);

    /**
     * 根据账期账户ID获取账期账户信息
     * @param accountId
     * @return
     */
    PaymentDaysAccountDTO getPaymentDaysAccountById(Long accountId);

    /**
     * B2B-根据采购商ID获取账期账户信息
     *
     * @param customerEid 企业ID
     * @return
     */
    List<PaymentDaysAccountDTO> getByCustomerEid(Long customerEid);

    /**
     * 根据订单id获取账期订单信息
     * @param orderId
     * @return
     */
    PaymentDaysOrderDTO getPaymentDaysOrderOne(Long orderId);

    /**
     * 根据采购商ID查询存在未还款的账期供应商ID
     *
     * @param customerEid
     * @return
     */
    List<Long> getPaymentDaysUnRepayment(Long customerEid);

    /**
     * 根据供应商ID查询存在未收款的账期的供应商ID
     *
     * @param eid
     * @return
     */
    List<Long> getPaymentDaysUnReceive(Long eid);
}
