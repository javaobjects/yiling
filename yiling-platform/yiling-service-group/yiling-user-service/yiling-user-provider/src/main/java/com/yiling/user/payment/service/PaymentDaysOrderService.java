package com.yiling.user.payment.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.dto.request.UpdatePaymentOrderAmountRequest;
import com.yiling.user.payment.entity.PaymentDaysOrderDO;

/**
 * <p>
 * 账期使用订单信息 服务类
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
public interface PaymentDaysOrderService extends BaseService<PaymentDaysOrderDO> {

    /**
     * 更新账期订单退款金额
     *
     * @param orderId 订单ID
     * @param returnAmount 退款金额
     * @param opUserId 操作人ID
     * @return
     */
    boolean updateReturnAmountByOrderId(Long orderId, BigDecimal returnAmount, Long opUserId);

    /**
     * 更新订单票折金额
     * @param orderId
     * @param ticketDiscountAmount
     * @param opUserId
     * @return
     */
    boolean updateOrderTicketDiscountAmount(Long orderId , BigDecimal ticketDiscountAmount , Long opUserId);

    /**
     * 更新账期订单退款金额
     *
     * @param request
     * @return
     */
    Page<PaymentDaysOrderDO> pageList(QueryExpireDayOrderRequest request);

    /**
     * 账期到期提醒金额
     * @param request
     * @return
     */
    List<PaymentDaysOrderDO> expireOrderAmountCount(QueryExpireDayOrderRequest request);

    /**
     * 更新账期订单的还款金额
     * @param request
     * @return
     */
    Boolean updatePaymentOrderAmount(UpdatePaymentOrderAmountRequest request);

    /**
     * 根据订单id获取账期订单信息
     * @param orderId
     * @return
     */
    PaymentDaysOrderDO getPaymentDaysOrderDo(Long orderId);

    /**
     * 根据订单id获取账期订单信息
     * @param orderId
     * @return
     */
    PaymentDaysOrderDO getPaymentDaysOrderOne(Long orderId);

    /**
     *查询企业待还款订单
     * @param customerEid
     * @return
     */
    List<PaymentDaysOrderDTO> getUnRepaymentOrderByCustomerEid(Long customerEid);

    /**
     * 账期已使用订单列表/账期已还款订单列表/账期待还款订单列表
     * @param request
     * @return
     */
    Page<PaymentDaysOrderDO> getQuotaOrderPage(QueryQuotaOrderRequest request);

    /**
     * 账期已使用订单列表/账期已还款订单列表/账期待还款订单列表
     * @param request
     * @return
     */
    List<PaymentDaysOrderDO> getQuotaOrderList(QueryQuotaOrderRequest request);
}
